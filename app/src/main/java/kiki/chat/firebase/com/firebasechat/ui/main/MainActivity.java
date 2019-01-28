package kiki.chat.firebase.com.firebasechat.ui.main;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivityMainBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;

public class MainActivity extends BaseActivity {

    ActivityMainBinding activityMainBinding;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new MainViewModel(this, this, activityMainBinding);
        activityMainBinding.setViewModel(mainViewModel);
    }
}
