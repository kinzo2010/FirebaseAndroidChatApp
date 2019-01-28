package kiki.chat.firebase.com.firebasechat.ui.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.databinding.ActivityHomeBinding;
import kiki.chat.firebase.com.firebasechat.models.Group;
import kiki.chat.firebase.com.firebasechat.models.LatestMessage;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.ui.main.MainActivity;
import kiki.chat.firebase.com.firebasechat.ui.profile.ProfileActivity;
import kiki.chat.firebase.com.firebasechat.ui.search.SearchActivity;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;

public class HomeViewModel extends BaseViewModel{

    public ObservableField<String> fullName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> avatarUrl = new ObservableField<>("");

    private ActivityHomeBinding activityHomeBinding;

    private HomeActivity.AddState addState = HomeActivity.AddState.MESSAGE;

    public ObservableField<String> groupName = new ObservableField<>("");

    public HomeViewModel(Context context, Activity activity, ActivityHomeBinding activityHomeBinding) {
        super(context, activity);
        this.activityHomeBinding = activityHomeBinding;

        activityHomeBinding.blackView.setVisibility(View.GONE);
    }

    public void fetchProfile() {

        showProgressDialog();

        FireBaseDataAccess.getInstance().getCurrentUserProfile(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(user == null) {
                    FirebaseAuth.getInstance().signOut();

                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                } else {
                    if(user.getFullName() == null) {

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);

                    } else {
                        fullName.set(user.getFullName());
                        email.set(user.getEmail());
                        avatarUrl.set(user.getAvatarUrl());
                        dismissProgressDialog();
                    }
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(getClass().getSimpleName(), " error --------> "+databaseError.toString());
                dismissProgressDialog();
                showNotification("Đã xảy ra lỗi");
            }
        });

    }

    public void onClickSearch(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(activity, SearchActivity.class);

                intent.putExtra(SearchActivity.SEARCH_TYPE_EXTRA_KEY, 1);

                activity.startActivity(intent);
            }
        });

    }

    public void onClickAdd(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                switch (addState) {

                    case GROUP: {

                        activityHomeBinding.blackView.setVisibility(View.VISIBLE);
                        activityHomeBinding.addGroupDialog.setVisibility(View.VISIBLE);

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(ObjectAnimator.ofFloat(activityHomeBinding.blackView, "alpha", 1),
                                ObjectAnimator.ofFloat(activityHomeBinding.addGroupDialog, "alpha", 1),
                                ObjectAnimator.ofFloat(activityHomeBinding.addGroupDialog, "translationY",  0));
                        animatorSet.setDuration(250);
                        animatorSet.setInterpolator(new FastOutLinearInInterpolator());
                        animatorSet.start();


                        break;
                    }

                    case MESSAGE: {
                        Intent intent = new Intent(activity, SearchActivity.class);
                        intent.putExtra(SearchActivity.SEARCH_TYPE_EXTRA_KEY, 2);
                        activity.startActivity(intent);
                        break;
                    }
                }


            }
        });

    }

    public void onClickCreateGroup(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(groupName.get().length() < 1) {

                    showNotification("Vui lòng nhập tên nhóm chat.");

                } else {

                    Group group = new Group(groupName.get(), System.currentTimeMillis()/1000);

                    FireBaseDataAccess.getInstance().saveGroupChat(group, new OnSuccessListener<Void>() {

                        @Override
                        public void onSuccess(Void aVoid) {
                            showNotification("Đã tạo nhớm:\n"+groupName.get());

                            groupName.set("");
                            dismissAddGroupDialog();
                        }
                    });


                }
            }
        });

    }

    public void onClickCancel(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dismissAddGroupDialog();

            }
        });

    }

    public void onClickAvatar(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(activity, ProfileActivity.class);
                intent.putExtra(ProfileActivity.PROFILE_TYPE_EXTRA_KEY, 1);
                activity.startActivity(intent);
            }
        });



    }

    public void onClickBlackView(View view) {
        dismissAddGroupDialog();

    }

    public void dismissAddGroupDialog() {



        Log.d(getClass().getSimpleName(), "add dialog height -- > "+ activityHomeBinding.addGroupDialog.getHeight());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(activityHomeBinding.blackView, "alpha", 0),
                ObjectAnimator.ofFloat(activityHomeBinding.addGroupDialog, "translationY",  activityHomeBinding.addGroupDialog.getHeight()));
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new FastOutSlowInInterpolator());

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                activityHomeBinding.blackView.setVisibility(View.GONE);
                activityHomeBinding.addGroupDialog.setVerticalGravity(View.GONE);
                groupName.set("");
            }
        });

        animatorSet.start();

    }

    public HomeActivity.AddState getAddState() {
        return addState;
    }

    public void setAddState(HomeActivity.AddState addState) {
        this.addState = addState;
    }
}
