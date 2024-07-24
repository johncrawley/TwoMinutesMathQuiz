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
        int maxTries = 15;
        int currentTries = 0;
        MathQuestion mathQuestion = generateQuestion(gameLevel);
        while(hasQuestionBeenUsedAlready(mathQuestion)){
            mathQuestion = generateQuestion(gameLevel);
            if(currentTries++ >= maxTries){
                System.out.println("clearing history!");
                existingQuestions.clear();
            }
        }
        existingQuestions.add(mathQuestion.getQuestionText());
        return mathQuestion;
    }


    private boolean hasQuestionBeenUsedAlready(MathQuestion mathQuestion){
        System.out.println("^^^ QuestionGenerator: generated question: " + mathQuestion.getQuestionText()  + " already used: " + existingQuestions.contains(mathQuestion.getQuestionText()));

        return existingQuestions.contains(mathQuestion.getQuestionText());
    }
}
