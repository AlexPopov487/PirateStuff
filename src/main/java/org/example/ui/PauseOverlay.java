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
    private BufferedImage background;
    private int backgroundX;
    private int backgroundY;
    private int backgroundWidth;
    private int backgroundHeight;
    private SoundButton musicButton;
    private SoundButton sfxButton;
    private UrmButton exitToMenuButton;
    private UrmButton restartButton;
    private UrmButton resumeButton;
    private VolumeButton volumeButton;


    public PauseOverlay(Playing playing) {
        this.playing = playing;
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
        musicButton.render(graphics);
        sfxButton.render(graphics);

        exitToMenuButton.render(graphics);
        restartButton.render(graphics);
        resumeButton.render(graphics);

        volumeButton.render(graphics);
    }

    @Override
    public void update() {
        musicButton.update();
        sfxButton.update();

        exitToMenuButton.update();
        restartButton.update();
        resumeButton.update();

        volumeButton.update();
    }

    public void mouseDragged(MouseEvent e){
        if (volumeButton.isMousePressed()) {
            volumeButton.updateVolume(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isHoveredOverButton(e, musicButton.getHitBox())) {
            musicButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, sfxButton.getHitBox())) {
            sfxButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, exitToMenuButton.getHitBox())) {
            exitToMenuButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, restartButton.getHitBox())) {
            restartButton.setMousePressed(true);
        } else if (isHoveredOverButton(e, resumeButton.getHitBox())) {
            resumeButton.setMousePressed(true);
         }else if (isHoveredOverButton(e, volumeButton.getHitBox())) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, musicButton.getHitBox()) && musicButton.isMousePressed()) {
            musicButton.switchMuted();
        } else if (isHoveredOverButton(e, sfxButton.getHitBox()) && sfxButton.isMousePressed()) {
            sfxButton.switchMuted();
        } else if (isHoveredOverButton(e, exitToMenuButton.getHitBox()) && exitToMenuButton.isMousePressed()) {
            GameState.setState(GameState.MENU);
            playing.resetPlaying();
        } else if (isHoveredOverButton(e, restartButton.getHitBox()) && restartButton.isMousePressed()) {
            playing.resetPlaying();
        } else if (isHoveredOverButton(e, resumeButton.getHitBox()) && resumeButton.isMousePressed()) {
            playing.resumeGame();
        }

        musicButton.resetButtonState();
        sfxButton.resetButtonState();
        exitToMenuButton.resetButtonState();
        restartButton.resetButtonState();
        resumeButton.resetButtonState();
        volumeButton.resetButtonState();

    }

    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        musicButton.setMouseHovered(false);
        sfxButton.setMouseHovered(false);
        exitToMenuButton.setMouseHovered(false);
        resumeButton.setMouseHovered(false);
        restartButton.setMouseHovered(false);
        volumeButton.setMouseHovered(false);

        if (isHoveredOverButton(e, musicButton.getHitBox())) {
            musicButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, sfxButton.getHitBox())) {
            sfxButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, exitToMenuButton.getHitBox())) {
            exitToMenuButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, restartButton.getHitBox())) {
            restartButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, resumeButton.getHitBox())) {
            resumeButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, volumeButton.getHitBox())) {
            volumeButton.setMouseHovered(true);
        }
    }

    private void initButtons() {
        int soundButtonX = (int) (450 * SCALE);
        int musicButtonY = (int) (140 * SCALE);
        int sfxButtonY = (int) (186 * SCALE);

        musicButton = new SoundButton(soundButtonX, musicButtonY, SOUND_BUTTON_WIDTH, SOUND_BUTTON_HEIGHT);
        sfxButton = new SoundButton(soundButtonX, sfxButtonY, SOUND_BUTTON_WIDTH, SOUND_BUTTON_HEIGHT);

        int toMenuButtonX = (int) (313 * SCALE);
        int restartButtonX = (int) (387 * SCALE);
        int resumeButtonX = (int) (462 * SCALE);
        int urmButtonY = (int) (325 * SCALE);
        resumeButton = new UrmButton(resumeButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 0);
        restartButton = new UrmButton(restartButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 1);
        exitToMenuButton = new UrmButton(toMenuButtonX, urmButtonY, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, 2);

        int volumeButtonX = (int) (309 * SCALE);
        int volumeButtonY = (int) (278 * SCALE);
        volumeButton = new VolumeButton(volumeButtonX, volumeButtonY, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT);
    }
}
