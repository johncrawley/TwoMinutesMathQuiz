package com.jcrawley.tmmq.service.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class GamePreferenceManagerImpl implements GamePreferenceManager{
    public SharedPreferences sharedPreferences;
    private enum PrefKey { TIMER, TIMER_INDEX, LEVEL}


    public GamePreferenceManagerImpl(Context context){
        sharedPreferences = context.getSharedPreferences("gamePreferences", Context.MODE_PRIVATE);
    }

    public void saveTimer(int value){
        saveInt(PrefKey.TIMER, value);
    }


    public int getTimer(){
        return getInt(PrefKey.TIMER, 120);
    }


    public void saveTimerIndex(int value){
        saveInt(PrefKey.TIMER_INDEX, value);
    }


    public int getTimerIndex(){
        return getInt(PrefKey.TIMER_INDEX, 0);
    }


    public void saveLevel(int value){
        saveInt(PrefKey.LEVEL, value);
    }


    public int getLevel(){
        return getInt(PrefKey.LEVEL, 5);
    }


    private void saveInt(PrefKey key, int value){
        sharedPreferences.edit().putInt(key.toString(), value).apply();
    }


    private int getInt(PrefKey key, int defaultValue){
        return sharedPreferences.getInt(key.toString(), defaultValue);
    }

}
