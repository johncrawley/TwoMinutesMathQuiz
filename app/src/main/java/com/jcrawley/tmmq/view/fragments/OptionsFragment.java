package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.loadFragmentOnBackButtonPressed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class OptionsFragment extends Fragment {

    public static final String FRAGMENT_TAG = "Choose_level_fragment";
    private final AtomicBoolean isLevelChosen = new AtomicBoolean(false);
    private int currentLevel = 5;
    private final int maxLevel = 10;
    private TextView levelText, timerText;

    private int currentTimerIndex = 1;
    private final Map<String, Integer> timerMap = Map.of("1:00", 60, "2:00", 120, "3:00", 180);
    private final List<String> timerValues = List.of("1:00", "2:00", "3:00");
    private String currentTimerStr = timerValues.get(currentTimerIndex);



    public OptionsFragment() {
        // Required empty public constructor
    }


    public static OptionsFragment newInstance(String param1, String param2) {
        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        isLevelChosen.set(false);
        View parentView = inflater.inflate(R.layout.fragment_options, container, false);
        setupViews(parentView);
        setupBackButton();
        setupStartButton(parentView);
        return parentView;
    }


    private void setupBackButton() {
        loadFragmentOnBackButtonPressed(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }


    private void setupViews(View parentView){
        levelText = parentView.findViewById(R.id.setLevelText);
        timerText = parentView.findViewById(R.id.setTimerText);

        setupButton(parentView, R.id.previousLevelButton, this::decreaseCurrentLevel);
        setupButton(parentView, R.id.nextLevelButton, this::increaseCurrentLevel);
        setupButton(parentView, R.id.previousTimerValueButton, this::decreaseCurrentTimerValue);
        setupButton(parentView, R.id.nextTimerValueButton, this::increaseCurrentTimerValue);
    }


    private void decreaseCurrentTimerValue(){
        decrementTimerIndex();
        assignCurrentTimerValue();
        //getMain().ifPresent(ma -> ma.setTimerValue(currentTimerStr));
    }



    private void increaseCurrentTimerValue(){
        incrementTimerIndex();
        assignCurrentTimerValue();
    }


    private void assignCurrentTimerValue(){
        currentTimerStr = timerValues.get(currentTimerIndex);

    }


    private void decrementTimerIndex(){
        currentTimerIndex = currentTimerIndex <= 0 ? timerValues.size() - 1 : currentTimerIndex -1;
    }


    private void incrementTimerIndex(){
        currentTimerIndex = currentTimerIndex >= timerValues.size() ? 0 : currentTimerIndex + 1;
    }


    private void setupButton(View parentView, int id, Runnable onClick){
        Button button = parentView.findViewById(id);
        button.setOnClickListener(v -> onClick.run());
    }


    private void decreaseCurrentLevel(){
        playMenuButtonSound();
        decrementLevel();
        updateLevelTextView();
    }


    private void increaseCurrentLevel(){
        playMenuButtonSound();
        incrementLevel();
        updateLevelTextView();
    }


    private void decrementLevel(){
        currentLevel--;
        if(currentLevel < 1){
            currentLevel = maxLevel;
        }
    }

    private void incrementLevel(){
        currentLevel++;
        if(currentLevel > maxLevel){
            currentLevel = 1;
        }
    }


    private void updateLevelTextView(){
        levelText.setText(String.valueOf(currentLevel));
    }


    private void playMenuButtonSound(){
        getMain().ifPresent(ma-> {ma.playSound(Sound.MENU_BUTTON);});
    }



    private void setupStartButton(View parentView) {
        Button button = parentView.findViewById(R.id.startButton);
        button.setOnClickListener(v -> {
            getMain().ifPresent(ma-> {
                ma.setDifficulty(currentLevel);
                ma.playSound(Sound.MENU_BUTTON);
            });
            FragmentUtils.loadFragment(this, new GetReadyFragment(), GetReadyFragment.FRAGMENT_TAG);
        });
    }


    private Optional<MainActivity> getMain(){
        return Optional.ofNullable((MainActivity) getActivity());
    }
}