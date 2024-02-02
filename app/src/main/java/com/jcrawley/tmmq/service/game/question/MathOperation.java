package com.jcrawley.tmmq.service.game.question;

import java.util.function.BiFunction;

public enum MathOperation {
    ADDITION("+", Integer::sum),
    SUBTRACTION("-",(x,y)-> x - y),
    MULTIPLICATION("×", (x,y)-> x * y),
    DIVISION("÷", (x,y)-> x / y),
    POWER_OF("", (x,y) -> (int) Math.pow(x,y), true);

    private final String symbol;
    private final boolean containsExponent;
    private BiFunction<Integer, Integer, Integer> biFunction;

    MathOperation(String symbol, BiFunction<Integer, Integer, Integer> biFunction){
        this(symbol, biFunction, false);
    }


    MathOperation(String symbol,BiFunction<Integer, Integer, Integer> biFunction, boolean containsExponent){
        this.symbol = symbol;
        this.biFunction = biFunction;
        this.containsExponent = containsExponent;
    }


    public boolean containsExponent(){
        return containsExponent;
    }


    public String getSymbol(){
        return symbol;
    }


    public BiFunction<Integer, Integer, Integer> getFunction(){
        return biFunction;
    }


    public int perform(int x1, int x2){
        return biFunction.apply(x1, x2);
    }
}
