package kiki.chat.firebase.com.firebasechat.viewmodels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.models.LatestMessage;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseItemVIewModel;
import kiki.chat.firebase.com.firebasechat.ui.message.SendMessageActivity;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class LatestMessageItemViewModel extends BaseItemVIewModel{

    public ObservableField<String> fullName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> message = new ObservableField<>("");
    public ObservableField<String> time = new ObservableField<>("");
    public ObservableField<String> avatarUrl = new ObservableField<>("");
    public ObservableInt newMessageVisibility = new ObservableInt(View.GONE);

    private String uid;
    private String ownerUid;
    private long timestamp;

    Activity activity;

    public LatestMessageItemViewModel(LatestMessage latestMessage) {
        this.uid = latestMessage.getUid();
        this.ownerUid = latestMessage.getOwnerUid();
        message.set(latestMessage.getMessage());
        this.timestamp = latestMessage.getTimestamp();
        time.set(Util.calculateTime(latestMessage.getTimestamp()));
        FireBaseDataAccess.getInstance().getUserProfile(latestMessage.getUid(), new ValueEventListener() {
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

    }

    public void updateTime() {

        time.set(Util.calculateTime(timestamp));

    }

    public void onClickItem(View view) {
        activity = (Activity) view.getContext();
        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {


                Intent intent = new Intent(activity, SendMessageActivity.class);
                intent.putExtra(SendMessageActivity.UID_EXTRA_KEY, uid);
                activity.startActivity(intent);
                newMessageVisibility.set(View.GONE);
            }
        });

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }
}
