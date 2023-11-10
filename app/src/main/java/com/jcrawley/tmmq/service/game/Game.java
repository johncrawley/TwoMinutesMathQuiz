package com.jcrawley.tmmq.service.game;

import com.jcrawley.tmmq.service.GameService;
import com.jcrawley.tmmq.service.game.level.GameLevel;
import com.jcrawley.tmmq.service.game.level.LevelFactory;
import com.jcrawley.tmmq.service.game.question.MathQuestion;
import com.jcrawley.tmmq.service.game.question.QuestionGenerator;

import java.util.Map;

public class Game {

    private int currentScore;
    private MathQuestion currentQuestion;
    private final QuestionGenerator questionGenerator;
    private GameService gameService;
    private GameTimer gametimer;
    private boolean isStarted;
    private int difficulty = 5;
    private int questionCount;
    private final Map<Integer, GameLevel> levels;


    public Game(){
        levels = LevelFactory.createAndAddLevels();
        questionGenerator = new QuestionGenerator();
        setDifficulty(difficulty);
    }


    public void init(GameService gameService){
        this.gameService = gameService;
        gametimer = new GameTimer(this);
    }


    public void startGame(int timerLength){
        if(isStarted){
            return;
        }
        gametimer.setTimerLength(timerLength);
        isStarted = true;
        currentQuestion = questionGenerator.generate();
        setQuestionTextOnView();
        gametimer.startTimer();
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
        GameLevel level = levels.containsKey(difficulty) ? levels.get(difficulty) : levels.get(5);
        questionGenerator.setGameLevel(level);
    }


    public boolean isStarted(){
        return isStarted;
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
        currentScore = 0;
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        gameService.updateTimer(minutesRemaining, secondsRemaining);
    }


    public int getScore(){
        return currentScore;
    }


    public String getCurrentQuestionText(){
        return currentQuestion == null ? "" : currentQuestion.getQuestionText();
    }


    public int getSecondsRemaining(){
        return gametimer == null ? 0 : gametimer.getSecondsRemaining();
    }


    public int getMinutesRemaining(){
        return gametimer == null ? 0 : gametimer.getMinutesRemaining();
    }


    public void resetGame(){
        currentScore = 0;
        gametimer.resetTime();
    }


}
