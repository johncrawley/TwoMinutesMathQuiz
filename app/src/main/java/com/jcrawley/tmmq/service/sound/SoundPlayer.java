package com.jcrawley.tmmq.service.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.preference.PreferenceManager;

import com.jcrawley.tmmq.R;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SoundPlayer {

    private final SharedPreferences sharedPreferences;
    private final Context context;
    private SoundPool soundPool;
    private Map<Sound, Integer> soundMap;
    private Map<SoundType, Supplier<Boolean>> soundEnabledMap;

    public SoundPlayer(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        setupSoundEnabledMap();
        setupSoundPool();
        loadSounds();
    }


    private void setupSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(attributes)
                .build();
    }


    private void setupSoundEnabledMap(){
        soundEnabledMap = new HashMap<>();
        soundEnabledMap.put(SoundType.KEYPAD, () -> getPref("keypad_sounds"));
        soundEnabledMap.put(SoundType.BUTTON_PRESS, () -> getPref("button_sounds"));
        soundEnabledMap.put(SoundType.GAME_EFFECT, () -> getPref("game_sounds"));
        soundEnabledMap.put(SoundType.GET_READY, () -> getPref("get_ready_sounds"));
    }


    private boolean getPref(String prefName){
        return sharedPreferences.getBoolean(prefName, false);
    }


    public void playSound(Sound sound){
        Integer soundId = soundMap.getOrDefault(sound, -1);
        if(soundId == null || soundId == -1 || isDisabled(sound)){
            return;
        }
        play(soundId);
    }


    private boolean isDisabled(Sound sound){
        Supplier<Boolean> supplier = soundEnabledMap.get(sound.getSoundType());
        return supplier == null || !supplier.get();
    }


    private void play(int soundId){
        soundPool.play(soundId, 100,100, 1, 0,1);
    }


    private void loadSounds(){
        soundMap = new HashMap<>();
        load(Sound.CORRECT_ANSWER, R.raw.fx_correct_answer);
        load(Sound.GET_READY_COUNTDOWN, R.raw.fx_get_ready_beep);
        load(Sound.GET_READY_COMPLETE, R.raw.fx_get_ready_complete);
        load(Sound.KEYPAD_BUTTON, R.raw.fx_keypad_click);
        load(Sound.INCORRECT_ANSWER, R.raw.fx_incorrect_answer);
        load(Sound.GAME_OVER, R.raw.fx_game_over);
        load(Sound.MENU_BUTTON, R.raw.high_tom_1);
        load(Sound.LOW_TIME, R.raw.fx_low_time);
    }


    private void load(Sound sound, int rawId){
        final int soundId = soundPool.load(context, rawId, 1);
        soundMap.put(sound, soundId);
    }

}
