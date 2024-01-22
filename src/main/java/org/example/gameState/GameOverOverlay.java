package org.example.gameState;

import org.example.GamePanel;
import org.example.types.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GameOverOverlay implements Drawable, GameStateActions {

    private final Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
    }

    @Override
    public void render(Graphics graphics) {
        graphics.setColor(new Color(0,0,0, 65));
        graphics.fillRect(0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight());

        graphics.setColor(Color.WHITE);
        graphics.drawString("GAME OVER! Press Esc to enter main menu", GamePanel.getWindowWidth() / 2, GamePanel.getWindowHeight() / 2);

    }

    @Override
    public void update() {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
            playing.resetPlaying();
            GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
