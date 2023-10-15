package com.jcrawley.tmmq.service.question;

import java.util.function.BiFunction;

public class MathOperation {

    private final String symbol;
    private final BiFunction<Integer, Integer, Integer> operation;

    public MathOperation(String symbol, BiFunction<Integer, Integer, Integer> operation){
        this.symbol = symbol;
        this.operation = operation;
    }

    public int performOperation(int part1, int part2){
        return operation.apply(part1, part2);
    }

    public String getSymbol(){
        return symbol;
    }
}
