package kiki.chat.firebase.com.firebasechat.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.ui.home.HomeActivity;
import kiki.chat.firebase.com.firebasechat.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        final Intent intent;

        if(FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getUid() != null) {
            intent = new Intent(this, HomeActivity.class);

        } else {

            intent = new Intent(this, MainActivity.class);
            Log.d(TAG, "null user");

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 1000);

    }
}
