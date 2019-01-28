package kiki.chat.firebase.com.firebasechat.ui.home.message;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import kiki.chat.firebase.com.firebasechat.adapter.LatestMessageListAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.FragmentMessageListBinding;
import kiki.chat.firebase.com.firebasechat.models.LatestMessage;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;

public class MessageListViewModel extends BaseViewModel {

    FragmentMessageListBinding fragmentMessageListBinding;

    LatestMessageListAdapter latestMessageListAdapter = new LatestMessageListAdapter();

    public MessageListViewModel(Context context, Activity activity, FragmentMessageListBinding fragmentMessageListBinding) {
        super(context, activity);

        this.fragmentMessageListBinding = fragmentMessageListBinding;
    }

    public void fetchLatestMessage() {

        FirebaseDatabase.getInstance().getReference("/latest-message/" + FirebaseAuth.getInstance().getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                fragmentMessageListBinding.emptyNotificationText.setVisibility(View.GONE);
                LatestMessage latestMessage = dataSnapshot.getValue(LatestMessage.class);
                latestMessageListAdapter.addItem(latestMessage);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LatestMessage latestMessage = dataSnapshot.getValue(LatestMessage.class);
                latestMessageListAdapter.changeData(latestMessage);

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

    public void updateUI() {
        latestMessageListAdapter.updateTime();


    }

    public LatestMessageListAdapter getLatestMessageListAdapter() {
        return latestMessageListAdapter;
    }

    public void setLatestMessageListAdapter(LatestMessageListAdapter latestMessageListAdapter) {
        this.latestMessageListAdapter = latestMessageListAdapter;
    }
}
