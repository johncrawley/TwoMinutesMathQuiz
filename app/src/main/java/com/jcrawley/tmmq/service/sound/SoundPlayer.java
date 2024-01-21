package com.jcrawley.tmmq.service.sound;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SoundPlayer {

    private SharedPreferences sharedPreferences;

    public SoundPlayer(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean areKeypadSoundsEnabled(){
        return true;
    }

    private boolean areGameSoundEffectsEnabled(){
        return true;
    }



    public void playSound(){

    }


}
