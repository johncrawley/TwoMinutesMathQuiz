package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GetReadyScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetReadyScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView countdownTextView;

    public GetReadyScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetReadyScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GetReadyScreenFragment newInstance(String param1, String param2) {
        GetReadyScreenFragment fragment = new GetReadyScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parentView = inflater.inflate(R.layout.fragment_get_ready_screen, container, false);
        setupViews(parentView);
        return parentView;
    }


    private void setupViews(View parentView){
        countdownTextView = parentView.findViewById(R.id.gameStartCountdownText);
        startTextAnimation(countdownTextView);
    }


    public void startTextAnimation(final TextView v){
        MainViewModel viewModel = getMainViewModel();
        updateGameStartCountdownText(viewModel);
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
        reductionAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation){ v.setVisibility(View.VISIBLE);}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(viewModel.gameStartCurrentCountdown > 1){
                    viewModel.gameStartCurrentCountdown--;
                    updateGameStartCountdownText(viewModel);
                    v.startAnimation(animationSet);
                }
                else{
                    navigateToGameFragment();
                }
            }
            @Override public void onAnimationRepeat(Animation animation){}
        });

        animationSet.addAnimation(enlargeAnimation);
        animationSet.addAnimation(reductionAnimation);
        v.startAnimation(animationSet);
    }


    private void navigateToGameFragment(){
        if(getActivity() == null){
            return;
        }
        GameScreenFragment gameScreenFragment = new GameScreenFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, gameScreenFragment, "gameScreenFragment")
                .addToBackStack(null)
                .commit();
    }


    private void updateGameStartCountdownText(MainViewModel viewModel){

        countdownTextView.setText(String.valueOf(viewModel.gameStartCurrentCountdown));
    }


    private MainActivity getMainActivity(){
        return (MainActivity) getActivity();
    }


    private MainViewModel getMainViewModel(){
       MainActivity mainActivity =  (MainActivity)getActivity();
       return mainActivity.getViewModel();
    }

}