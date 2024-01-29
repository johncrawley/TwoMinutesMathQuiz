package com.jcrawley.tmmq.service.game.question;

import androidx.annotation.NonNull;

public class MathQuestion {

    private final String questionText;
    private final int correctAnswer;
    private final boolean containsExponent;

    public MathQuestion(String questionText, int correctAnswer, boolean containsExponent){
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.containsExponent = containsExponent;
    }


    public String getQuestionText(){
        return questionText;
    }

    public int getCorrectAnswer(){
        return correctAnswer;
    }

    @NonNull
    @Override
    public String toString(){
        return questionText + " = " + correctAnswer;
    }


    public boolean containsExponent(){
        return containsExponent;
    }


    public boolean isGivenAnswerCorrect(String givenAnswerStr){
        return correctAnswer == Integer.parseInt(givenAnswerStr);
    }
}
