package kiki.chat.firebase.com.firebasechat.ui.message;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import kiki.chat.firebase.com.firebasechat.adapter.UserChatMessageListAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySendMessageToGroupBinding;
import kiki.chat.firebase.com.firebasechat.models.ChatMessage;
import kiki.chat.firebase.com.firebasechat.models.GroupChatMessage;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;

public class SendMessageToGroupViewModel extends BaseViewModel{

    ActivitySendMessageToGroupBinding activitySendMessageToGroupBinding;

    UserChatMessageListAdapter userChatMessageListAdapter = new UserChatMessageListAdapter();

    public ObservableField<String> groupName = new ObservableField<>("");

    public ObservableField<String> message = new ObservableField<>("");

    private String groupID;

    public SendMessageToGroupViewModel(Context context, Activity activity, String groupID,ActivitySendMessageToGroupBinding activitySendMessageToGroupBinding) {
        super(context, activity);
        this.activitySendMessageToGroupBinding = activitySendMessageToGroupBinding;
        this.groupID = groupID;


        fetchGroupMessage();

    }

    public void fetchGroupMessage() {

        FirebaseDatabase.getInstance().getReference("/group-chat-message/"+groupID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                GroupChatMessage groupChatMessage = dataSnapshot.getValue(GroupChatMessage.class);

                userChatMessageListAdapter.addItemGroupChat(groupChatMessage);


                activitySendMessageToGroupBinding.messageListView.scrollToPosition(userChatMessageListAdapter.getItemCount()-1);

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

    public void onClickSendMessage(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if(message.get().length() > 0) {

                    GroupChatMessage groupChatMessage = new GroupChatMessage(FirebaseAuth.getInstance().getUid(), message.get(), System.currentTimeMillis()/1000);
                    message.set("");
                    FireBaseDataAccess.getInstance().sendMessageToGroup(groupChatMessage, groupID);

                } else {

                    showNotification("Hãy nhập tin nhắn");

                }

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
