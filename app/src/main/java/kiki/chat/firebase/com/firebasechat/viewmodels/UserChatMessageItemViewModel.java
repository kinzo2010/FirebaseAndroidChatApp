package kiki.chat.firebase.com.firebasechat.viewmodels;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.models.ChatMessage;
import kiki.chat.firebase.com.firebasechat.models.GroupChatMessage;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseItemVIewModel;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class UserChatMessageItemViewModel extends BaseItemVIewModel{

    public ObservableField<String> message = new ObservableField<>("");
    private String fromId = "";
    public ObservableField<String> time = new ObservableField<>("");
    public ObservableField<String> avatarUrl = new ObservableField<>("");

    public UserChatMessageItemViewModel(ChatMessage chatMessage, String avatarUrl) {
        this.fromId = chatMessage.getFromId();
        this.message.set(chatMessage.getMessage());
        this.avatarUrl.set(avatarUrl);
        time.set(Util.calculateTime(chatMessage.getTimestamp()));

    }

    public UserChatMessageItemViewModel(GroupChatMessage groupChatMessage) {

        this.fromId = groupChatMessage.getFromId();
        this.message.set(groupChatMessage.getMessage());
        this.time.set(Util.calculateTime(groupChatMessage.getTimestamp()));

        FireBaseDataAccess.getInstance().getUserProfile(fromId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                avatarUrl.set(user.getAvatarUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
