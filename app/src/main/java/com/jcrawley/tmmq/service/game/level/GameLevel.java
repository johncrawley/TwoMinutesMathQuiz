package com.jcrawley.tmmq.service.game.level;

import com.jcrawley.tmmq.service.game.question.MathOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameLevel {

    private final Map<MathOperation, OperationLimits> operationLimitsMap;
    private Set<MathOperation> mathOperations;

    public GameLevel(){
        operationLimitsMap = new HashMap<>(4);
        mathOperations = new HashSet<>();
    }

    public void addOperationLimits(MathOperation mathOperation, OperationLimits operationLimits){
        operationLimitsMap.put(mathOperation, operationLimits);
        mathOperations.add(mathOperation);
    }

    public boolean containsMathOperation(MathOperation mathOperation){
        return mathOperations.contains(mathOperation);
    }

    public OperationLimits getOperationLimitsFor(MathOperation mathOperation){
        return operationLimitsMap.getOrDefault(mathOperation, new OperationLimits());
    }


}
