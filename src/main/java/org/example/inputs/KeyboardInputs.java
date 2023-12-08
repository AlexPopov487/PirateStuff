package org.example.inputs;

import org.example.GamePanel;
import org.example.utils.Direction;
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
        switch (e.getKeyCode()){
            case KeyEvent.VK_W -> {
                log.trace("keyPressed : W");
                gamePanel.setPlayerDirection(Direction.UP);
            }
            case KeyEvent.VK_S -> {
                log.trace("keyPressed : S");
                gamePanel.setPlayerDirection(Direction.DOWN);
            }
            case KeyEvent.VK_D -> {
                log.trace("keyPressed : D");
                gamePanel.setPlayerDirection(Direction.RIGHT);
            }
            case KeyEvent.VK_A -> {
                log.trace("keyPressed : A");
                gamePanel.setPlayerDirection(Direction.LEFT);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S -> {
                log.trace("keyPressed : W");
                gamePanel.setPlayerDirection(Direction.STILL);
            }
        }
    }
}
