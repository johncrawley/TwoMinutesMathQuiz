package com.jcrawley.tmmq.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.getColorFromAttribute;

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
        TextPaint paint = titleText.getPaint();
        float width = paint.measureText(titleText.getText().toString());
        Context context = getContext();

        Shader textShader = new LinearGradient(0, 0, width, titleText.getTextSize(),
                new int[]{
                        getColorFromAttribute(R.attr.title_text_color_1, context),
                        getColorFromAttribute(R.attr.title_text_color_2, context),
                        getColorFromAttribute(R.attr.title_text_color_3, context),
                        getColorFromAttribute(R.attr.title_text_color_4, context),
                        getColorFromAttribute(R.attr.title_text_color_5, context)
                }, null, Shader.TileMode.CLAMP);
        titleText.getPaint().setShader(textShader);
    }

}