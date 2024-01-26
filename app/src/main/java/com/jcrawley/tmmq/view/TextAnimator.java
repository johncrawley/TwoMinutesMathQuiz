package com.jcrawley.tmmq.view;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class TextAnimator {

    final Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
    final Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);


    public TextAnimator(TextView itemTextView, int defaultColor){
        fadeOutAnimation.setDuration(250);
        fadeInAnimation.setDuration(200);
        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                itemTextView.setTextColor(defaultColor);
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
    }


    public Animation getFadeInAnimation(){
        return fadeInAnimation;
    }


    public Animation getFadeOutAnimation(){
        return fadeOutAnimation;
    }

}

