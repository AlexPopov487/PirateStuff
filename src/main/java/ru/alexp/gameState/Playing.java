package ru.alexp.gameState;

import ru.alexp.Config;
import ru.alexp.Game;
import ru.alexp.GamePanel;
import ru.alexp.entities.EnemyManager;
import ru.alexp.entities.Player;
import ru.alexp.exception.LoadNextLevelException;
import ru.alexp.levelObjects.LevelObjectManager;
import ru.alexp.levels.Level;
import ru.alexp.levels.LevelManager;
import ru.alexp.types.AtlasType;
import ru.alexp.types.GameState;
import ru.alexp.ui.LevelCompletedOverlay;
import ru.alexp.ui.PauseOverlay;
import ru.alexp.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Random;

import static ru.alexp.Game.SCALE;
import static ru.alexp.types.GameState.GAME_OVER;

public class Playing extends StateBase implements GameStateActions, Drawable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int CHARACTER_SPRITE_HEIGHT = 40;
    public static final int CHARACTER_SPRITE_WIDTH = 64;

    private final Player player;
    private final LevelManager levelManager;
    private final EnemyManager enemyManager;
    private final LevelObjectManager levelObjectManager;
    private final PauseOverlay pauseOverlay;
    private boolean isPaused;
    private final LevelCompletedOverlay levelCompletedOverlay;
    private boolean isCurrLevelCompleted = false;
    private final BufferedImage background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND);
    private final BufferedImage backgroundCloudBig = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND_CLOUD_BIG);
    private final BufferedImage backgroundCloudSmall = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYING_BACKGROUND_CLOUD_SMALL);
    private int[] smallCloudYPositions;

    // exceeding the threshold by x% of the windowWidth means that we need to move the level animation to the left
    private final int leftThreshold = (int) (0.4 * GamePanel.getWindowWidth());
    private final int rightThreshold = (int) (0.6 * GamePanel.getWindowWidth());
    private int xLevelOffset;

    // shows for how many tiles it is possible to move the level to the left in pixels
    private int maxLevelOffsetX;
    private boolean isReadyToCompleteLevel = false;
    private boolean isScriptMessageShown = false;

    public Playing(Game game) {
        super(game);
        generateSmallCloudPositions();

        pauseOverlay = new PauseOverlay(this);
        levelManager = new LevelManager();
        enemyManager = new EnemyManager(this);
        levelObjectManager = new LevelObjectManager(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);

        player = new Player(200, 200, (int) (CHARACTER_SPRITE_WIDTH * SCALE), (int) (CHARACTER_SPRITE_HEIGHT * SCALE), this);
        loadFirstLevelResources();
    }

    public Player getPlayer() {
        return player;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    @Override
    public void update() {
        if (isPaused) {
            pauseOverlay.update();
        } else if (isCurrLevelCompleted) {
            levelCompletedOverlay.update();

        } else {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkPlayerCloseToBorder();
            levelObjectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(background, 0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight(), null);
        renderClouds(g);

        levelManager.render(g, xLevelOffset);
        player.render(g, xLevelOffset);
        enemyManager.render(g, xLevelOffset);
        levelObjectManager.render(g, xLevelOffset);

        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight());
            pauseOverlay.render(g);
        } else if (isCurrLevelCompleted) {
            levelCompletedOverlay.render(g);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
//        if (e.getButton() == MouseEvent.BUTTON1) {
//            setPlayerAttack();
//        } else if (e.getButton() == MouseEvent.BUTTON3) {
//            setPlayerPowerAttack();
//        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mousePressed(e);
        } else if (isCurrLevelCompleted) {
            levelCompletedOverlay.mousePressed(e);
        } else {
            if (e.getButton() == MouseEvent.BUTTON1) {
                setPlayerAttack();
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                setPlayerPowerAttack();
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mouseReleased(e);
        } else if (isCurrLevelCompleted) {
            levelCompletedOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isPaused) {
            pauseOverlay.mouseMoved(e);
        } else if (isCurrLevelCompleted) {
            levelCompletedOverlay.mouseMoved(e);
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
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                disableScriptMessage();
                log.trace("keyPressed : D");
                player.getDirections().setMovingRight(true);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                log.trace("keyPressed : A");
                player.getDirections().setMovingLeft(true);
            }
            case KeyEvent.VK_SPACE -> {
                log.trace("keyPressed : SPACE");
                player.getDirections().setJumping(true);
            }
            case KeyEvent.VK_ESCAPE -> {
                log.trace("keyPressed : ESCAPE");
                switchPaused();
            }
            case KeyEvent.VK_SHIFT -> {
                log.trace("keyPressed : SHIFT");
                setPlayerAttack();
            } case KeyEvent.VK_CONTROL -> {
                log.trace("keyPressed : ALT");
                setPlayerPowerAttack();
            }
        }
    }

    private void disableScriptMessage() {
        if (isScriptMessageShown) {
            isScriptMessageShown = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
                log.trace("keyReleased : D");
                player.getDirections().setMovingRight(false);
            }
            case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
                log.trace("keyReleased : A");
                player.getDirections().setMovingLeft(false);
            }
            case KeyEvent.VK_SPACE -> {
                log.trace("keyReleased : SPACE");
                player.getDirections().setJumping(false);
            }
        }
    }

    public void resetPlaying() {
        resumeGame();
        player.reset();
        enemyManager.resetAll();
        levelObjectManager.resetAll();
        isCurrLevelCompleted = false;
        isReadyToCompleteLevel = false;
    }

    public void loadNextLevel() throws LoadNextLevelException {
        Level level = levelManager.loadNextLevel();

//        enemyManager.loadEnemies(level);
        levelObjectManager.loadLevelObjects(level);
        player.setCurrentLevelData(level.getLevelData());
        player.setSpawnPosition(level.getPlayerSpawnPosition());
        maxLevelOffsetX = level.getMaxLevelOffsetX();
        game.getAudioPlayer().playLevelSong(levelManager.getCurrentLevelIndex());

        resetPlaying();
    }

    public void loadFirstLevelResources() {
        isScriptMessageShown = true;
        levelManager.setFirstLevel();
        levelObjectManager.loadLevelObjects(levelManager.getCurrentLevel());
        player.setCurrentLevelData(levelManager.getCurrentLevel().getLevelData());
        player.setSpawnPosition(levelManager.getCurrentLevel().getPlayerSpawnPosition());
        player.setKeyCollected(false);
        if (Objects.nonNull(player.getStatusBar().getKey())) {
            player.getStatusBar().setKey(null);
        }

        maxLevelOffsetX = levelManager.getCurrentLevel().getMaxLevelOffsetX();
        game.getAudioPlayer().playLevelSong(levelManager.getCurrentLevelIndex());

        resetPlaying();
    }

    public void checkEnemyHit(Rectangle2D.Float playerAttackRange) {
        enemyManager.checkEnemyGotHit(playerAttackRange);
    }

    public boolean isReadyToCompleteLevel() {
        return isReadyToCompleteLevel;
    }

    public void setReadyToCompleteLevel(boolean readyToCompleteLevel) {
        isReadyToCompleteLevel = readyToCompleteLevel;
    }

    public void checkEnemyStumped(Player player) {
        enemyManager.checkEnemyStumped(player);
    }

    public boolean isScriptMessageShown() {
        return isScriptMessageShown;
    }

    public void setScriptMessageShown(boolean scriptMessageShown) {
        isScriptMessageShown = scriptMessageShown;
    }

    public void checkPotionCollected(Rectangle2D.Float playerHitBox) {
        levelObjectManager.checkObjectCollected(playerHitBox);
    }

    public void checkExitReached(Rectangle2D.Float playerHitBox) {
        levelObjectManager.checkExitReached(playerHitBox);
    }


    public void checkKeyCollected(Rectangle2D.Float playerHitBox) {
        levelObjectManager.checkObjectCollected(playerHitBox);
    }

    public void checkLevelObjectDestroyed(Rectangle2D.Float attackRange) {
        levelObjectManager.checkObjectDestroyed(attackRange);
    }

    public void checkSpikeTrapTouched(Rectangle2D.Float playerHitBox) {
        levelObjectManager.checkSpikeTrapTouched(playerHitBox);
    }

    public LevelObjectManager getLevelObjectManager() {
        return levelObjectManager;
    }

    public void checkDrowned(Rectangle2D.Float playerHitBox) {
        levelObjectManager.checkDrowned(playerHitBox);
    }

    private void calculateLevelOffset() {
        maxLevelOffsetX = levelManager.getCurrentLevel().getMaxLevelOffsetX();
    }

    private void setPlayerAttack() {
        player.getActions().setAttacking(true);
    }

    private void setPlayerPowerAttack() {
        player.doPowerAttack();
    }

    public void setGameOver() {
        resetPlaying();
        GameState.setState(GAME_OVER);
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

    public void setCurrLevelCompleted(boolean isCurrLevelCompleted) {
        this.isCurrLevelCompleted = isCurrLevelCompleted;
        if (isCurrLevelCompleted) {
            game.getAudioPlayer().playLvlCompletedEffect();
        }
    }
}
