package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

import java.util.HashSet;
import java.util.Set;

public class QuestionGenerator {

    private final Set<String> existingQuestions = new HashSet<>();


    public MathQuestion generateQuestion(GameLevel gameLevel){
            return gameLevel.getRandomQuestionCreator().createQuestion();
    }


    public MathQuestion generateRandomQuestionFrom(GameLevel gameLevel){
        int maxTries = 10;
        int currentTries = 0;
        MathQuestion mathQuestion = generateQuestion(gameLevel);
        while(currentTries < maxTries){
            mathQuestion = generateQuestion(gameLevel);
            if(!existingQuestions.contains(mathQuestion.getQuestionText())){
                return mathQuestion;
            }
            currentTries ++;
            if(currentTries >= maxTries){
                existingQuestions.clear();
            }
        }
        return mathQuestion;
    }
}
