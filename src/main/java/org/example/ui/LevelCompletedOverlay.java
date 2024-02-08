package org.example.ui;

import org.example.GamePanel;
import org.example.gameState.Drawable;
import org.example.gameState.GameStateActions;
import org.example.types.GameState;
import org.example.gameState.Playing;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.example.Config.*;
import static org.example.Game.SCALE;
import static org.example.utils.ButtonUtils.isHoveredOverButton;

public class LevelCompletedOverlay implements Drawable, GameStateActions {
    private final Playing playing;
    private UrmButton menuButton;
    private UrmButton nextLevelButton;
    private BufferedImage background;
    private int backgroundX;
    private int backgroundY;
    private int backgroundWidth;
    private int backgroundHeight;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        preloadBackground();
        initButtons();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        menuButton.render(graphics);
        nextLevelButton.render(graphics);
    }

    @Override
    public void update() {
        menuButton.update();
        nextLevelButton.update();
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, nextLevelButton.getHitBox())) {
            nextLevelButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
            GameState.setState(GameState.MENU);
            playing.loadFirstLevelResources();
        } else if (isHoveredOverButton(e, nextLevelButton.getHitBox()) && nextLevelButton.isMousePressed()) {
            playing.loadNextLevel();
        }

        menuButton.resetButtonState();
        nextLevelButton.resetButtonState();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        menuButton.setMouseHovered(false);
        nextLevelButton.setMouseHovered(false);


        if (isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, nextLevelButton.getHitBox())) {
            nextLevelButton.setMouseHovered(true);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void preloadBackground() {
        background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_LEVEL_COMPLETED);
        backgroundWidth = (int) (background.getWidth() * SCALE);
        backgroundHeight = (int) (background.getHeight() * SCALE);

        backgroundX = GamePanel.getWindowWidth() / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * SCALE);
    }

    private void initButtons() {
        int menuButtonX = (int) (330 * SCALE);
        int nextLevelButtonX = (int) (445 * SCALE);
        int y = (int) (195 * SCALE);

        menuButton = new UrmButton(menuButtonX, y, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 2);
        nextLevelButton = new UrmButton(nextLevelButtonX, y, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 0);
    }
}
