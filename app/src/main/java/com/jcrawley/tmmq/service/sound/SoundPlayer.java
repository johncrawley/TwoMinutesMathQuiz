package com.jcrawley.tmmq.service.sound;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

import androidx.preference.PreferenceManager;

import com.jcrawley.tmmq.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {

    private final SharedPreferences sharedPreferences;
    private final Context context;
    private SoundPool soundPool;
    private Map<Sound, Integer> soundMap;

    public SoundPlayer(Context context){
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
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


    private boolean areKeypadSoundsEnabled(){
        return true;
    }


    private boolean areGameSoundEffectsEnabled(){
        return true;
    }


    public void playSound(Sound sound){
        Integer soundId = soundMap.getOrDefault(sound, -1);
        if(soundId == null || soundId == -1){
            return;
        }
        playSound(soundId);
    }


    private void playSound(int soundId){
        soundPool.play(soundId, 100,100, 1, 0,1);
    }


    private void loadSounds(){
        soundMap = new HashMap<>();
        addSound(Sound.CORRECT_ANSWER, R.raw.high_tom_1);
    }


    private void addSound(Sound sound, int rawId){
        final int soundId = soundPool.load(context, rawId, 1);
        soundMap.put(sound, soundId);
    }

}
