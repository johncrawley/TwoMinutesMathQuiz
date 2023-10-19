package com.jcrawley.tmmq.service.game.state;

import com.jcrawley.tmmq.service.GameService;

public abstract class AbstractGameState implements GameState{

    GameService gameService;

    public AbstractGameState(GameService gameService){
        this.gameService = gameService;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onUnload() {

    }
}
