package com.example.agapelife.utils;

import android.graphics.drawable.AnimationDrawable;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.agapelife.R;

public class AnimationsConfig {

    public AnimationsConfig() {
    }

    public void createGradientAnimation(ConstraintLayout bgLayout, int enterFadeDuration, int exitFadeDuration) {
        bgLayout.setBackgroundResource(R.drawable.background_gradient_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) bgLayout.getBackground();
        animationDrawable.setEnterFadeDuration(enterFadeDuration);
        animationDrawable.setExitFadeDuration(exitFadeDuration);
        animationDrawable.start();
    }
}
