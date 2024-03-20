package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.loadFragmentOnBackButtonPressed;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;
import com.jcrawley.tmmq.view.fragments.utils.FragmentUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class LevelSelectFragment extends Fragment {

    public static final String FRAGMENT_TAG = "Choose_level_fragment";
    private final AtomicBoolean isLevelChosen = new AtomicBoolean(false);

    public LevelSelectFragment() {
        // Required empty public constructor
    }


    public static LevelSelectFragment newInstance(String param1, String param2) {
        LevelSelectFragment fragment = new LevelSelectFragment();
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
        View parentView =  inflater.inflate(R.layout.fragment_choose_level, container, false);
        setupLevelButtons(parentView);
        setupBackButton();
        return parentView;
    }


    public void setupLevelButtons(View parentView){
        Map<Integer,Integer> buttonsMap = Map.of(
                R.id.level1Button, 1,
                R.id.level2Button, 2,
                R.id.level3Button, 3,
                R.id.level4Button, 4,
                R.id.level5Button, 5,
                R.id.level6Button, 6,
                R.id.level7Button, 7,
                R.id.level8Button, 8,
                R.id.level9Button, 9,
                R.id.level10Button, 10);
        buttonsMap.forEach((buttonId, difficulty )-> setupLevelButton(parentView, buttonId, difficulty));
    }


    public void setupLevelButton(View parentView, int buttonId, int difficulty){
        Button button = parentView.findViewById(buttonId);
        button.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if(mainActivity == null || isLevelChosen.get()){
                return;
            }
            isLevelChosen.set(true);
            mainActivity.setDifficulty(difficulty);
            mainActivity.playSound(Sound.MENU_BUTTON);
            FragmentUtils.loadFragment(this, new GetReadyFragment(), GetReadyFragment.FRAGMENT_TAG);
        });
    }


    private void setupBackButton(){
        loadFragmentOnBackButtonPressed(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }
}