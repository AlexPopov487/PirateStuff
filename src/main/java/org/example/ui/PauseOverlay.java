package org.example.ui;

import org.example.GamePanel;
import org.example.gameState.Drawable;
import org.example.types.GameState;
import org.example.gameState.Playing;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.example.Config.*;
import static org.example.Game.SCALE;
import static org.example.utils.ButtonUtils.isHoveredOverButton;

public class PauseOverlay implements Drawable {
    private final Playing playing;
    private final AudioOptions audioOptions;
    private BufferedImage background;
    private int backgroundX;
    private int backgroundY;
    private int backgroundWidth;
    private int backgroundHeight;
    private UrmButton exitToMenuButton;
    private UrmButton restartButton;
    private UrmButton resumeButton;


    public PauseOverlay(Playing playing) {
        this.playing = playing;
        audioOptions = playing.getGame().getAudioOptions();
        preloadBackground();
        initButtons();
    }

    private void preloadBackground() {
        background = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PAUSE_SCREEN_BACKGROUND);
        backgroundWidth = (int) (background.getWidth() * SCALE);
        backgroundHeight = (int) (background.getHeight() * SCALE);

        backgroundX = GamePanel.getWindowWidth() / 2 - backgroundWidth / 2;
        backgroundY = (int) (25 * SCALE);
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(background, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        audioOptions.render(graphics);

        exitToMenuButton.render(graphics);
        restartButton.render(graphics);
        resumeButton.render(graphics);

    }

    @Override
    public void update() {
        audioOptions.update();

        exitToMenuButton.update();
        restartButton.update();
        resumeButton.update();
    }

    public void mouseDragged(MouseEvent e){
       audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isHoveredOverButton(e, exitToMenuButton.getHitBox())) {
            exitToMenuButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, restartButton.getHitBox())) {
            restartButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, resumeButton.getHitBox())) {
            resumeButton.setMousePressed(true);
         }else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, exitToMenuButton.getHitBox()) && exitToMenuButton.isMousePressed()) {
            GameState.setState(GameState.MENU);
            playing.loadFirstLevelResources();
        } else if (isHoveredOverButton(e, restartButton.getHitBox()) && restartButton.isMousePressed()) {
            playing.resetPlaying();
        } else if (isHoveredOverButton(e, resumeButton.getHitBox()) && resumeButton.isMousePressed()) {
            playing.resumeGame();
        } else {
            audioOptions.mouseReleased(e);
        }

        audioOptions.resetAudioButtonsState();
        exitToMenuButton.resetButtonState();
        restartButton.resetButtonState();
        resumeButton.resetButtonState();

    }

    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        exitToMenuButton.setMouseHovered(false);
        resumeButton.setMouseHovered(false);
        restartButton.setMouseHovered(false);
        audioOptions.setHoverFalse();

       if (isHoveredOverButton(e, exitToMenuButton.getHitBox())) {
            exitToMenuButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, restartButton.getHitBox())) {
            restartButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, resumeButton.getHitBox())) {
            resumeButton.setMouseHovered(true);
        } else {
           audioOptions.mouseMoved(e);
       }
    }

    private void initButtons() {
        int toMenuButtonX = (int) (313 * SCALE);
        int restartButtonX = (int) (387 * SCALE);
        int resumeButtonX = (int) (462 * SCALE);
        int urmButtonY = (int) (325 * SCALE);
        resumeButton = new UrmButton(resumeButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 0);
        restartButton = new UrmButton(restartButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 1);
        exitToMenuButton = new UrmButton(toMenuButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 2);
    }
}
