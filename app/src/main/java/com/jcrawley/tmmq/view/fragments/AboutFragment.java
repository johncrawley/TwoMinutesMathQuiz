package com.jcrawley.tmmq.view.fragments;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.R;

import java.io.BufferedReader;
import java.io.IOException;
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
        var parent = inflater.inflate(R.layout.fragment_about, container, false);
        setupTitleText(parent);
        return parent;
    }


    private void setupTitleText(View parentView) {
        TextView fontInfoText = parentView.findViewById(R.id.fontText);
         if(getContext()!= null){
            try(var is = getContext()
                    .getAssets()
                    .open("font_license_info.txt", AssetManager.ACCESS_BUFFER)){

                var br = new BufferedReader(new InputStreamReader(is));
                br.lines().forEach(this::addTextLine);
                br.close();
            }catch (IOException e) {
                printError(e.getMessage());
            }
            formatFontText();
            fontInfoText.setText(fontText);
         }
    }


    private void printError(String msg){
        System.out.println("Error:  " + msg);
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