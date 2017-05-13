package com.softark.eddie.gasexpress.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;

public class LogoProgressAnimation extends Animation {

    private final ProgressBar progressBar;
    private final float from;
    private final float to;

    public LogoProgressAnimation(ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.from = (float) 0;
        this.to = (float) 100;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int)value);
    }
}
