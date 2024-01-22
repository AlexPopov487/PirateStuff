package org.example.inputs;

import org.example.GamePanel;
import org.example.types.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().keyPressed(e);

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().keyReleased(e);
        }
    }
}
