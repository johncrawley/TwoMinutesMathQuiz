package com.jcrawley.tmmq.service.game.level;


import com.jcrawley.tmmq.service.game.question.MathOperation;

public class OperationLimits {

    private final MathOperation mathOperation;
    private final int firstNumberMin;
    private final int firstNumberMax;
    private final int secondNumberMin;
    private final int secondNumberMax;

    public OperationLimits(MathOperation mathOperation, int min1, int max1, int min2, int max2){
        this.mathOperation = mathOperation;
        this.firstNumberMin = min1;
        this.firstNumberMax = max1;
        this.secondNumberMin = min2;
        this.secondNumberMax = max2;
        validateLimits();
    }


    private void validateLimits(){
        if(firstNumberMin > firstNumberMax){
            throw new RuntimeException("OperationLimits number 1 minimum ("  + ") should be less than maximum (" + firstNumberMax + ").");
        }
        if(secondNumberMin > secondNumberMax){
            throw new RuntimeException("OperationLimits number 2 minimum ("  + ") should be less than maximum (" + firstNumberMax + ").");
        }
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
