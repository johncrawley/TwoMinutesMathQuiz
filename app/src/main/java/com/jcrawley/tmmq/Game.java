package com.jcrawley.tmmq;

public class Game {

    private final int initialRemainingTime = 120;
    private int currentRemainingTime = initialRemainingTime;
    private int currentScore;
    private MathQuestion currentQuestion;

    void startGame(){

    }

    void stopGame(){

    }


    public void checkAnswer(int answer){

    }


    public int getScore(){
        return 0;
    }


    public void resetGame(){
        currentScore = 0;
        currentRemainingTime = initialRemainingTime;
    }


    private void decrementRemainingTime(){
        currentRemainingTime--;
        if(currentRemainingTime <= 0){

        }
    }

}
