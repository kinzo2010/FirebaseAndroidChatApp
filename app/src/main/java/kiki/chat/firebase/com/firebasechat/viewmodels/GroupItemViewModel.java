package kiki.chat.firebase.com.firebasechat.viewmodels;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import kiki.chat.firebase.com.firebasechat.models.Group;
import kiki.chat.firebase.com.firebasechat.ui.BaseItemVIewModel;
import kiki.chat.firebase.com.firebasechat.ui.message.SendMessageToGroupActivity;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class GroupItemViewModel extends BaseItemVIewModel{

    public ObservableField<String> groupName = new ObservableField<>("");
    private String groupID;
    public ObservableField<String> time = new ObservableField<>("");

    Activity activity;

    public GroupItemViewModel(Group group) {

        groupName.set(group.getName());
        groupID = group.getGroupID();
        time.set(Util.calculateTime(group.getTimestamp()));

    }

    public void onClickitem(View view) {

        activity = (Activity)view.getContext();

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                Intent intent = new Intent(activity, SendMessageToGroupActivity.class);
                intent.putExtra(SendMessageToGroupActivity.GROUP_ID_EXTRA_KEY, groupID);
                intent.putExtra(SendMessageToGroupActivity.GROUP_NAME_EXTRA_KEY, groupName.get());
                activity.startActivity(intent);

            }
        });

    }

}
