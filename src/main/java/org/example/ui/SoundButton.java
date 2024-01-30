package org.example.ui;

import org.example.gameState.Drawable;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Config.*;

public class SoundButton extends PauseButtonBase implements Drawable {
    private BufferedImage[][] soundButtonImages;
    private boolean isMouseHovered;
    private boolean isMousePressed;
    private boolean isMuted;
    private int currRowIndex;
    private int currentColumnIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);

        preloadImages();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(soundButtonImages[currRowIndex][currentColumnIndex], x, y, width, height, null);
    }

    @Override
    public void update() {
        if (isMuted) {
            currRowIndex = 1;
        } else {
            currRowIndex = 0;
        }

        currentColumnIndex = 0;
        if (isMouseHovered) {
            currentColumnIndex = 1;
        }
        if (isMousePressed) {
            currentColumnIndex = 2;
        }
    }

    public void toggleMute() {
        isMuted = !isMuted;
    }

    public void resetButtonState() {
        isMousePressed = false;
        isMouseHovered = false;
    }

    public boolean isMouseHovered() {
        return isMouseHovered;
    }

    public void setMouseHovered(boolean mouseHovered) {
        isMouseHovered = mouseHovered;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        isMousePressed = mousePressed;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }

    private void preloadImages() {
        BufferedImage soundButtonAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_SOUND_BUTTONS);

        // on the actual atlas we have 2 rows and 3 columns of images
        soundButtonImages = new BufferedImage[2][3];

        for (int row = 0; row < soundButtonImages.length; row++) {
            for (int column = 0; column < soundButtonImages[row].length; column++) {
                soundButtonImages[row][column] = soundButtonAtlas.getSubimage(column * SOUND_BUTTON_WIDTH_DEFAULT,
                        row * SOUND_BUTTON_HEIGHT_DEFAULT,
                        SOUND_BUTTON_WIDTH_DEFAULT,
                        SOUND_BUTTON_HEIGHT_DEFAULT);
            }
        }
    }
}
