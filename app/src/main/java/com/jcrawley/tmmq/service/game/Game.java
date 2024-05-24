package com.jcrawley.tmmq.service.game;

import static com.jcrawley.tmmq.utils.Utils.getTimerStrFor;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.level.LevelFactory;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.score.ScoreStatistics;

import java.util.Map;

public class Game {

    private int currentScore;
    private MathQuestion currentQuestion;
    private GameService gameService;
    private GameTimer gametimer;
    private boolean isStarted;
    private int difficulty = 5;
    private String timerLengthDisplayStr;
    private final Map<Integer, GameLevel> levels;
    private GameLevel currentLevel;


    public Game(){
        levels = LevelFactory.createLevels();
        setDifficulty(difficulty);
    }


    public void init(GameService gameService){
        this.gameService = gameService;
        gametimer = new GameTimer(this);
    }


    public void setTimerLength(int value){
        gametimer.setTimerLength(value);
        timerLengthDisplayStr = getTimerStrFor(value);
    }


    public void startGame(){
        if(!isStarted){
            isStarted = true;
            currentQuestion = generateQuestion();
            setQuestionTextOnView();
            gametimer.startTimer();
        }
    }


    public void quit(){
        gametimer.cancel();
        isStarted = false;
        currentScore = 0;
        gameService.resetScore();
    }


    private MathQuestion generateQuestion(){
        return currentLevel.getRandomQuestionCreator().createQuestion();
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
        currentLevel = levels.containsKey(difficulty) ? levels.get(difficulty) : levels.get(5);
    }


    public void checkAnswer(String answerStr){
        if(currentQuestion == null){
            gameService.notifyIncorrectAnswer();
            return;
        }
        MathQuestion nextQuestion = generateQuestion();
        gameService.setQuestionOnView(nextQuestion);
        if(currentQuestion.isGivenAnswerCorrect(answerStr)){
            currentScore++;
            gameService.updateScore(currentScore);
        }
        else{
            gameService.notifyIncorrectAnswer();
        }
        currentQuestion = nextQuestion;
    }


    private void setQuestionTextOnView(){
        gameService.setQuestionOnView(currentQuestion);
    }


    void gameOver(){
        ScoreStatistics scoreStatistics = new ScoreStatistics();
        scoreStatistics.setFinalScore(currentScore);
        scoreStatistics.setGameLevel(currentLevel);
        scoreStatistics.setTimerLength(timerLengthDisplayStr);
        gameService.onGameOver(scoreStatistics);
        isStarted = false;
        currentScore = 0;
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        gameService.updateTimer(minutesRemaining, secondsRemaining);
    }

}
