package kiki.chat.firebase.com.firebasechat.ui.registration;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivityRegistrationBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;
import kiki.chat.firebase.com.firebasechat.ui.main.MainActivity;


public class RegistrationActivity extends BaseActivity {

    ActivityRegistrationBinding activityRegistrationBinding;
    RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRegistrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        registrationViewModel = new RegistrationViewModel(this, this, activityRegistrationBinding);
        activityRegistrationBinding.setViewModel(registrationViewModel);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Log.d("bit map size", bitmap.getWidth()+"*"+bitmap.getHeight());

                if(bitmap.getHeight() <= 4096 && bitmap.getWidth() <= 4096) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
                    activityRegistrationBinding.avatar.setImageDrawable(bitmapDrawable);
                    registrationViewModel.setUriImage(uri);
                    Log.d("URI", "uri --> "+uri);
                } else {

                    registrationViewModel.showNotification("Ảnh quá lớn("+bitmap.getWidth()+"x"+bitmap.getHeight()+")\nKích thước tối đa 4096x4096");

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
