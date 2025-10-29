package com.jcrawley.tmmq.service.game;

import static com.jcrawley.tmmq.utils.Utils.getTimerStrFor;

import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.level.LevelFactory;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

import java.util.Map;

public class GameModel {


    private int currentScore;
    private MathQuestion currentQuestion, nextQuestion;
    private GameTimer gametimer;
    private boolean isStarted;
    private int difficulty = 5;
    private String timerLengthDisplayStr;
    private final Map<Integer, GameLevel> levels;
    private GameLevel currentLevel;
    private final QuestionGenerator questionGenerator = new QuestionGenerator();


    public GameModel(){
        levels = LevelFactory.createLevels();
        setDifficulty(difficulty);
    }


    public void init(){
        gametimer = new GameTimer(this);
    }


    public void setTimerLength(int value){
        gametimer.setTimerLength(value);
        timerLengthDisplayStr = getTimerStrFor(value);
    }


    public void resetTimer(){
        gametimer.resetTime();
        gametimer.updateTimer();
    }


    public MathQuestion getCurrentQuestion(){
        return currentQuestion;
    }


    public MathQuestion generateNextQuestion(){
        nextQuestion = generateQuestion();
        return nextQuestion;
    }


    public void assignCurrentQuestion(){
        currentQuestion = nextQuestion;
    }


    public boolean isGameStarted(){
        return isStarted;
    }


    public void startGame(){
        if(!isStarted){
            isStarted = true;
            currentQuestion = generateQuestion();
            gametimer.startTimer();
        }
    }


    public void quit(){
        gametimer.cancel();
        isStarted = false;
        currentScore = 0;
    }


    private MathQuestion generateQuestion(){
        return questionGenerator.generateRandomQuestionFrom(currentLevel);
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
        currentLevel = levels.containsKey(difficulty) ? levels.get(difficulty) : levels.get(5);
    }


    public void checkAnswer(String answerStr){
        MathQuestion nextQuestion = generateQuestion();
        process(answerStr);
        currentQuestion = nextQuestion;
    }


    public ScoreStatistics generateStats(){
        var stats = new ScoreStatistics();
        stats.setFinalScore(currentScore);
        stats.setGameLevel(currentLevel);
        stats.setTimerLength(timerLengthDisplayStr);

        return stats;
    }


    public int getScore(){
        return currentScore;
    }


    public boolean process(String answerStr){
        if(currentQuestion.isGivenAnswerCorrect(answerStr)){
            currentScore++;
            return true;
        }
        return false;
    }


    void gameOver(){
        isStarted = false;
        currentScore = 0;
    }


}