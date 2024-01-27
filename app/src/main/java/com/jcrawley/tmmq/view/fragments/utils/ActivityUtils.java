package com.jcrawley.tmmq.view.fragments.utils;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.MainActivity;
import com.jcrawley.tmmq.service.sound.Sound;

public class ActivityUtils {
    public static void playSound(Fragment fragment, Sound sound){
        MainActivity mainActivity = (MainActivity) fragment.getActivity();
        if(mainActivity != null){
            mainActivity.playSound(sound);
        }
    }

}
