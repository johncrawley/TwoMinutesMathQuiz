package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.MainViewModel;
import com.jcrawley.tmmq.view.fragments.game.GameFragment;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;


public class GetReadyFragment extends Fragment {

    private TextView countdownTextView;
    public static final String FRAGMENT_TAG = "get_ready_fragment_tag";


    public GetReadyFragment() {
        // Required empty public constructor
    }


    public static GetReadyFragment newInstance(String param1, String param2) {
        return new GetReadyFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_get_ready, container, false);
        setupViews(parentView);
        return parentView;
    }


    private void setupViews(View parentView){
        countdownTextView = parentView.findViewById(R.id.gameStartCountdownText);
        MainViewModel viewModel = getMainViewModel();
        setInitialCountdownValue(viewModel);
        startTextAnimation(countdownTextView, viewModel);
    }


    private void setInitialCountdownValue(MainViewModel viewModel){
        viewModel.gameStartCurrentCountdown = viewModel.gameStartInitialCountdown;
        countdownTextView.setText(String.valueOf(viewModel.gameStartInitialCountdown));
    }


    private void startTextAnimation(final TextView v, MainViewModel viewModel){
        updateGameStartCountdownText(viewModel.gameStartCurrentCountdown);
        AnimationSet animationSet = createScaleAnimationSet(v, viewModel);
        new Handler(Looper.getMainLooper()).postDelayed(() -> v.startAnimation(animationSet), 500);
    }


    private AnimationSet createScaleAnimationSet(TextView v, MainViewModel viewModel){
        final AnimationSet animationSet = new AnimationSet(true);
        addScaleAnimationTo(animationSet, 2f,2f, null);
        addScaleAnimationTo(animationSet, 0.5f,0.5f, createListenerForCountdown(v, viewModel, animationSet));
        return animationSet;
    }


    private void addScaleAnimationTo(AnimationSet animationSet, float toX, float toY, Animation.AnimationListener animationListener){
        int duration = 800;
        float pivotX = 0.5f;
        float pivotY = 0.5f;
        Animation animation = new ScaleAnimation(1f, toX, 1f, toY,
                Animation.RELATIVE_TO_SELF, pivotX,
                Animation.RELATIVE_TO_SELF, pivotY);
        animation.setFillAfter(true);
        animation.setDuration(duration);
        animation.setAnimationListener(animationListener);
        animationSet.addAnimation(animation);
    }


    private Animation.AnimationListener createListenerForCountdown(View v, MainViewModel viewModel, AnimationSet animationSet){
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation){
                if(viewModel.gameStartCurrentCountdown > 1){
                    viewModel.gameStartCurrentCountdown--;
                    updateGameStartCountdownText(viewModel.gameStartCurrentCountdown);
                    v.startAnimation(animationSet);
                }
                else{
                    getMainActivity().playSound(Sound.GET_READY_COMPLETE);
                    setCountdownTextToGo();
                    navigateToGameFragment();
                }
            }
            @Override public void onAnimationStart(Animation animation){
                getMainActivity().playSound(Sound.GET_READY_COUNTDOWN);
                v.setVisibility(View.VISIBLE);}

            @Override public void onAnimationRepeat(Animation animation){}
        };
    }


    private void navigateToGameFragment(){
        if(getActivity() == null){
            return;
        }
        FragmentUtils.loadFragment(this, new GameFragment(), "gameScreenFragment");
    }


    private void updateGameStartCountdownText(int countdownValue){
        countdownTextView.setText(String.valueOf(countdownValue));
    }


    private void setCountdownTextToGo(){
        countdownTextView.setText(getString(R.string.start_game_go));
    }


    private MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }


    private MainViewModel getMainViewModel(){
        MainActivity mainActivity = getMainActivity();
        return mainActivity.getViewModel();
    }

}