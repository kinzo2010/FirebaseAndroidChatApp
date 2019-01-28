package kiki.chat.firebase.com.firebasechat.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    public static SearchType searchType = SearchType.PROFILE;

    public static boolean isInternetConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

    public static String calculateTime(long timestamp) {

        long deltaTime = System.currentTimeMillis()/1000 - timestamp;

        if(deltaTime  < 60) {
            return "vài giây trước";

        }  else if (deltaTime < 60*60) {

            return deltaTime/60 + " phút trước";
        } else if (deltaTime < 12*60*60) {

            return deltaTime/60/60 +"giờ trước";

        } else {
            String pattern = "HH:mm dd/MM/yyyy";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = new Date(timestamp*1000);

            return simpleDateFormat.format(date);
        }

    }

}
