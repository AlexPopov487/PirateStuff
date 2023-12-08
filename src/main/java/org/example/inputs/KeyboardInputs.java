package org.example.inputs;

import org.example.GamePanel;
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
                gamePanel.getGame().getPlayer().getDirection().setMovingUp(true);
            }
            case KeyEvent.VK_S -> {
                log.trace("keyPressed : S");
                gamePanel.getGame().getPlayer().getDirection().setMovingDown(true);
            }
            case KeyEvent.VK_D -> {
                log.trace("keyPressed : D");
                gamePanel.getGame().getPlayer().getDirection().setMovingRight(true);
            }
            case KeyEvent.VK_A -> {
                log.trace("keyPressed : A");
                gamePanel.getGame().getPlayer().getDirection().setMovingLeft(true);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> {
                log.trace("keyReleased : W");
                gamePanel.getGame().getPlayer().getDirection().setMovingUp(false);
            }
            case KeyEvent.VK_S -> {
                log.trace("keyReleased : S");
                gamePanel.getGame().getPlayer().getDirection().setMovingDown(false);
            }
            case KeyEvent.VK_D -> {
                log.trace("keyReleased : D");
                gamePanel.getGame().getPlayer().getDirection().setMovingRight(false);
            }
            case KeyEvent.VK_A -> {
                log.trace("keyReleased : A");
                gamePanel.getGame().getPlayer().getDirection().setMovingLeft(false);
            }
        }
    }
}
