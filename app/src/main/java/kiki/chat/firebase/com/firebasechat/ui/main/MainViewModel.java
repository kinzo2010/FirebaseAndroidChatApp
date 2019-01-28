package kiki.chat.firebase.com.firebasechat.ui.main;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import kiki.chat.firebase.com.firebasechat.databinding.ActivityMainBinding;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.ui.home.HomeActivity;
import kiki.chat.firebase.com.firebasechat.ui.registration.RegistrationActivity;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class MainViewModel extends BaseViewModel {

    /***
     *
     * {@link ActivityMainBinding}
     *
     */

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");

    public MainViewModel(Context context, Activity activity, ActivityMainBinding activityMainBinding) {
        super(context, activity);
    }

    public void onClickRegistration(View view) {

        super.clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(activity, RegistrationActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

    }

    public void onClickLogin(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(email.get().length() < 6 || password.get().length() < 6) {

                    showNotification("Vui lòng nhập địa chỉ Email và Mật khẩu.");

                } else {

                    showProgressDialog();
                    performLogin();

                }
            }
        });



    }

    private void performLogin() {

        if(Util.isInternetConnected(context)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email.get(), password.get())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {

                                dismissProgressDialog();
                                Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(activity, HomeActivity.class);
                                activity.startActivity(intent);
                                activity.finish();

                            } else {
                                dismissProgressDialog();

                                if(task.getException() instanceof FirebaseAuthInvalidUserException) {
                                    dismissProgressDialog();
                                    showNotification("Email không tồn tại.");

                                } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    dismissProgressDialog();
                                    showNotification("Sai mật khẩu.");

                                } else {
                                    dismissProgressDialog();
                                    showNotification("Xảy ra lỗi không xác định.");

                                }

                            }
                        }
                    });
        } else {

            dismissProgressDialog();
            showNotification("Không có kết nối Internet.");
        }


    }

}
