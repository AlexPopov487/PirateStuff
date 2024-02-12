package ru.alexp.ui;

import ru.alexp.GamePanel;
import ru.alexp.exception.LoadNextLevelException;
import ru.alexp.gameState.Drawable;
import ru.alexp.gameState.GameStateActions;
import ru.alexp.types.GameState;
import ru.alexp.gameState.Playing;
import ru.alexp.types.AtlasType;
import ru.alexp.utils.ResourceLoader;
import ru.alexp.Config;
import ru.alexp.Game;
import ru.alexp.utils.ButtonUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMousePressed(true);
        } else if (ButtonUtils.isHoveredOverButton(e, nextLevelButton.getHitBox())) {
            nextLevelButton.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox()) && menuButton.isMousePressed()) {
            GameState.setState(GameState.MENU);
            playing.loadFirstLevelResources();
        } else if (ButtonUtils.isHoveredOverButton(e, nextLevelButton.getHitBox()) && nextLevelButton.isMousePressed()) {
           try {
               playing.loadNextLevel();
           } catch (LoadNextLevelException ex) {
               playing.loadFirstLevelResources();
           }
        }

        menuButton.resetButtonState();
        nextLevelButton.resetButtonState();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        menuButton.setMouseHovered(false);
        nextLevelButton.setMouseHovered(false);


        if (ButtonUtils.isHoveredOverButton(e, menuButton.getHitBox())) {
            menuButton.setMouseHovered(true);
        } else if (ButtonUtils.isHoveredOverButton(e, nextLevelButton.getHitBox())) {
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
        backgroundWidth = (int) (background.getWidth() * Game.SCALE);
        backgroundHeight = (int) (background.getHeight() * Game.SCALE);

        backgroundX = GamePanel.getWindowWidth() / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);
    }

    private void initButtons() {
        int menuButtonX = (int) (330 * Game.SCALE);
        int nextLevelButtonX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);

        menuButton = new UrmButton(menuButtonX, y, Config.URM_BUTTON_WIDTH, Config.URM_BUTTON_HEIGHT, 2);
        nextLevelButton = new UrmButton(nextLevelButtonX, y, Config.URM_BUTTON_WIDTH, Config.URM_BUTTON_HEIGHT, 0);
    }
}
