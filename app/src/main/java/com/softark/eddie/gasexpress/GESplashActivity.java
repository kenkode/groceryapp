package com.softark.eddie.gasexpress;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.softark.eddie.gasexpress.animations.LogoProgressAnimation;
import com.softark.eddie.gasexpress.helpers.GEPreference;

public class GESplashActivity extends AppCompatActivity {

    public static final int SPLASH_TIME = 5000;

    private ProgressBar logoProgress;
    private LogoProgressAnimation logoProgressAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesplash);

        logoProgress = (ProgressBar) findViewById(R.id.logo_progress);

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateProgress();
                Intent intent = new Intent(GESplashActivity.this, GELoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }

    public void updateProgress() {
        try {
        logoProgressAnimation = new LogoProgressAnimation(logoProgress, 0, 100);
        logoProgressAnimation.setDuration(SPLASH_TIME);
        logoProgress.startAnimation(logoProgressAnimation);
        Thread.sleep(SPLASH_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
