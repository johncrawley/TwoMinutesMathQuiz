package com.jcrawley.tmmq.service.game;


public class Game {

    private final GameModel model;
    private final GameView view;


    public Game(GameView view){
        this.view = view;
        model = new GameModel();
    }


    public void submitAnswer(String answerStr){

    }


    public void notifyThatGameFinished(){

    }


    public void init(){
        model.init();
    }


    public void setTimerLength(int value){
        model.setTimerLength(value);
    }


    public void resetTimer(){
        model.resetTimer();
    }


    public void startGame(){
        if(!model.isGameStarted()){
            model.startGame();
            setQuestionTextOnView();
        }
    }


    public void quit(){
        model.quit();
        view.resetScore();
    }


    public void setDifficulty(int difficulty){
        model.setDifficulty(difficulty);
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


    void gameOver(){
        view.gameOver(model.generateStats());
        model.gameOver();
    }


    public void updateTime(int minutesRemaining, int secondsRemaining){
        view.updateTimer(minutesRemaining, secondsRemaining);
    }

}
