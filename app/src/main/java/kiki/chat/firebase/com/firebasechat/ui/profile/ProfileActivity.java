package kiki.chat.firebase.com.firebasechat.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import kiki.chat.firebase.com.firebasechat.R;
import kiki.chat.firebase.com.firebasechat.databinding.ActivityProfileBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseActivity;

public class ProfileActivity extends BaseActivity {

    ActivityProfileBinding activityProfileBinding;
    ProfileViewModel profileViewModel;


    /**
     *
     * Lấy dữ liệu từ Intent với key bên dưới sẽ biết được loại đối tượng cần xem thông tin cá nhân
     * 1 - Thông tin của chính mình
     * 2 - Thông tin của người khác
     *
     */

    public static final String PROFILE_TYPE_EXTRA_KEY = "profile-type-extra-key";
    public static final String UID_EXTRA_KEY = "uid-extra-key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        switch (getIntent().getIntExtra(PROFILE_TYPE_EXTRA_KEY, 1)) {

            case 1: {
                profileViewModel = new ProfileViewModel(this, this, activityProfileBinding);
                activityProfileBinding.setViewModel(profileViewModel);
                break;
            }

            case 2 : {
                profileViewModel = new ProfileViewModel(this, this, activityProfileBinding, getIntent().getStringExtra(UID_EXTRA_KEY));
                activityProfileBinding.setViewModel(profileViewModel);
                break;
            }

        }



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
                    activityProfileBinding.avatar.setImageDrawable(bitmapDrawable);
                    profileViewModel.setUri(uri);
                    Log.d("URI", "uri --> "+uri);
                } else {

                    profileViewModel.showNotification("Ảnh quá lớn("+bitmap.getWidth()+"x"+bitmap.getHeight()+")\nKích thước tối đa 4096x4096");

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
