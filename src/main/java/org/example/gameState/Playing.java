package org.example.gameState;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.entities.EnemyManager;
import org.example.entities.Player;
import org.example.levels.LevelManager;
import org.example.ui.PauseOverlay;
import org.example.utils.AtlasType;
import org.example.utils.CollisionHelper;
import org.example.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static org.example.Game.SCALE;
import static org.example.gameState.GameState.GAME_OVER;

public class Playing extends StateBase implements GameStateActions, Drawable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int CHARACTER_SPRITE_HEIGHT = 40;
    public static final int CHARACTER_SPRITE_WIDTH = 64;

    private final Player player;
    private final LevelManager levelManager;
    private final EnemyManager enemyManager;
    private final PauseOverlay pauseOverlay;
    private boolean isPaused;
    private final BufferedImage background;
    private final BufferedImage backgroundCloudBig;
    private final BufferedImage backgroundCloudSmall;
    private int[] smallCloudYPositions;


    // exceeding the threshold by x% of the windowWidth means that we need to move the level animation to the left
    private final int leftThreshold = (int) (0.4 * GamePanel.getWindowWidth());
    private final int rightThreshold = (int) (0.6 * GamePanel.getWindowWidth());
    private int xLevelOffset;
    private final int levelTilesCount = ResourceLoader.getLevelData()[0].length;
    // represents how many tiles of the level remain unseen (i.e. for how many tiles it is possible to move the level to the left)
    private final int maxLevelTilesOffset = levelTilesCount - Game.TILE_VISIBLE_COUNT_WIDTH;
    // shows for how many tiles it is possible to move the level to the left in pixels
    private final int maxLevelOffsetX = maxLevelTilesOffset * GamePanel.getCurrentTileSize();


    public Playing(Game game) {
        super(game);
        background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND);
        backgroundCloudBig = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND_CLOUD_BIG);
        backgroundCloudSmall = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND_CLOUD_SMALL);
        generateSmallCloudPositions();

        pauseOverlay = new PauseOverlay(this);
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (CHARACTER_SPRITE_WIDTH * SCALE), (int) (CHARACTER_SPRITE_HEIGHT * SCALE), this);
        int[][] currentLevelData = levelManager.getCurrentLevel().getLevelData();
        player.setCurrentLevelData(currentLevelData);
        player.setInAirIfPlayerNotOnFloor(player, currentLevelData);
        enemyManager = new EnemyManager(this);
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
            checkPlayerAlive();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData());
            checkPlayerCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight(), null);
        renderClouds(g);

        levelManager.render(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.render(g, xLevelOffset);

        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight());
            pauseOverlay.render(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            setPlayerAttack();
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
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT-> {
                log.trace("keyPressed : D");
                player.getDirection().setMovingRight(true);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT-> {
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
            case KeyEvent.VK_SHIFT -> {
                log.trace("keyPressed : SHIFT");
                setPlayerAttack();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT-> {
                log.trace("keyReleased : D");
                player.getDirection().setMovingRight(false);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT-> {
                log.trace("keyReleased : A");
                player.getDirection().setMovingLeft(false);
            }
            case KeyEvent.VK_SPACE -> {
                log.trace("keyReleased : SPACE");
                player.getDirection().setJumping(false);
            }
        }
    }

    public void resetPlaying() {
        isPaused = false;
        player.reset();
        enemyManager.resetAll();
    }
    public void checkEnemyHit(Rectangle2D.Float playerAttackRange) {
        enemyManager.checkEnemyGotHit(playerAttackRange);
    }
    private void setPlayerAttack() {
        player.getActions().setAttacking(true);
    }

    private void checkPlayerAlive() {
        if (player.getHeath().isDead()) setGameOver();
    }

    private void setGameOver(){
        GameState.state = GAME_OVER;
    }

    private void switchPaused() {
        isPaused = !isPaused;
    }

    private void checkPlayerCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLevelOffset;

        if (diff > rightThreshold) {
            xLevelOffset += diff - rightThreshold;
        } else if (diff < leftThreshold) {
            xLevelOffset += diff - leftThreshold;
        }

        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }

    private void renderClouds(Graphics g) {

        // subtracting xOffset add a moving animation to the clouds
        for (int i = 0; i < levelManager.getCurrentLevel().getLevelData().length; i++) {
            // 204 is a y  representing the end of the horizon in the background image
            g.drawImage(backgroundCloudBig, (int) (i * Config.LevelEnv.BIG_CLOUD_WIDTH - xLevelOffset * 0.3), (int) (204 * SCALE), Config.LevelEnv.BIG_CLOUD_WIDTH, Config.LevelEnv.BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudYPositions.length; i++) {
            g.drawImage(backgroundCloudSmall, (int) (i * 4 * Config.LevelEnv.SMALL_CLOUD_WIDTH - xLevelOffset * 0.9), smallCloudYPositions[i], Config.LevelEnv.SMALL_CLOUD_WIDTH, Config.LevelEnv.SMALL_CLOUD_HEIGHT, null);
        }

    }

    private void generateSmallCloudPositions() {
        smallCloudYPositions = new int[8];

        Random random = new Random();
        for (int i = 0; i < smallCloudYPositions.length; i++) {
            smallCloudYPositions[i] = (int) (random.nextInt(100, 190) * SCALE);
        }
    }
}
