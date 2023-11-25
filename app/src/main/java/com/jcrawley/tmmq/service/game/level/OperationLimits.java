package com.jcrawley.tmmq.service.game.level;


public class OperationLimits {


    private final int firstNumberMin;
    private final int firstNumberMax;
    private final int secondNumberMin;
    private final int secondNumberMax;

    public OperationLimits(int firstNumberMin, int firstNumberMax, int secondNumberMin, int secondNumberMax){
        this.firstNumberMin = firstNumberMin;
        this.firstNumberMax = firstNumberMax;
        this.secondNumberMin = secondNumberMin;
        this.secondNumberMax = secondNumberMax;
    }


    public OperationLimits(){
        firstNumberMin = 0;
        firstNumberMax = 0;
        secondNumberMin = 0;
        secondNumberMax = 0;
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
