package kiki.chat.firebase.com.firebasechat.ui.registration;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import kiki.chat.firebase.com.firebasechat.databinding.ActivityRegistrationBinding;
import kiki.chat.firebase.com.firebasechat.models.User;
import kiki.chat.firebase.com.firebasechat.ui.BaseViewModel;
import kiki.chat.firebase.com.firebasechat.ui.home.HomeActivity;
import kiki.chat.firebase.com.firebasechat.ui.main.MainActivity;
import kiki.chat.firebase.com.firebasechat.util.FireBaseDataAccess;
import kiki.chat.firebase.com.firebasechat.util.FireBaseStorageAccess;
import kiki.chat.firebase.com.firebasechat.util.Util;

public class RegistrationViewModel extends BaseViewModel {

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");
    public ObservableField<String> passwordRetype = new ObservableField<>("");
    public ObservableField<String> fullName = new ObservableField<>("");
    private Uri uriImage;

    private String avatarUrl = "";
    private String avatarRef = "";

    public RegistrationViewModel(Context context, Activity activity, ActivityRegistrationBinding activityRegistrationBinding) {
        super(context, activity);
    }

    public void onClickBackToLogin(View view) {

        super.clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        });

    }

    public void onClickRegistration(View view) {

        super.clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (email.get().length() < 6 || !email.get().contains("@") || !email.get().contains(".")) {
                    showNotification("Vui lòng nhập đúng địa chỉ Email.");
                } else {

                    if (!password.get().equals(passwordRetype.get())) {
                        showNotification("Mật khẩu nhập lại không đúng.");

                    } else if (password.get().length() < 6) {
                        showNotification("Mật khẩu phải có độ dài hơn 6 ký tự.");


                    } else {
                        // email và mật khẩu đã nhập đúng. gửi yêu cầu đăng ký tài khoản
                        showProgressDialog();
                        performRegistration();


                    }

                }
            }
        });

    }

    private void performRegistration() {

        if (Util.isInternetConnected(context)) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.get(), password.get()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        //Đăng ksy thành công
                        //Ghi thông tin user vào data base

                        String uid = FirebaseAuth.getInstance().getUid();
                        User user = new User(uid, fullName.get().length() < 1 ? "Chưa cập nhật" : fullName.get(), email.get());

                        FireBaseDataAccess.getInstance().saveUser(user, uriImage, new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {


                                //Xử lý sau khi ghi dữ liệu lên database thành công
                                dismissProgressDialog();
                                Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_LONG).show();

                                Intent intent;

                                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                                    intent = new Intent(activity, HomeActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();

                                } else {

                                    intent = new Intent(activity, MainActivity.class);
                                    activity.startActivity(intent);
                                    activity.finish();

                                }
                            }
                        });



                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            dismissProgressDialog();
                            showNotification("Email không hợp lệ.");

                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            dismissProgressDialog();
                            showNotification("Email đã tồn tại.");

                        } else {
                            dismissProgressDialog();
                            showNotification("Đã có lỗi xảy ra.");
                        }


                    }
                }
            });


        } else {
            dismissProgressDialog();
            showNotification("Không có kết nối Internet.");
        }


    }


    public void onClickAvatar(View view) {

        clickAlphaEffect(view, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activity.startActivityForResult(intent, 0);
            }
        });

    }

    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }
}
