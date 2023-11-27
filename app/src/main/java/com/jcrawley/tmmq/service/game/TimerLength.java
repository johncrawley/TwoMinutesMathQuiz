package com.jcrawley.tmmq.service.game;

public class TimerLength {

    private final int value;
    private final String displayStr;

    public TimerLength(int value, String displayStr){
        this.value = value;
        this.displayStr = displayStr;
    }


    public int getValue() {
        return value;
    }

    public String getDisplayStr() {
        return displayStr;
    }

}
