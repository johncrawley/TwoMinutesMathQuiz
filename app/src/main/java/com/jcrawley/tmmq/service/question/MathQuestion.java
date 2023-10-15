package com.jcrawley.tmmq.service.question;

public class MathQuestion {

    private final String questionText;
    private final int correctAnswer;

    public MathQuestion(String questionText, int correctAnswer){
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText(){
        return questionText;
    }


    public boolean isGivenAnswerCorrect(String givenAnswerStr){
        return correctAnswer == Integer.parseInt(givenAnswerStr);
    }
}
