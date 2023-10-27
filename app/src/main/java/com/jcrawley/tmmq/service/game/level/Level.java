package com.jcrawley.tmmq.service.game.level;

public class Level {
    private int firstNumberMin, firstNumberMax, secondNumberMin, secondNumberMax;
    private boolean isAdditionEnabled, isDivisionEnabled, isSubtractionEnabled, isMultiplicationEnabled;


    public void setFirstNumberLimits(int firstNumberMin, int firstNumberMax){
        this.firstNumberMin = firstNumberMin;
        this.firstNumberMax = firstNumberMax;
    }


    public void setSecondNumberLimits(int secondNumberMin, int secondNumberMax){
        this.secondNumberMin = secondNumberMin;
        this.secondNumberMax = secondNumberMax;
    }


    public void enableAddition(){
        isAdditionEnabled = true;
    }


    public void enableSubtraction(){
        isSubtractionEnabled = true;
    }


    public void enableMultiplication(){
        isMultiplicationEnabled = true;
    }


    public void enableDivision() {
        isDivisionEnabled = true;
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


    public boolean isAdditionEnabled() {
        return isAdditionEnabled;
    }


    public boolean isDivisionEnabled() {
        return isDivisionEnabled;
    }


    public boolean isSubtractionEnabled() {
        return isSubtractionEnabled;
    }


    public boolean isMultiplicationEnabled() {
        return isMultiplicationEnabled;
    }


}
