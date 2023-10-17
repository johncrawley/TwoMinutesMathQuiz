package com.jcrawley.tmmq.view;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class TextAnimator {

    final Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
    final Animation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);

    private String nextText = "";

    public TextAnimator(TextView itemTextView){

        fadeOutAnimation.setDuration(200);
        fadeInAnimation.setDuration(200);


        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                itemTextView.setText(nextText);
                itemTextView.startAnimation(fadeInAnimation);
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
    }


    public void setNextText(String text){
        nextText = text;
    }


    public Animation getFadeInAnimation(){
        return fadeInAnimation;
    }


    public Animation getFadeOutAnimation(){
        return fadeOutAnimation;
    }

}

