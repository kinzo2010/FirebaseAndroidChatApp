package kiki.chat.firebase.com.firebasechat.ui.message;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import kiki.chat.firebase.com.firebasechat.adapter.UserChatMessageListAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySendMessageBinding;
import kiki.chat.firebase.com.firebasechat.models.ChatMessage;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;

public class SendMessageViewModel extends BaseViewModel {

    private String toId;
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> fullName = new ObservableField<>("");
    private String avatarUrl = "";
    public ObservableField<String> message = new ObservableField<>("");

    private UserChatMessageListAdapter userChatMessageListAdapter = new UserChatMessageListAdapter();
    ActivitySendMessageBinding activitySendMessageBinding;

    public SendMessageViewModel(Context context, Activity activity, ActivitySendMessageBinding activitySendMessageBinding, String uidTo) {
        super(context, activity);
        this.toId = uidTo;
        this.activitySendMessageBinding = activitySendMessageBinding;
        fetchProfile();


    }

    public void onClickSendMessage(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(message.get().length() > 0) {

                    ChatMessage chatMessage = new ChatMessage(FirebaseAuth.getInstance().getUid(), message.get(), System.currentTimeMillis()/1000);
                    message.set("");
                    FireBaseDataAccess.getInstance().sendMessageToUser(chatMessage, toId);

                } else {

                    showNotification("Hãy nhập tin nhắn");

                }
            }
        });



    }

    private void fetchChatHistory() {
        Log.d(getClass().getSimpleName(), "get chat history ---> of to id "+toId);
        FirebaseDatabase.getInstance().getReference("/user-message/"+FirebaseAuth.getInstance().getUid()+"/"+toId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                if(chatMessage.getFromId() != null) {
                    userChatMessageListAdapter.addItem(chatMessage);

                }
                activitySendMessageBinding.messageListView.scrollToPosition(userChatMessageListAdapter.getItemCount()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchProfile() {
        showProgressDialog();
        FireBaseDataAccess.getInstance().getUserProfile(toId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                fullName.set(user.getFullName());
                email.set(user.getEmail());
                userChatMessageListAdapter.setAvatarUrl(user.getAvatarUrl());
                avatarUrl = user.getAvatarUrl();
                dismissProgressDialog();

                //get lịch sử chat
                fetchChatHistory();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public void onClickBack(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                activity.finish();
            }
        });

    }

    public UserChatMessageListAdapter getUserChatMessageListAdapter() {
        return userChatMessageListAdapter;
    }

    public void setUserChatMessageListAdapter(UserChatMessageListAdapter userChatMessageListAdapter) {
        this.userChatMessageListAdapter = userChatMessageListAdapter;
    }
}
