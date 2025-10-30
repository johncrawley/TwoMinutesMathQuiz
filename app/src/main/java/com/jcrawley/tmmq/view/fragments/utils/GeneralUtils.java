package com.jcrawley.tmmq.view.fragments.utils;

import android.content.res.Configuration;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.game.Game;


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


    public static int incrementListIndex(int currentValue, int listSize){
        return currentValue >= listSize-1 ? 0 : currentValue + 1;
    }

    public static int decrementListIndex(int currentValue, int listSize){
        return currentValue <= 0 ? listSize - 1 : currentValue - 1;
    }


    public static Game getGame(Fragment fragment){
        var mainActivity = (MainActivity) fragment.getActivity();
        if(mainActivity == null){
            return null;
        }
        return mainActivity.getGame();
    }
}
