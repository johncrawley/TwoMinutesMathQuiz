package com.jcrawley.tmmq.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;

public class ScreenAnimator {

    private final MainActivity activity;
    private final MainViewModel viewModel;
    private TextView gameStartCountdownText;
    private ViewGroup startGameLayout, gameLayout, gameOverLayout;
    private int screenEnd = 3000;
    private final int screenSweepTime = 900;


    public ScreenAnimator(MainActivity activity){
        this.activity = activity;
        viewModel = activity.getViewModel();
        setupViews();
    }

    private void setupViews(){
        gameStartCountdownText = activity.findViewById(R.id.gameStartCountdownText);
        startGameLayout = activity.findViewById(R.id.startGameLayoutInclude);
        gameLayout = activity.findViewById(R.id.gameLayout);
        gameOverLayout = activity.findViewById(R.id.gameOverLayoutInclude);
    }


    public void beginGameStartAnimations(Button gameStartButton){
        Animation startButtonFadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
        startButtonFadeOutAnimation.setDuration(300);
        startButtonFadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                gameStartButton.setVisibility(View.GONE);
                activity.updateGameStartCountdownText();
                viewModel.gameStartCurrentCountdown = viewModel.gameStartInitialCountdown;
                startTextAnimation(gameStartCountdownText);
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
        gameStartButton.startAnimation(startButtonFadeOutAnimation);
    }


    public void startTextAnimation(final TextView v){
        final AnimationSet animationSet = new AnimationSet(true);
        int duration = 500;
        float pivotX = 0.5f;
        float pivotY = 0.5f;
        Animation enlargeAnimation = new ScaleAnimation(1.0f,2f,1.0f,2f,
                Animation.RELATIVE_TO_SELF,pivotX,
                Animation.RELATIVE_TO_SELF,pivotY);
        enlargeAnimation.setFillAfter(true);
        enlargeAnimation.setDuration(duration);

        Animation reductionAnimation = new ScaleAnimation(1.0f,0.5f,1.0f,0.5f,
                Animation.RELATIVE_TO_SELF,pivotX,
                Animation.RELATIVE_TO_SELF,pivotY);
        reductionAnimation.setFillAfter(true);
        reductionAnimation.setDuration(duration);
        reductionAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation){ v.setVisibility(View.VISIBLE);}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(viewModel.gameStartCurrentCountdown > 1){
                    viewModel.gameStartCurrentCountdown--;
                    activity.updateGameStartCountdownText();
                    v.startAnimation(animationSet);
                }
                else{
                    gameLayout.setVisibility(View.VISIBLE);
                    fadeOutStartScreen(startGameLayout);
                }
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });

        animationSet.addAnimation(enlargeAnimation);
        animationSet.addAnimation(reductionAnimation);
        v.startAnimation(animationSet);
    }


    private void fadeOutStartScreen(final ViewGroup startScreen){

        TranslateAnimation animation = new TranslateAnimation(0, 0,0, screenEnd);
        animation.setDuration(screenSweepTime);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation) {
                activity.startGame();
                startScreen.setVisibility(View.GONE);
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });
        startScreen.startAnimation(animation);
    }


    public void fadeInGameOverScreen(){
        TranslateAnimation animation = new TranslateAnimation(0, 0,screenEnd, 0);
        animation.setDuration(screenSweepTime);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation) {
                startGameLayout.setVisibility(View.GONE);
                gameLayout.setVisibility(View.GONE);
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });
        gameOverLayout.setVisibility(View.VISIBLE);
        gameOverLayout.startAnimation(animation);
    }
}
