package kiki.chat.firebase.com.firebasechat.viewmodels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseItemVIewModel;
import kiki.chat.firebase.com.firebasechat.ui.message.SendMessageActivity;
import kiki.chat.firebase.com.firebasechat.ui.profile.ProfileActivity;
import kiki.chat.firebase.com.firebasechat.ui.search.SearchViewModel;
import kiki.chat.firebase.com.firebasechat.util.SearchType;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class UserItemViewModel extends BaseItemVIewModel {
    Activity activity;
    private String uid = "";

    public ObservableField<String> fullName = new ObservableField<>("");
    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> avatarUrl = new ObservableField<>("");

    public UserItemViewModel(User user) {
        fullName.set(user.getFullName());
        email.set(user.getEmail());
        uid = user.getUid();
        avatarUrl.set(user.getAvatarUrl());
    }

    public void onClickItem(View view) {
        activity = (Activity) view.getContext();


        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;
                if (Util.searchType == SearchType.PROFILE) {
                    intent = new Intent(activity, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.UID_EXTRA_KEY, uid);
                    intent.putExtra(ProfileActivity.PROFILE_TYPE_EXTRA_KEY, 2);
                } else {
                    intent = new Intent(activity, SendMessageActivity.class);
                    intent.putExtra(SendMessageActivity.UID_EXTRA_KEY, uid);
                }


                activity.startActivity(intent);
            }
        });

    }
}
