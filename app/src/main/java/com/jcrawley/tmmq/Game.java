package com.jcrawley.tmmq;

public class Game {

    private int currentScore;
    private MathQuestion currentQuestion;
    private final QuestionGenerator questionGenerator;
    private final GameService gameService;
    private GameTimer gametimer;


    public Game(GameService gameService){
        questionGenerator = new QuestionGenerator();
        this.gameService = gameService;
    }

    public void init(){
        gametimer = new GameTimer(this);
    }


    void startGame(){
      currentQuestion = questionGenerator.generate();
      gameService.setQuestionText(currentQuestion.getQuestionText());
      gametimer.startTimer();
    }


    void stopGame(){

    }


    public void checkAnswer(String answerStr){
        if(currentQuestion.isGivenAnswerCorrect(answerStr)){
            currentScore++;
            gameService.updateScore(currentScore);
        }
        else{
            gameService.notifyIncorrectAnswer();
        }
        currentQuestion = questionGenerator.generate();
        gameService.setQuestionText(currentQuestion.getQuestionText());
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        gameService.updateTimer(minutesRemaining, secondsRemaining);
    }


    public int getScore(){
        return 0;
    }


    public void resetGame(){
        currentScore = 0;
        gametimer.resetTime();

    }



}
