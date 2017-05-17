package com.softark.eddie.gasexpress.helpers;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

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

    private static boolean isConnected(@NonNull Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) ApplicationConfiguration.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);

        return isConnected(ApplicationConfiguration.getInstance()) ||
                isWifiConnected(ApplicationConfiguration.getInstance()) ||
                isMobileConnected(ApplicationConfiguration.getInstance()) ||
                isConnected(connMgr, ConnectivityManager.TYPE_MOBILE) ||
                isConnected(connMgr, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isWifiConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isMobileConnected(@NonNull Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    private static boolean isConnected(@NonNull Context context, int type) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(type);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return isConnected(connMgr, type);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean isConnected(@NonNull ConnectivityManager connMgr, int type) {
        Network[] networks = connMgr.getAllNetworks();
        NetworkInfo networkInfo;
        for (Network mNetwork : networks) {
            networkInfo = connMgr.getNetworkInfo(mNetwork);
            if (networkInfo != null && networkInfo.getType() == type && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChange(boolean isConnected);
    }


}
