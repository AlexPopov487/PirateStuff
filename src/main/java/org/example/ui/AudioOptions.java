package org.example.ui;

import org.example.gameState.Drawable;
import org.example.types.GameState;

import java.awt.*;
import java.awt.event.MouseEvent;

import static org.example.Config.*;
import static org.example.Config.VOLUME_SLIDER_HEIGHT;
import static org.example.Game.SCALE;
import static org.example.utils.ButtonUtils.isHoveredOverButton;

public class AudioOptions implements Drawable  {

    private SoundButton musicButton;
    private SoundButton sfxButton;
    private VolumeButton volumeButton;

    public AudioOptions() {
        initButtons();
    }


    @Override
    public void render(Graphics graphics) {
        musicButton.render(graphics);
        sfxButton.render(graphics);
        volumeButton.render(graphics);
    }

    @Override
    public void update() {
        musicButton.update();
        sfxButton.update();
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
        }else if (isHoveredOverButton(e, volumeButton.getHitBox())) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isHoveredOverButton(e, musicButton.getHitBox()) && musicButton.isMousePressed()) {
            musicButton.switchMuted();
        } else if (isHoveredOverButton(e, sfxButton.getHitBox()) && sfxButton.isMousePressed()) {
            sfxButton.switchMuted();
        }
    }

    public void mouseMoved(MouseEvent e) {
        // reset mouseHovered to update button animation
        musicButton.setMouseHovered(false);
        sfxButton.setMouseHovered(false);
        volumeButton.setMouseHovered(false);

        if (isHoveredOverButton(e, musicButton.getHitBox())) {
            musicButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, sfxButton.getHitBox())) {
            sfxButton.setMouseHovered(true);
        } else if (isHoveredOverButton(e, volumeButton.getHitBox())) {
            volumeButton.setMouseHovered(true);
        }
    }

    public void resetAudioButtonsState() {
        musicButton.resetButtonState();
        sfxButton.resetButtonState();
        volumeButton.resetButtonState();
    }

    public void setHoverFalse() {
        musicButton.setMouseHovered(false);
        sfxButton.setMouseHovered(false);
        volumeButton.setMouseHovered(false);
    }


    private void initButtons() {
        int soundButtonX = (int) (450 * SCALE);
        int musicButtonY = (int) (140 * SCALE);
        int sfxButtonY = (int) (186 * SCALE);

        musicButton = new SoundButton(soundButtonX, musicButtonY, SOUND_BUTTON_WIDTH, SOUND_BUTTON_HEIGHT);
        sfxButton = new SoundButton(soundButtonX, sfxButtonY, SOUND_BUTTON_WIDTH, SOUND_BUTTON_HEIGHT);

        int volumeButtonX = (int) (309 * SCALE);
        int volumeButtonY = (int) (278 * SCALE);
        volumeButton = new VolumeButton(volumeButtonX, volumeButtonY, VOLUME_SLIDER_WIDTH, VOLUME_SLIDER_HEIGHT);
    }
}

