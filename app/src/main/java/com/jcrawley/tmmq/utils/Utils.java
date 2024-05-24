package com.jcrawley.tmmq.utils;

public class Utils {

    public static String getTimerStrFor(int value){
        int A_MINUTE = 60;
        String timeDelimiter = ":";
        int minuteValue = value/A_MINUTE;
        int secondValue = value % A_MINUTE;

        String secondValuePrefix = secondValue < 10 ? "0" : "";
        return minuteValue + timeDelimiter + secondValuePrefix + secondValue;
    }
}
