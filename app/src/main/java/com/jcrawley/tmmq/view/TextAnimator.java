package com.jcrawley.tmmq.view;

import android.text.Spanned;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

public class TextAnimator {

    private final Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
    private final Animation fadeOutAndInAnimation = new AlphaAnimation(1.0f, 0.0f);
    private final TextView textView;
    private Spanned text;


    public TextAnimator(TextView textView){
        this.textView = textView;
        fadeInAnimation.setDuration(250);
        fadeOutAndInAnimation.setDuration(250);

        fadeOutAndInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                textView.setVisibility(View.INVISIBLE);
                textView.setText(text);
                textView.startAnimation(fadeInAnimation);
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });


        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) { /*do nothing */}
            @Override public void onAnimationStart(Animation animation){ textView.setVisibility(View.VISIBLE);}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
    }


    public Animation getFadeInAnimation(){
        return fadeInAnimation;
    }


    public void animateTextChange(Spanned text){
        this.text = text;
        textView.startAnimation(fadeOutAndInAnimation);
    }

}

