package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.MathOperation;

import java.util.HashMap;
import java.util.Map;

public class GameLevel {

    private final Map<MathOperation, OperationLimits> operationLimitsMap;

    public GameLevel(){
        operationLimitsMap = new HashMap<>(4);
    }

    public void addOperationLimits(MathOperation mathOperation, OperationLimits operationLimits){
        operationLimitsMap.put(mathOperation, operationLimits);
    }


    public OperationLimits getOperationLimitsFor(MathOperation mathOperation){
        return operationLimitsMap.getOrDefault(mathOperation, new OperationLimits());
    }


}
