package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.utils.Utils.getTimerStrFor;
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
import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;
import com.jcrawley.tmmq.view.fragments.utils.GeneralUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class OptionsFragment extends Fragment {

    public static final String FRAGMENT_TAG = "Options_fragment";
    private int currentLevel = 5;
    private final int maxLevel = 10;
    private TextView levelText, timerText;

    private int currentTimerIndex = 1;
    private Map<String, Integer> timerMap;
    private List<Integer> timerValues;
    private String currentTimerStr;
    private int currentTimerValue;
    public enum Message { NOTIFY_OF_SERVICE_CONNECTED }



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
        View parentView = inflater.inflate(R.layout.fragment_options, container, false);
        setupListeners();
        setupViews(parentView);
        setupTimerValues();
        assignValuesToFields();
        setupBackButton();
        setupStartButton(parentView);
        return parentView;
    }


    private void setupListeners(){
        FragmentUtils.setListener(this, Message.NOTIFY_OF_SERVICE_CONNECTED.toString(), b -> assignValuesToFields());
    }


    private void assignValuesToFields(){
        getGameService().ifPresent(gs -> {
            assignTimerFromService();
            assignLevelFromService();
        });
    }


    private void setupTimerValues(){
        timerValues = parseTimerValuesFromStringResource();
        timerMap = new HashMap<>();
        for(Integer timerValue : timerValues){
            timerMap.put(getTimerStrFor(timerValue), timerValue);
        }
        currentTimerValue = timerValues.get(currentTimerIndex);
        currentTimerStr = getTimerStrFor(currentTimerValue);
    }


    private List<Integer> parseTimerValuesFromStringResource(){
        String[] timerValuesArray = getString(R.string.timer_values).split(",");
        return Arrays.stream(timerValuesArray).map(Integer::valueOf).collect(Collectors.toList());
    }


    private void setupBackButton() {
        loadFragmentOnBackButtonPressed(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }


    private void setupViews(View parentView){
        setupLevelOptions(parentView);
        setupTimeOptions(parentView);
    }


    private void setupLevelOptions(View parentView){
        ViewGroup vg = parentView.findViewById(R.id.choose_level_include);
        setupOptions(vg, this::decreaseCurrentLevel, this::increaseCurrentLevel);
        levelText = getOptionTextFor(vg);
        setupHeading(vg, R.string.choose_level_heading);
    }


    private void setupTimeOptions(View parentView){
        ViewGroup vg = parentView.findViewById(R.id.choose_time_include);
        setupOptions(vg, this::decreaseCurrentTimerValue, this::increaseCurrentTimerValue);
        timerText = getOptionTextFor(vg);
        timerText.setText(currentTimerStr);
        setupHeading(vg, R.string.choose_time_heading);
    }


    private void setupHeading(ViewGroup vg, int strId){
        String heading = getString(strId);
        TextView headingTextView = vg.findViewById(R.id.optionHeading);
        headingTextView.setText(heading);
    }


    private TextView getOptionTextFor(ViewGroup vg){
        return vg.findViewById(R.id.optionText);
    }


    private void setupOptions(ViewGroup vg, Runnable prevRunnable, Runnable nextRunnable){
        setupButton(vg, R.id.optionPreviousButton, prevRunnable);
        setupButton(vg, R.id.optionNextButton, nextRunnable);
    }


    private void decreaseCurrentTimerValue(){
        playMenuButtonSound();
        decrementTimerIndex();
        assignCurrentTimerValue();
        setTimerOnService();
    }


    private void increaseCurrentTimerValue(){
        playMenuButtonSound();
        incrementTimerIndex();
        assignCurrentTimerValue();
        setTimerOnService();
    }


    private void assignCurrentTimerValue(){
        currentTimerValue = timerValues.get(currentTimerIndex);
        currentTimerStr = getTimerStrFor(currentTimerValue);
        Integer value = timerMap.get(currentTimerStr);
        if(value != null){
            currentTimerValue = value;
            timerText.setText(currentTimerStr);
        }
    }


    private void decrementTimerIndex(){
        currentTimerIndex = GeneralUtils.decrementListIndex(currentTimerIndex, timerValues.size());
    }


    private void incrementTimerIndex(){
        currentTimerIndex = GeneralUtils.incrementListIndex(currentTimerIndex, timerValues.size());
    }


    private void setupButton(View parentView, int id, Runnable onClick){
        Button button = parentView.findViewById(id);
        button.setOnClickListener(v -> onClick.run());
    }


    private void decreaseCurrentLevel(){
        playMenuButtonSound();
        decrementLevel();
        updateLevelTextView();
        setLevelOnService();
    }


    private void increaseCurrentLevel(){
        playMenuButtonSound();
        incrementLevel();
        updateLevelTextView();
        setLevelOnService();
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


    private void assignTimerFromService(){
        getGameService().ifPresent(gs -> {
            int savedValue = gs.getTimer();
            currentTimerIndex = gs.getSavedTimerIndex();
            currentTimerStr = getTimerStrFor(savedValue);
            currentTimerValue = savedValue;
            assignCurrentTimerValue();
        });
    }


    private void assignLevelFromService(){
        getGameService().ifPresent(gs -> {
            currentLevel = gs.getLevel();
            updateLevelTextView();
        });
    }


    private void updateLevelTextView(){
        levelText.setText(String.valueOf(currentLevel));
    }


    private void playMenuButtonSound(){
        getGameService().ifPresent(gs-> gs.playSound(Sound.MENU_BUTTON));
    }


    private void setupStartButton(View parentView) {
        Button button = parentView.findViewById(R.id.startButton);
        button.setOnClickListener(v -> {
            playMenuButtonSound();
            getGameService().ifPresent(gs ->{
                gs.setLevel(currentLevel);
                gs.setTimer(currentTimerValue, currentTimerIndex);
            });
            FragmentUtils.loadFragment(this, new GetReadyFragment(), GetReadyFragment.FRAGMENT_TAG);
        });
    }


    private void setTimerOnService(){
        getGameService().ifPresent(gs -> gs.setTimer(currentTimerValue, currentTimerIndex));
    }


    private void setLevelOnService(){
        getGameService().ifPresent(gs -> gs.setLevel(currentLevel));
    }


    private Optional<GameService> getGameService(){
        MainActivity mainActivity = (MainActivity) getActivity();
        return mainActivity == null ? Optional.empty() : mainActivity.getGameService();
    }
}