package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcrawley.tmmq.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeScreenFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String FRAGMENT_TAG = "welcome_screen_fragment";

    private final AtomicBoolean isGameStartInitiated = new AtomicBoolean(false);


    public WelcomeScreenFragment() {
        // Required empty public constructor
    }


    public static WelcomeScreenFragment newInstance(String param1, String param2) {
        WelcomeScreenFragment fragment = new WelcomeScreenFragment();
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


    private void setupButtons(View parent){
        Button button = parent.findViewById(R.id.startGameButton);
        button.setOnClickListener(v -> startGame());
    }


    private void startGame(){
        if(isGameStartInitiated.get()){
            return;
        }
        isGameStartInitiated.set(true);
        if(getActivity() == null){
            return;
        }
        GetReadyScreenFragment getReadyScreenFragment= new GetReadyScreenFragment();
        FragmentUtils.loadFragment(this, getReadyScreenFragment, GetReadyScreenFragment.FRAGMENT_TAG);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View parent = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        setupButtons(parent);
        return parent;
    }
}