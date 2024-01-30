package org.example.gameState;

import org.example.Game;
import org.example.GamePanel;
import org.example.types.AtlasType;
import org.example.types.GameState;
import org.example.ui.UrmButton;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.example.Config.URM_BUTTON_HEIGHT;
import static org.example.Config.URM_BUTTON_WIDTH;
import static org.example.Game.SCALE;
import static org.example.utils.ButtonUtils.isHoveredOverButton;

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
        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, replayButton.getHitBox())) {
            replayButton.setMousePressed(true);

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
            playing.resetPlaying();
            GameState.setState(GameState.MENU);
        } else if (isHoveredOverButton(e, replayButton.getHitBox()) && replayButton.isMousePressed()) {
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


        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, replayButton.getHitBox())) {
            replayButton.setMouseHovered(true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (KeyEvent.VK_ESCAPE == e.getKeyCode()) {
            playing.resetPlaying();
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
        int menuButtonX = (int) (335 * SCALE);
        int replayButtonX = (int) (440 * SCALE);
        int y = (int) (210 * SCALE);

        menuButton = new UrmButton(menuButtonX, y, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 2);
        replayButton = new UrmButton(replayButtonX, y, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 1);
    }
}
