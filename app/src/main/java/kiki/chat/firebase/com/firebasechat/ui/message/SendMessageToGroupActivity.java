package kiki.chat.firebase.com.firebasechat.ui.message;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivitySendMessageToGroupBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;

public class SendMessageToGroupActivity extends BaseActivity {

    ActivitySendMessageToGroupBinding activitySendMessageToGroupBinding;
    SendMessageToGroupViewModel sendMessageToGroupViewModel;

    public static final String GROUP_ID_EXTRA_KEY = "group-id-extra-key";
    public static final String GROUP_NAME_EXTRA_KEY = "group-name-extra-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySendMessageToGroupBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_message_to_group);
        sendMessageToGroupViewModel = new SendMessageToGroupViewModel(this, this, getIntent().getStringExtra(GROUP_ID_EXTRA_KEY),activitySendMessageToGroupBinding);
        activitySendMessageToGroupBinding.setViewModel(sendMessageToGroupViewModel);

        sendMessageToGroupViewModel.groupName.set(getIntent().getStringExtra(GROUP_NAME_EXTRA_KEY));

    }
}
