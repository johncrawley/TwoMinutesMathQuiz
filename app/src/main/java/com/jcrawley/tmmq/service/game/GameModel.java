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
    private boolean isStarted;
    private int difficulty = 5;
    private String timerLengthDisplayStr;
    private final Map<Integer, GameLevel> levels;
    private GameLevel currentLevel;
    private final QuestionGenerator questionGenerator = new QuestionGenerator();
    private ScoreStatistics scoreStatistics;

    public GameModel(){
        levels = LevelFactory.createLevels();
        setDifficulty(difficulty);
    }


    public void setTimerLength(int value){
        timerLengthDisplayStr = getTimerStrFor(value);
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
        }
    }


    public void quit(){
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
        var nextQuestion = generateQuestion();
        process(answerStr);
        currentQuestion = nextQuestion;
    }


    public ScoreStatistics generateStats(){
        var stats = new ScoreStatistics();
        log("generateStats() currentScore: " + currentScore + " level: " + currentLevel + " timerLength: " + timerLengthDisplayStr);
        stats.setFinalScore(currentScore);
        stats.setGameLevel(currentLevel);
        stats.setTimerLength(timerLengthDisplayStr);
        this.scoreStatistics = stats;
        return stats;
    }


    public void setStats(ScoreStatistics stats){
        this.scoreStatistics = stats;
    }


    public ScoreStatistics getScoreStatistics(){
        return scoreStatistics;
    }


    private void log(String msg){
        System.out.println("^^^ GameModel: " + msg);
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