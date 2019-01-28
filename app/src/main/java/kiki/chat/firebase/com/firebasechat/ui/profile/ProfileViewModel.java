package kiki.chat.firebase.com.firebasechat.ui.profile;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivityProfileBinding;
import kiki.chat.firebase.com.firebasechat.models.Friend;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.ui.main.MainActivity;
import kiki.chat.firebase.com.firebasechat.ui.message.SendMessageActivity;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;
import kiki.chat.firebase.com.firebasechat.util.FriendState;

public class ProfileViewModel extends BaseViewModel {

    public ObservableField<String> fullName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> avatarUrl = new ObservableField<>("");

    private ActivityProfileBinding activityProfileBinding;
    private String otherPersonUid;
    private FriendState friendState = FriendState.NOT_IS_FRIEND;
    private EditState editState = EditState.CANCEL_EDIT;

    private Uri uri;
    private User currentUser;

    public ProfileViewModel(Context context, Activity activity, ActivityProfileBinding activityProfileBinding) {
        super(context, activity);

        this.activityProfileBinding = activityProfileBinding;

        activityProfileBinding.actionButtonContainer.setVisibility(View.GONE);

        fetchProfile();

    }

    public ProfileViewModel(Context context, Activity activity, ActivityProfileBinding activityProfileBinding, String uid) {
        super(context, activity);

        activityProfileBinding.logoutButton.setVisibility(View.GONE);
        activityProfileBinding.editButton.setVisibility(View.GONE);

        this.activityProfileBinding = activityProfileBinding;
        this.otherPersonUid = uid;
        fetchProfile(otherPersonUid);
        updateUI();

    }

    private void fetchProfile() {

        showProgressDialog();

        FireBaseDataAccess.getInstance().getCurrentUserProfile(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                currentUser = user;
                fullName.set(user.getFullName());
                email.set(user.getEmail());
                avatarUrl.set(user.getAvatarUrl());
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                dismissProgressDialog();
            }
        });

    }

    private void fetchProfile(String uid) {



        showProgressDialog();

        FireBaseDataAccess.getInstance().getUserProfile(otherPersonUid, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                fullName.set(user.getFullName());
                email.set(user.getEmail());
                avatarUrl.set(user.getAvatarUrl());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("get key", "--> "+ FirebaseDatabase.getInstance().getReference("/friend/" + FirebaseAuth.getInstance().getUid() + "/" + otherPersonUid).child("FriendState"));


            FirebaseDatabase.getInstance().getReference("/friend/" + FirebaseAuth.getInstance().getUid() + "/" + otherPersonUid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Friend friend = dataSnapshot.getValue(Friend.class);
                        if (friend != null && friend.getFriendState() != null) {

                            friendState = friend.getFriendState();
                            updateUI();
                            dismissProgressDialog();

                        }
                    } else {
                        friendState = FriendState.NOT_IS_FRIEND;
                        updateUI();
                        dismissProgressDialog();
                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });


    }



    public void onClickSendMessage(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                Intent intent = new Intent(activity, SendMessageActivity.class);
                intent.putExtra(SendMessageActivity.UID_EXTRA_KEY, otherPersonUid);
                activity.startActivity(intent);

            }
        });
    }

    public void onClickFriendButton(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                switch (friendState) {

                    case NOT_IS_FRIEND: {

                        FireBaseDataAccess.getInstance().sendFriendRequest(otherPersonUid);
                        friendState = FriendState.WAIT_FOR_ACCEPT;
                        updateUI();
                        break;
                    }

                }
            }
        });


    }

    public void onClickSave(View view) {



        if(editState == EditState.EDITING) {
            editState = EditState.CANCEL_EDIT;
            clickAlphaEffect(view, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {

                    currentUser.setFullName(fullName.get());
                    showProgressDialog();

                    if(uri != null) {

                        FireBaseDataAccess.getInstance().upadeUserProfile(currentUser, uri, new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {

                                /**
                                 * update lên server thành công
                                 *
                                 */
                                cancelEdit();

                            }
                        });

                    } else {

                        FireBaseDataAccess.getInstance().updateUserProfile(currentUser, new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {

                                cancelEdit();

                            }
                        });

                    }




                }
            });
        }



    }

    public void onClickEdit(View view) {

        if(editState == EditState.CANCEL_EDIT) {
            editState = EditState.EDITING;
            activityProfileBinding.editAvatarLabel.setVisibility(View.VISIBLE);
            activityProfileBinding.editTextFullName.setEnabled(true);

            clickAlphaEffect(view, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activityProfileBinding.saveButton, "translationY", 0);
                    objectAnimator.setDuration(250);
                    objectAnimator.setInterpolator(new FastOutSlowInInterpolator());
                    objectAnimator.start();
                    activityProfileBinding.editButton.setImageResource(R.drawable.ic_close);
                }
            });
        } else {

            cancelEdit();
        }




    }

    private void cancelEdit() {
        editState = EditState.CANCEL_EDIT;
        activityProfileBinding.editAvatarLabel.setVisibility(View.GONE);
        activityProfileBinding.editTextFullName.setEnabled(false);
        activityProfileBinding.editButton.setImageResource(R.drawable.ic_create);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(activityProfileBinding.saveButton, "translationY", activityProfileBinding.saveButton.getHeight()+2);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new FastOutLinearInInterpolator());
        objectAnimator.start();

        fetchProfile();

    }


    public void onClickAvatar(View view) {

        if(editState == EditState.EDITING) {

            clickAlphaEffect(view, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    activity.startActivityForResult(intent, 0);
                }
            });
        }

    }

    public void onClickLogout(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

    }

    private void updateUI() {

        switch (friendState) {

            case FRIEND: {
                activityProfileBinding.friendButton.setText("Bạn bè");

                break;
            }

            case NOT_IS_FRIEND: {
                activityProfileBinding.friendButton.setText("Gửi yêu cầu");
                break;
            }

            case WAIT_FOR_ACCEPT: {
                activityProfileBinding.friendButton.setText("Đã gửi yêu cầu");
                break;
            }

            case FRIEND_REQUEST: {
                activityProfileBinding.friendButton.setText("Chờ đồng ý");
                break;
            }
        }

    }
    public void onClickBack(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(getClass().getSimpleName(), " -----> profile finish");
                activity.finish();
            }
        });
    }

    enum EditState {

        EDITING, CANCEL_EDIT

    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
