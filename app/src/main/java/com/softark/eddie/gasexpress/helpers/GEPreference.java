package com.softark.eddie.gasexpress.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class GEPreference {

    private static final String USER_EMAIL = "user_email";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static final String PREFERENCES = "ge_prefs";
    private static final String SPLASH_SHOWN = "splash_shown";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE= "user_phone";
    private static final String USER_LOGGED = "user_logged";
    public static final String USER_NAME = "user_name";
    private static final String ORDER_KEY = "order_key";

    public GEPreference(Context context) {
        Context context1 = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getOrderKey() {
        return sharedPreferences.getString(ORDER_KEY, "");
    }

    public void setOrderKey(String orderKey) {
        editor.putString(ORDER_KEY, orderKey);
        editor.apply();
    }

    public void setSplashShown(boolean splashShown) {
        editor.putBoolean(SPLASH_SHOWN, splashShown);
        editor.apply();
    }

    public boolean getSplashShown() {
        return sharedPreferences.getBoolean(SPLASH_SHOWN, false);
    }

// User preference
    public void setUser(String id, String name, String phone, String email) {
        editor.putString(USER_ID, id);
        editor.putString(USER_NAME, name);
        editor.putString(USER_PHONE, phone);
        editor.putString(USER_EMAIL, email);
        editor.putBoolean(USER_LOGGED, true);
        editor.apply();
    }

    public void unsetUser() {
        editor.clear();
        editor.apply();
    }

    public Map<String, String> getUser() {
        Map<String, String> user = new HashMap<>();
        user.put(USER_ID, sharedPreferences.getString(USER_ID, ""));
        user.put(USER_NAME, sharedPreferences.getString(USER_NAME, ""));
        user.put(USER_PHONE, sharedPreferences.getString(USER_PHONE, ""));
        user.put(USER_EMAIL, sharedPreferences.getString(USER_EMAIL, ""));
        return user;
    }

    public boolean isUserLogged() {
        return sharedPreferences.getBoolean(USER_LOGGED, false);
    }
}
