package com.softark.eddie.gasexpress.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.softark.eddie.gasexpress.core.ApplicationConfiguration;

/**
 * Created by Eddie on 5/10/2017.
 */

public class Internet extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public Internet() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if(connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChange(isConnected);
        }
    }

    public static boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationConfiguration.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChange(boolean isConnected);
    }


}
