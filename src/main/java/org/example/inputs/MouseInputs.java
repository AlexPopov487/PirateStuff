package org.example.inputs;

import org.example.GamePanel;
import org.example.types.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
    private final GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.getState()) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(e);
            case MENU -> gamePanel.getGame().getMenu().mouseClicked(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().mouseClicked(e);
            case OPTIONS -> gamePanel.getGame().getSettings().mouseClicked(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.getState()) {
            case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
            case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().mousePressed(e);
            case OPTIONS -> gamePanel.getGame().getSettings().mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.getState()) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().mouseReleased(e);
            case OPTIONS -> gamePanel.getGame().getSettings().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.getState()) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
            case MENU -> gamePanel.getGame().getMenu().mouseDragged(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().mouseDragged(e);
            case OPTIONS -> gamePanel.getGame().getSettings().mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.getState()) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
            case GAME_OVER -> gamePanel.getGame().getGameOverOverlay().mouseMoved(e);
            case OPTIONS -> gamePanel.getGame().getSettings().mouseMoved(e);
        }
    }
}
