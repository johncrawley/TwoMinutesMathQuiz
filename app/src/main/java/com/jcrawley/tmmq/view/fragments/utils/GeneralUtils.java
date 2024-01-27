package com.jcrawley.tmmq.view.fragments.utils;

import android.content.res.Configuration;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class GeneralUtils {


    public static void setTextForLandscape(Fragment fragment, int textId, TextView... textViews){
        if (isInLandscapeMode(fragment)) {
            String text = fragment.getString(textId);
            for(TextView textView : textViews){
                textView.setMaxLines(1);
                textView.setText(text);
            }
        }
    }

    public static boolean isInLandscapeMode(Fragment fragment){
        return fragment.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
