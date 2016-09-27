package com.amaral.appaluno.activity.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
    public static boolean IsInternetConnectionEnabled(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        try {
            return (netInfo != null && netInfo.isConnected());
        } catch (Exception ex) {
            return false;
        }
    }
}
