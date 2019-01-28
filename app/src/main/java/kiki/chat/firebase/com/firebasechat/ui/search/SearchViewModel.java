package kiki.chat.firebase.com.firebasechat.ui.search;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.adapter.UserListAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySearchBinding;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;
import kiki.chat.firebase.com.firebasechat.util.SearchType;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class SearchViewModel extends BaseViewModel{

    ActivitySearchBinding activitySearchBinding;
    private SearchType searchType;

    private UserListAdapter userListAdapter = new UserListAdapter();

    public SearchViewModel(Context context, Activity activity, SearchType searchType, ActivitySearchBinding activitySearchBinding) {
        super(context, activity);
        this.activitySearchBinding = activitySearchBinding;
        this.searchType = searchType;

        switch (searchType) {

            case FOR_SEND_MESSAGE: {
                Util.searchType = SearchType.FOR_SEND_MESSAGE;
                fetchUserList();
                break;
            }

            case PROFILE: {
                Util.searchType = SearchType.PROFILE;
                fetchUserList();
                break;
            }

        }

    }

    private void fetchUserList() {

        activitySearchBinding.progressBar.setVisibility(View.VISIBLE);

        FireBaseDataAccess.getInstance().getUserList(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot1.getValue(User.class);
                    if (user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                        continue;
                    userListAdapter.addItem(user);
                }

                activitySearchBinding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(getClass().getSimpleName(), " error --------> "+databaseError.toString());
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



    public UserListAdapter getUserListAdapter() {
        return userListAdapter;
    }

    public void setUserListAdapter(UserListAdapter userListAdapter) {
        this.userListAdapter = userListAdapter;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }
}
