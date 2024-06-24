package com.jcrawley.tmmq.service.game.level;

import static com.jcrawley.tmmq.service.game.question.MathOperation.ADDITION;

import com.jcrawley.tmmq.service.game.question.MathOperation;
import com.jcrawley.tmmq.service.game.question.creator.QuestionCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameLevel {

    private final int difficulty;
    private final Random random;
    private final Set<MathOperation> mathOperationsSet;
    private final List<MathOperation> mathOperations;
    private final Map<MathOperation, Map<Integer, QuestionCreator>> questionCreatorsMap2;

    public GameLevel(int difficulty){
        this.difficulty = difficulty;
        questionCreatorsMap2 = new HashMap<>();
        mathOperationsSet = new HashSet<>();
        mathOperations = new ArrayList<>();
        initMap();
        random = new Random(System.currentTimeMillis());
    }


    public QuestionCreator getRandomQuestionCreator(){
        var qcMap = questionCreatorsMap2.get(getRandomMathOperation());
        return qcMap == null ? getDefaultQuestionCreator() : getRandomQuestionCreatorFrom(qcMap);
    }


    public void addQuestionCreator(QuestionCreator questionCreator){
        MathOperation mathOperation = questionCreator.getOperationType();
        addToList(mathOperation);
        int limitsRange = questionCreator.getOperationLimits().getRange();
        var creatorMap = questionCreatorsMap2.computeIfAbsent(mathOperation, k -> new HashMap<>());
        for(int i = 0; i < limitsRange; i++){
            creatorMap.put(creatorMap.size(), questionCreator);
        }
    }


    public int getLimitsRangeFor(MathOperation mathOperation){
        var qcMap = questionCreatorsMap2.get(mathOperation);
        if(qcMap == null){
            return -1;
        }
        return qcMap.size();
    }


    public String getDifficultyStr(){
        return String.valueOf(difficulty);
    }



    private void initMap(){
        for(MathOperation mo : MathOperation.values()){
            questionCreatorsMap2.put(mo, new HashMap<>());
        }
    }


    private MathOperation getRandomMathOperation(){
        return mathOperations.get(random.nextInt(mathOperations.size()));
    }


    private QuestionCreator getDefaultQuestionCreator(){
        var qc = new QuestionCreator(MathOperation.ADDITION);
        qc.setOperationLimits(new OperationLimits(ADDITION, 1, 10, 1, 10));
        return qc;
    }


    private QuestionCreator getRandomQuestionCreatorFrom(Map<Integer, QuestionCreator> qcMap){
        int index = random.nextInt(qcMap.size());
        return qcMap.get(index);
    }


    private void addToList(MathOperation mo){
        if(!mathOperationsSet.contains(mo)){
            mathOperations.add(mo);
            mathOperationsSet.add(mo);
        }
    }


}
