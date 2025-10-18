package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.question.MathQuestion;

import java.util.HashSet;
import java.util.Set;

public class QuestionGenerator {

    private final Set<String> existingQuestions = new HashSet<>();


    public MathQuestion generateRandomQuestionFrom(GameLevel gameLevel){
        int maxTries = 12;
        int currentTries = 0;
        var mathQuestion = generateQuestion(gameLevel);
        while(wasAlreadyAsked(mathQuestion)){
            mathQuestion = generateQuestion(gameLevel);
            if(++currentTries >= maxTries){
                existingQuestions.clear();
            }
        }
        existingQuestions.add(mathQuestion.getQuestionText());
        return mathQuestion;
    }


    public MathQuestion generateQuestion(GameLevel gameLevel){
        return gameLevel.getRandomQuestionCreator().createQuestion();
    }


    private boolean wasAlreadyAsked(MathQuestion mathQuestion){
        return existingQuestions.contains(mathQuestion.getQuestionText());
    }
}
