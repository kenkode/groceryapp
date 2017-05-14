package com.softark.eddie.gasexpress.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.softark.eddie.gasexpress.core.ApplicationConfiguration;

public class Internet extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public Internet() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnected();

        if(connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChange(isConnected);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationConfiguration.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChange(boolean isConnected);
    }


}
