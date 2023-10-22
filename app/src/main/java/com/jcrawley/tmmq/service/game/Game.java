package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.game.question.QuestionGenerator;

public class Game {

    private int currentScore;
    private MathQuestion currentQuestion;
    private final QuestionGenerator questionGenerator;
    private final GameService gameService;
    private GameTimer gametimer;
    private boolean isStarted;
    private int questionCount;


    public Game(GameService gameService){
        questionGenerator = new QuestionGenerator();
        this.gameService = gameService;
    }

    public void init(){
        gametimer = new GameTimer(this);
    }


    public void startGame(){
        if(isStarted){
            return;
        }
        isStarted = true;
        currentQuestion = questionGenerator.generate();
        setQuestionTextOnView();
        gametimer.startTimer();
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
        gameService.fadeInQuestionTextOnView(currentQuestion.getQuestionText());
    }


    private void setQuestionTextOnView(){
        gameService.setQuestionTextOnView(currentQuestion.getQuestionText());
    }


    void gameOver(){
        gameService.onGameOver(currentScore);
        isStarted = false;
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
