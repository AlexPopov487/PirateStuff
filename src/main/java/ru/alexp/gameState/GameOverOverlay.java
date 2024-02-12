package ru.alexp.gameState;

import ru.alexp.Game;
import ru.alexp.GamePanel;
import ru.alexp.types.AtlasType;
import ru.alexp.types.GameState;
import ru.alexp.ui.UrmButton;
import ru.alexp.utils.ResourceLoader;
import ru.alexp.Config;
import ru.alexp.utils.ButtonUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class GameOverOverlay implements Drawable, GameStateActions {

    private BufferedImage background;
    private BufferedImage gameOverScreenAsset;
    private UrmButton menuButton;
    private UrmButton replayButton;
    private int gameOverlayX;
    private int gameOverlayY;
    private int gameOverlayWidth;
    private int gameOverlayHeight;

    private final Playing playing;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        preloadAssets();
        initButtons();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, 0, 0, GamePanel.getWindowWidth(), GamePanel.getWindowHeight(), null);
        graphics.drawImage(gameOverScreenAsset, gameOverlayX, gameOverlayY, gameOverlayWidth, gameOverlayHeight, null);

        menuButton.render(graphics);
        replayButton.render(graphics);
    }

    @Override
    public void update() {
        menuButton.update();
        replayButton.update();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMousePressed(true);
        } else if (ButtonUtils.isHoveredOverButton(e, replayButton.getHitBox())) {
            replayButton.setMousePressed(true);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
            playing.loadFirstLevelResources();
            GameState.setState(GameState.MENU);
        } else if (ButtonUtils.isHoveredOverButton(e, replayButton.getHitBox()) && replayButton.isMousePressed()) {
            playing.resetPlaying();
            GameState.setState(GameState.PLAYING);
        }

        menuButton.resetButtonState();
        replayButton.resetButtonState();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        menuButton.setMouseHovered(false);
        replayButton.setMouseHovered(false);


        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMouseHovered(true);
        } else if (ButtonUtils.isHoveredOverButton(e, replayButton.getHitBox())) {
            replayButton.setMouseHovered(true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
            playing.loadFirstLevelResources();
            GameState.setState(GameState.MENU);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void preloadAssets() {
        gameOverScreenAsset = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_DEATH_SCREEN);
        background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_BACKGROUND_GAME_OVER);

        gameOverlayWidth = (int) (gameOverScreenAsset.getWidth() * Game.SCALE);
        gameOverlayHeight = (int) (gameOverScreenAsset.getHeight() * Game.SCALE);
        gameOverlayX = (GamePanel.getWindowWidth() / 2) - (gameOverlayWidth / 2);
        gameOverlayY = (GamePanel.getWindowHeight() / 2) - (gameOverlayHeight / 2);

    }

    private void initButtons() {
        int menuButtonX = (int) (335 * Game.SCALE);
        int replayButtonX = (int) (440 * Game.SCALE);
        int y = (int) (210 * Game.SCALE);

        menuButton = new UrmButton(menuButtonX, y, Config.URM_BUTTON_WIDTH, Config.URM_BUTTON_HEIGHT, 2);
        replayButton = new UrmButton(replayButtonX, y, Config.URM_BUTTON_WIDTH, Config.URM_BUTTON_HEIGHT, 1);
    }
}
