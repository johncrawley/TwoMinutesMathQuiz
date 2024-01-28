package com.jcrawley.tmmq.service.game.question;

public class MathQuestion {

    private final String questionText;
    private final int correctAnswer;
    private final boolean containsExponent;

    public MathQuestion(String questionText, int correctAnswer, boolean containsExponent){
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.containsExponent = containsExponent;
    }


    public MathQuestion(String questionText, int correctAnswer){
        this(questionText, correctAnswer, false);
    }


    public String getQuestionText(){
        return questionText;
    }


    public boolean containsExponent(){
        return containsExponent;
    }


    public boolean isGivenAnswerCorrect(String givenAnswerStr){
        return correctAnswer == Integer.parseInt(givenAnswerStr);
    }
}
