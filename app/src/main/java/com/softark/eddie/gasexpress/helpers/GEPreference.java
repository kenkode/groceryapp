package com.softark.eddie.gasexpress.helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Eddie on 4/16/2017.
 */

public class GEPreference {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    public static final String PREFERENCES = "ge_prefs";
    public static final String SPLASH_SHOWN = "splash_shown";

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



}
