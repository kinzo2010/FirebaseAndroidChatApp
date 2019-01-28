package kiki.chat.firebase.com.firebasechat.ui;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import kiki.chat.firebase.com.firebasechat.R;

public class BaseViewModel extends BaseObservable {

    private AppCompatTextView dialog;
    private View progressDialog;

    protected Context context;
    protected Activity activity;

    public BaseViewModel(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    public void clickAlphaEffect(View view, AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.32f, 1f);
        fadeAnim.setDuration(200);
        fadeAnim.setInterpolator(new FastOutSlowInInterpolator());
        fadeAnim.addListener(animatorListenerAdapter);
        fadeAnim.start();


    }

    public void showNotification(String message) {

        if(dialog == null) {
            dialog = new AppCompatTextView(context);
            dialog.setBackground(context.getDrawable(R.drawable.notification_background));
            dialog.setTextColor(activity.getResources().getColor(R.color.white));
            dialog.setTextSize(20);
            dialog.setText(message);
            dialog.setElevation(24);
            dialog.setTypeface(ResourcesCompat.getFont(context, R.font.roboto_bold));
            dialog.setGravity(Gravity.CENTER);
            dialog.setAlpha(0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            layoutParams.setMargins(32,0,32, 0);

            activity.addContentView(dialog, layoutParams);
        } else {
            dialog.setText(message);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(dialog, "alpha", 1),
                ObjectAnimator.ofFloat(dialog, "translationY",  16));
        animatorSet.setDuration(250);
        animatorSet.setInterpolator(new FastOutLinearInInterpolator());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(ObjectAnimator.ofFloat(dialog, "alpha",  0),
                                ObjectAnimator.ofFloat(dialog, "translationY", 0));
                        animatorSet.setDuration(200);
                        animatorSet.setInterpolator(new FastOutSlowInInterpolator());

                        animatorSet.start();

                    }
                }, 3000);
            }
        });

        animatorSet.start();

    }

    public void showProgressDialog() {

        if(progressDialog == null) {
            progressDialog = LayoutInflater.from(context).inflate(R.layout.propress_dialog_layout, null, false);
            progressDialog.setAlpha(0);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            activity.addContentView(progressDialog, layoutParams);

        }

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(progressDialog, "alpha",1);
        objectAnimator.setDuration(250);
        objectAnimator.setInterpolator(new FastOutLinearInInterpolator());
        objectAnimator.start();
    }

    public void dismissProgressDialog() {

        if(progressDialog != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(progressDialog, "alpha",0);
                    objectAnimator.setDuration(200);
                    objectAnimator.setInterpolator(new FastOutLinearInInterpolator());
                    objectAnimator.start();
                }
            }, 200);

        }

    }
}
