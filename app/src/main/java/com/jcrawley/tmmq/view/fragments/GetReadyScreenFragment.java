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
import com.jcrawley.tmmq.view.MainViewModel;


public class GetReadyScreenFragment extends Fragment {

    private TextView countdownTextView;
    public static final String FRAGMENT_TAG = "get_ready_fragment_tag";


    public GetReadyScreenFragment() {
        // Required empty public constructor
    }


    public static GetReadyScreenFragment newInstance(String param1, String param2) {
        return new GetReadyScreenFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_get_ready_screen, container, false);
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


    public void startTextAnimation(final TextView v, MainViewModel viewModel){
        updateGameStartCountdownText(viewModel.gameStartCurrentCountdown);
        viewModel.gameStartCurrentCountdown = viewModel.gameStartInitialCountdown;

        final AnimationSet animationSet = new AnimationSet(true);
        int duration = 800;
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

        reductionAnimation.setAnimationListener(createAnimationListenerForCountdown(v, viewModel, animationSet));
        animationSet.addAnimation(enlargeAnimation);
        animationSet.addAnimation(reductionAnimation);
        new Handler(Looper.getMainLooper()).postDelayed(() -> v.startAnimation(animationSet), 500);
    }


    private Animation.AnimationListener createAnimationListenerForCountdown(View v, MainViewModel viewModel, AnimationSet animationSet){
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation){
                if(viewModel.gameStartCurrentCountdown > 1){
                    viewModel.gameStartCurrentCountdown--;
                    updateGameStartCountdownText(viewModel.gameStartCurrentCountdown);
                    v.startAnimation(animationSet);
                }
                else{
                    navigateToGameFragment();
                }
            }
            @Override public void onAnimationStart(Animation animation){ v.setVisibility(View.VISIBLE);}

            @Override public void onAnimationRepeat(Animation animation){}
        };
    }


    private void navigateToGameFragment(){
        if(getActivity() == null){
            return;
        }
        FragmentUtils.loadFragment(this, new GameScreenFragment(), "gameScreenFragment");
    }


    private void updateGameStartCountdownText(int countdownValue){
        countdownTextView.setText(String.valueOf(countdownValue));
    }


    private MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }


    private MainViewModel getMainViewModel(){
       MainActivity mainActivity =  (MainActivity)getActivity();
       return mainActivity.getViewModel();
    }

}