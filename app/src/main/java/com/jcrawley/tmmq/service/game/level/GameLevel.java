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

    private final Map<MathOperation, List<QuestionCreator>> questionCreatorsMap;
    private final int difficulty;
    private final Random random;
    private final Set<MathOperation> mathOperationsSet;
    private final List<MathOperation> mathOperations;

    public GameLevel(int difficulty){
        this.difficulty = difficulty;
        questionCreatorsMap = new HashMap<>();
        mathOperationsSet = new HashSet<>();
        mathOperations = new ArrayList<>();
        initMap();
        random = new Random(System.currentTimeMillis());
    }


    private void initMap(){
        for(MathOperation mo : MathOperation.values()){
            questionCreatorsMap.put(mo, new ArrayList<>());
        }
    }


    public QuestionCreator getRandomQuestionCreator(){
        List<QuestionCreator> list = questionCreatorsMap.get(getRandomMathOperation());

        return list == null ? getDefaultQuestionCreator() : getRandomQuestionCreatorFrom(list);
    }


    private MathOperation getRandomMathOperation(){
        return mathOperations.get(random.nextInt(mathOperations.size()));
    }


    private QuestionCreator getDefaultQuestionCreator(){
        var qc = new QuestionCreator(MathOperation.ADDITION, false);
        qc.setOperationLimits(new OperationLimits(ADDITION, 1, 10, 1, 10));
        return qc;
    }


    private QuestionCreator getRandomQuestionCreatorFrom(List<QuestionCreator> questionCreators){
        return questionCreators.get(random.nextInt(questionCreators.size()));
    }


    public void addQuestionCreator(QuestionCreator questionCreator){
        MathOperation mathOperation = questionCreator.getOperationType();
        addToList(mathOperation);
        questionCreatorsMap.computeIfAbsent(mathOperation,  x -> new ArrayList<>()).add(questionCreator);
    }


    private void addToList(MathOperation mo){
        if(!mathOperationsSet.contains(mo)){
            mathOperations.add(mo);
            mathOperationsSet.add(mo);
        }
    }


    public String getDifficultyStr(){
        return String.valueOf(difficulty);
    }


}
