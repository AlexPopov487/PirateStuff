package ru.alexp.gameState;

import ru.alexp.Game;

public class StateBase {

    protected final Game game;

    public StateBase(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
