package com.jcrawley.tmmq.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.SettingsActivity;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;

import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.addGradientTo;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;

import java.util.concurrent.atomic.AtomicBoolean;


public class MainMenuFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String FRAGMENT_TAG = "welcome_screen_fragment";

    private final AtomicBoolean isGameStartInitiated = new AtomicBoolean(false);


    public MainMenuFragment() {
        // Required empty public constructor
    }


    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGameStartInitiated.set(false);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        isGameStartInitiated.set(false);
        setupButtons(parent);
        setupTitleText(parent);
        return parent;
    }


    private void setupButtons(View parent){
        Button button = parent.findViewById(R.id.startGameButton);
        button.setOnClickListener(v -> startGame());

        Button settingsButton = parent.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });
    }


    private void startGame(){
        if(isGameStartInitiated.get()){
            return;
        }
        isGameStartInitiated.set(true);
        if(getActivity() == null){
            return;
        }
        FragmentUtils.loadFragment(this, new ChooseLevelFragment(), ChooseLevelFragment.FRAGMENT_TAG);
    }


    private void setupTitleText(View parentView){
        TextView titleText = parentView.findViewById(R.id.titleText);
        addGradientTo(titleText, getContext());
    }

}