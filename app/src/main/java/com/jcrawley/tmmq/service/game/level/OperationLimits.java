package com.jcrawley.tmmq.service.game.level;


import com.jcrawley.tmmq.service.game.question.MathOperation;

public class OperationLimits {

    private MathOperation mathOperation;
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

    public OperationLimits(MathOperation mathOperation, int min1, int max1, int min2, int max2){
        this.mathOperation = mathOperation;
        this.firstNumberMin = min1;
        this.firstNumberMax = max1;
        this.secondNumberMin = min2;
        this.secondNumberMax = max2;
    }


    public OperationLimits(){
        firstNumberMin = 0;
        firstNumberMax = 0;
        secondNumberMin = 0;
        secondNumberMax = 0;
    }

    public MathOperation getMathOperation(){
        return mathOperation;
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
