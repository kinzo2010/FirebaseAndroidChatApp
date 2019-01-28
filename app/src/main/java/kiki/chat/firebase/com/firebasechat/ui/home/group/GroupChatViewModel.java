package kiki.chat.firebase.com.firebasechat.ui.home.group;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import kiki.chat.firebase.com.firebasechat.adapter.GroupChatListAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.FragmentGroupChatBinding;
import kiki.chat.firebase.com.firebasechat.models.Group;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;

public class GroupChatViewModel extends BaseViewModel{


GroupChatListAdapter groupChatListAdapter = new GroupChatListAdapter();

FragmentGroupChatBinding fragmentGroupChatBinding;

    public GroupChatViewModel(Context context, Activity activity, FragmentGroupChatBinding fragmentGroupChatBinding) {
        super(context, activity);

        this.fragmentGroupChatBinding = fragmentGroupChatBinding;
    }

    public void fetchGroupList() {

        FirebaseDatabase.getInstance().getReference("/group-chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fragmentGroupChatBinding.emptyNotificationText.setVisibility(View.GONE);

                Group group = dataSnapshot.getValue(Group.class);
                groupChatListAdapter.addItem(group);

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

    public GroupChatListAdapter getGroupChatListAdapter() {
        return groupChatListAdapter;
    }

    public void setGroupChatListAdapter(GroupChatListAdapter groupChatListAdapter) {
        this.groupChatListAdapter = groupChatListAdapter;
    }
}
