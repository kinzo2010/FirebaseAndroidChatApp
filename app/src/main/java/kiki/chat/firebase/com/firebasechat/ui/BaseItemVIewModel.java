package kiki.chat.firebase.com.firebasechat.ui;

import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.databinding.BaseObservable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;

public class BaseItemVIewModel extends BaseObservable {

    protected void clickAlphaEffect(View view, AnimatorListenerAdapter animatorListenerAdapter) {
        ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.32f, 1f);
        fadeAnim.setDuration(200);
        fadeAnim.setInterpolator(new FastOutSlowInInterpolator());
        fadeAnim.addListener(animatorListenerAdapter);
        fadeAnim.start();


    }

}
