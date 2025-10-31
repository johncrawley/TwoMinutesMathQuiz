package com.jcrawley.tmmq.service.game;


import com.jcrawley.tmmq.service.game.timer.GameTimer;
import com.jcrawley.tmmq.service.preferences.GamePreferenceManager;
import com.jcrawley.tmmq.service.score.ScoreRecords;
import com.jcrawley.tmmq.service.score.ScoreStatistics;
import com.jcrawley.tmmq.service.score.saver.ScoreSaver;


public class Game {

    private GameModel model;
    private final GameView view;
    private final GamePreferenceManager prefManager;
    private final ScoreRecords scoreRecords;
    private final ScoreSaver scoreSaver;
    private GameTimer timer;

    public Game(GameView view, GamePreferenceManager gamePreferenceManager, ScoreRecords scoreRecords, ScoreSaver saver){
        this.view = view;
        this.prefManager = gamePreferenceManager;
        this.scoreRecords = scoreRecords;
        this.scoreSaver = saver;
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


    public ScoreStatistics getScoreStatistics(){
        return model.getScoreStatistics();
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
        var stats = model.generateStats();
        var fullStats = scoreRecords.getCompleteScoreStats(stats);
        scoreSaver.saveScores(fullStats);
        model.setStats(fullStats);
        model.gameOver();
        view.loadGameOverScreen();
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        view.updateTimer(minutesRemaining, secondsRemaining);
    }

}
