package kiki.chat.firebase.com.firebasechat.ui.home;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.adapter.HomePagerAdapter;
import kiki.chat.firebase.com.firebasechat.databinding.ActivityHomeBinding;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;

public class HomeActivity extends BaseActivity {

    ActivityHomeBinding activityHomeBinding;
    HomeViewModel homeViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());

        activityHomeBinding.viewPager.setAdapter(homePagerAdapter);

        activityHomeBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("pager", "page ---> "+position);
                changeIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        activityHomeBinding.messageIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHomeBinding.viewPager.setCurrentItem(0);
                changeIndicator(0);
            }
        });

        activityHomeBinding.groupChatIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHomeBinding.viewPager.setCurrentItem(1);
                changeIndicator(1);
            }
        });


        homeViewModel = new HomeViewModel(this, this, activityHomeBinding);
        activityHomeBinding.setViewModel(homeViewModel);

    }

    private void changeIndicator(int position) {

        switch (position) {

            case 0 : {
                homeViewModel.setAddState(AddState.MESSAGE);
                activityHomeBinding.buttonAdd.setImageResource(R.drawable.ic_create);

                activityHomeBinding.messageIndicator.setTextColor(getResources().getColor(R.color.white));
                activityHomeBinding.messageIndicator.setBackground(getDrawable(R.drawable.custom_tab_active_background));

                activityHomeBinding.groupChatIndicator.setTextColor(getResources().getColor(R.color.grayDark));
                activityHomeBinding.groupChatIndicator.setBackground(getDrawable(R.drawable.custom_tab_disable_background));

                break;
            }

            case 1 : {

                homeViewModel.setAddState(AddState.GROUP);
                activityHomeBinding.buttonAdd.setImageResource(R.drawable.icon_add);
                activityHomeBinding.groupChatIndicator.setTextColor(getResources().getColor(R.color.white));
                activityHomeBinding.groupChatIndicator.setBackground(getDrawable(R.drawable.custom_tab_active_background));

                activityHomeBinding.messageIndicator.setTextColor(getResources().getColor(R.color.grayDark));
                activityHomeBinding.messageIndicator.setBackground(getDrawable(R.drawable.custom_tab_disable_background));

                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        homeViewModel.dismissAddGroupDialog();
        homeViewModel.fetchProfile();
    }

    enum AddState {
        MESSAGE, GROUP


    }


}
