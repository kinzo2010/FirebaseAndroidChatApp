package kiki.chat.firebase.com.firebasechat.ui.message;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySendMessageBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;

public class SendMessageActivity extends BaseActivity {

    ActivitySendMessageBinding activitySendMessageBinding;
    SendMessageViewModel sendMessageViewModel;

    public static final String UID_EXTRA_KEY = "uid-extra-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySendMessageBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_message);
        sendMessageViewModel = new SendMessageViewModel(this, this, activitySendMessageBinding, getIntent().getStringExtra(UID_EXTRA_KEY));

        activitySendMessageBinding.setViewModel(sendMessageViewModel);

    }
}
