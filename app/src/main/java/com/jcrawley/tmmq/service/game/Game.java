package com.jcrawley.tmmq.service.game;


import com.jcrawley.tmmq.service.game.timer.GameTimer;
import com.jcrawley.tmmq.service.preferences.GamePreferenceManager;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {

    private GameModel model;
    private final GameView view;
    private final GamePreferenceManager prefManager;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> notifyGameOverFuture;
    private final ScoreRecords scoreRecords;
    private GameTimer timer;

    public Game(GameView view, GamePreferenceManager gamePreferenceManager, ScoreRecords scoreRecords){
        this.view = view;
        this.prefManager = gamePreferenceManager;
        this.scoreRecords = scoreRecords;
    }


    public void submitAnswer(String answerStr){
        checkAnswer(answerStr);
    }


    public void setTimer(int value, int currentTimerIndex){
        if(prefManager != null){
            prefManager.saveTimer(value);
            prefManager.saveTimerIndex(currentTimerIndex);
        }
        setTimerLength(value);
    }


    public void notifyThatGameFinished(){
        if(notifyGameOverFuture != null && !notifyGameOverFuture.isCancelled()){
            notifyGameOverFuture.cancel(false);
        }
    }


    public void init(GameModel gameModel, GameTimer gameTimer){
        model = gameModel;
        this.timer = gameTimer;
    }


    public void setSavedValues(){
        setTimerLength(prefManager.getTimer());
        setDifficulty(prefManager.getLevel());
    }


    public void setTimerLength(int value){
        timer.setTimerLength(value);
        model.setTimerLength(value);
    }


    public void resetTimer(){
        timer.resetTime();
        timer.updateTimer();

    }

    public int getLevel(){
        return prefManager.getLevel();
    }


    public void startGame(){
        if(!model.isGameStarted()){
            model.startGame();
            timer.start();
            setQuestionTextOnView();
        }
    }


    public void quit(){
        timer.cancel();
        model.quit();
        view.resetScore();
    }


    public void setDifficulty(int difficulty){
        prefManager.saveLevel(difficulty);
        model.setDifficulty(difficulty);
    }


    public int getTimer(){
        return prefManager.getTimer();
    }


    public int getSavedTimerIndex(){
        return prefManager.getTimerIndex();
    }


    public void checkAnswer(String answerStr){
        if(model.getCurrentQuestion() == null){
            view.notifyIncorrectAnswer();
            return;
        }
        var nextQuestion = model.generateNextQuestion();
        view.setQuestion(nextQuestion);
        var isAnswerCorrect = model.process(answerStr);
        if(isAnswerCorrect){
            view.setScore(model.getScore());
        }
        else{
            view.notifyIncorrectAnswer();
        }
        model.assignCurrentQuestion();
    }


    private void setQuestionTextOnView(){
        view.setQuestion(model.getCurrentQuestion());
    }


    public void gameOver(){
        view.onGameOver(model.generateStats());
        model.gameOver();
    }


    public void onGameOver(ScoreStatistics scoreStatistics){
        var fullScoreStats = scoreRecords.getCompleteScoreStatsAndSaveRecords(scoreStatistics);
        notifyGameOverFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> view.onGameOver(fullScoreStats), 0, 2, TimeUnit.SECONDS);
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        view.updateTimer(minutesRemaining, secondsRemaining);
    }

}
