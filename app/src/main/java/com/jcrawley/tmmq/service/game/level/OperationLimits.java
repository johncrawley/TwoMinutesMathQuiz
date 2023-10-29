package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.MathOperation;

public class OperationLimits {

    private final boolean isEnabled;
    private final int firstNumberMin;
    private final int firstNumberMax;
    private final int secondNumberMin;
    private final int secondNumberMax;

    public OperationLimits(int firstNumberMin, int firstNumberMax, int secondNumberMin, int secondNumberMax){
        this.isEnabled = true;
        this.firstNumberMin = firstNumberMin;
        this.firstNumberMax = firstNumberMax;
        this.secondNumberMin = secondNumberMin;
        this.secondNumberMax = secondNumberMax;
    }


    public OperationLimits(){
        this.isEnabled = false;
        firstNumberMin = 0;
        firstNumberMax = 0;
        secondNumberMin = 0;
        secondNumberMax = 0;
    }


    public boolean isEnabled() {
        return isEnabled;
    }

    public int getFirstNumberMin() {
        return firstNumberMin;
    }

    public int getFirstNumberMax() {
        return firstNumberMax;
    }

    public int getSecondNumberMin() {
        return secondNumberMin;
    }

    public int getSecondNumberMax() {
        return secondNumberMax;
    }
}
