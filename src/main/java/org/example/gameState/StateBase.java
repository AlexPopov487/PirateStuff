package org.example.gameState;

import org.example.Config;
import org.example.Game;
import org.example.types.GameState;
import org.example.ui.MenuButton;

import java.awt.*;
import java.awt.event.MouseEvent;

public class StateBase {

    protected final Game game;

    public StateBase(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
