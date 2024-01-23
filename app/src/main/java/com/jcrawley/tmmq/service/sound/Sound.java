package com.jcrawley.tmmq.service.sound;

import com.jcrawley.tmmq.R;

public enum Sound {

    KEYPAD_BUTTON(SoundType.KEYPAD),
    MENU_BUTTON(SoundType.BUTTON_PRESS),
    GET_READY_COUNTDOWN(SoundType.GET_READY),
    GET_READY_COMPLETE(SoundType.GET_READY),
    CORRECT_ANSWER(SoundType.GAME_EFFECT),
    INCORRECT_ANSWER(SoundType.GAME_EFFECT),
    GAME_OVER(SoundType.GAME_EFFECT),
    LOW_TIME(SoundType.GAME_EFFECT);


    private final SoundType soundType;

    Sound(SoundType soundType){
        this.soundType = soundType;
    }

    public SoundType getSoundType(){
        return soundType;
    }
}
