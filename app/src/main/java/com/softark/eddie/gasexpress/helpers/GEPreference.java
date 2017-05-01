package com.softark.eddie.gasexpress.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eddie on 4/16/2017.
 */

public class GEPreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String PREFERENCES = "ge_prefs";
    public static final String SPLASH_SHOWN = "splash_shown";
    public static final String USER_ID = "user_id";
    public static final String USER_PHONE= "user_phone";
    public static final String USER_LOGGED = "user_logged";
    public static final String USER_NAME = "user_name";

    public GEPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setSplashShown(boolean splashShown) {
        editor.putBoolean(SPLASH_SHOWN, splashShown);
        editor.apply();
    }

    public boolean getSplashShown() {
        return sharedPreferences.getBoolean(SPLASH_SHOWN, false);
    }

// User preference
    public void setUser(String id, String name, String phone) {
        editor.putString(USER_ID, id);
        editor.putString(USER_NAME, name);
        editor.putString(USER_PHONE, phone);
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
        return user;
    }

    public boolean isUserLogged() {
        return sharedPreferences.getBoolean(USER_LOGGED, false);
    }
}
