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
    public static final String USER_FNAME = "user_fname";
    public static final String USER_LNAME = "user_lname";

    private static final String PAYMENT_OPTION = "payment_option";
    private static final String TOKEN = "token";

    public GEPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setPaymentOption(String paymentOption) {
        editor.putString(PAYMENT_OPTION, paymentOption);
        editor.apply();
    }

    public String getPaymentOption() {
        return sharedPreferences.getString(PAYMENT_OPTION, "");
    }

    public void setToken(String token) {
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN, "");
    }

// User preference
    public void setUser(String id, String fname, String lname, String phone, String email) {
        editor.putString(USER_ID, id);
        editor.putString(USER_FNAME, fname);
        editor.putString(USER_LNAME, lname);
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
        user.put(USER_FNAME, sharedPreferences.getString(USER_FNAME, ""));
        user.put(USER_LNAME, sharedPreferences.getString(USER_LNAME, ""));
        user.put(USER_PHONE, sharedPreferences.getString(USER_PHONE, ""));
        user.put(USER_EMAIL, sharedPreferences.getString(USER_EMAIL, ""));
        return user;
    }

    public boolean isUserLogged() {
        return sharedPreferences.getBoolean(USER_LOGGED, false);
    }
}
