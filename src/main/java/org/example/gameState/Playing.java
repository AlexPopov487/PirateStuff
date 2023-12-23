package org.example.gameState;

import org.example.Game;
import org.example.entities.Player;
import org.example.levels.LevelManager;
import org.example.ui.PauseOverlay;
import org.example.utils.CollisionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static org.example.Game.SCALE;

public class Playing extends StateBase implements GameStateActions, Drawable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int CHARACTER_SPRITE_HEIGHT = 40;
    public static final int CHARACTER_SPRITE_WIDTH = 64;

    private final Player player;
    private final LevelManager levelManager;
    private final PauseOverlay pauseOverlay;
    private boolean isPaused;

    public Playing(Game game) {
        super(game);
        pauseOverlay = new PauseOverlay(this);
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (CHARACTER_SPRITE_WIDTH * SCALE), (int) (CHARACTER_SPRITE_HEIGHT * SCALE));
        int[][] currentLevelData = levelManager.getCurrentLevel().getLevelData();
        player.setCurrentLevelData(currentLevelData);
        setInAirIfPlayerNotOnFloor(player, currentLevelData);
    }

    public Player getPlayer() {
        return player;
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    @Override
    public void update() {
        if (!isPaused) {
            levelManager.update();
            player.update();
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void render(Graphics g) {
        levelManager.render(g);
        player.render(g);

        if (isPaused) {
        pauseOverlay.render(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.getActions().setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> {
                log.trace("keyPressed : D");
                player.getDirection().setMovingRight(true);
            }
            case KeyEvent.VK_A -> {
                log.trace("keyPressed : A");
                player.getDirection().setMovingLeft(true);
            }
            case KeyEvent.VK_SPACE -> {
                log.trace("keyPressed : SPACE");
                player.getDirection().setJumping(true);
            }
            case KeyEvent.VK_ESCAPE -> {
                log.trace("keyPressed : ESCAPE");
                switchPaused();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D -> {
                log.trace("keyReleased : D");
                player.getDirection().setMovingRight(false);
            }
            case KeyEvent.VK_A -> {
                log.trace("keyReleased : A");
                player.getDirection().setMovingLeft(false);
            }
            case KeyEvent.VK_SPACE -> {
                log.trace("keyReleased : SPACE");
                player.getDirection().setJumping(false);
            }
        }
    }

    private void switchPaused() {
        isPaused = !isPaused;
    }

    private void setInAirIfPlayerNotOnFloor(Player player, int[][] currentLevelData) {
        if (!CollisionHelper.isOnTheFloor(player.getHitBox(), currentLevelData)) {
            player.getDirection().setInAir(true);
        }
    }
}
