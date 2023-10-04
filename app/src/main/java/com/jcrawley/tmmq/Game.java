package com.jcrawley.tmmq;

public class Game {

    private final int initialRemainingTime = 120;
    private int currentRemainingTime = initialRemainingTime;
    private int currentScore;
    private MathQuestion currentQuestion;
    private QuestionGenerator questionGenerator;
    private final GameService gameService;


    public Game(GameService gameService){
        questionGenerator = new QuestionGenerator();
        this.gameService = gameService;
    }


    void startGame(){
      currentQuestion = questionGenerator.generate();
      gameService.displayQuestionOnView(currentQuestion.getQuestionText());
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
