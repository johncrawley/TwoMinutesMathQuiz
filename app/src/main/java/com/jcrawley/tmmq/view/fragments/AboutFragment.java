package com.jcrawley.tmmq.view.fragments;

import static com.jcrawley.tmmq.view.fragments.utils.ActivityUtils.playSound;
import static com.jcrawley.tmmq.view.fragments.utils.ColorUtils.addGradientTo;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.loadFragment;
import static com.jcrawley.tmmq.view.fragments.utils.GeneralUtils.isInLandscapeMode;
import static com.jcrawley.tmmq.view.fragments.utils.GeneralUtils.setTextForLandscape;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.service.sound.Sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class AboutFragment extends Fragment {

    public static final String FRAGMENT_TAG = "about_fragment";
    private String fontText = "";

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_about, container, false);

       // setupButtons(parent);
        setupTitleText(parent);
        return parent;
    }


    private void setupButtons(View parent) {
        setupButton(parent, R.id.startGameButton, this::goToMainMenu);
    }


    private void setupButton(View parent, int buttonId, Runnable onClick) {
        Button button = parent.findViewById(buttonId);
        button.setOnClickListener(v -> {
            playSound(this, Sound.MENU_BUTTON);
            onClick.run();
        });
    }


    private void goToMainMenu() {
        loadFragment(this, new MainMenuFragment(), MainMenuFragment.FRAGMENT_TAG);
    }


    private void setupTitleText(View parentView) {
        TextView fontInfoText = parentView.findViewById(R.id.fontText);
         if(getContext()!= null){
                try(InputStream is = getContext().getAssets().open("font_license_info.txt", AssetManager.ACCESS_BUFFER)){
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    br.lines().forEach(this::addTextLine);
                }catch (IOException e) {
                    e.printStackTrace();
                }
                formatFontText();
                fontInfoText.setText(fontText);
            }
    }


    private void addTextLine(String line){
        fontText += line;
        fontText += "\n";
    }


    private void formatFontText(){
        fontText = fontText.replaceAll("\n", " ");
        fontText = fontText.replaceAll(" {2}", "\n\n");
        fontText = fontText.replace("--- ", "---\n");
        fontText = fontText.replace(" ---", " \n---");
    }
}