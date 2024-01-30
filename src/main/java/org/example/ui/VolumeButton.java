package org.example.ui;

import org.example.gameState.Drawable;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Config.*;

public class VolumeButton extends PauseButtonBase implements Drawable {
    private BufferedImage[] volumeButtonImages;
    private final int sliderDraggedMinX;
    private final int sliderDraggedMaxX;
    private final int volumeSliderX;
    private BufferedImage sliderImage;
    private int currentColumnIndex;
    private boolean isMouseHovered;
    private boolean isMousePressed;
    private boolean isResumed;
    private boolean isRestarted;
    private boolean isExitedToMenu;
    private int dragButtonX;

    public VolumeButton(int x, int y, int width, int height) {
        /*
         x and y passed in the constructor are referred to the x and y for the whole slider.
         To initialise the hitBox correctly we need to recalculate x (y stays the same) so it matches only the
         size of the slider dragger
        */
        super(x + (width / 2), y, VOLUME_BUTTON_WIDTH, height);
        volumeSliderX = x;

        sliderDraggedMinX = x;
        sliderDraggedMaxX = x + width - VOLUME_BUTTON_WIDTH;

        double quarter = (sliderDraggedMaxX - sliderDraggedMinX) * 0.25;
        dragButtonX = (int) (sliderDraggedMaxX - quarter);
        hitBox.x = dragButtonX;
        preloadImages();
    }

    @Override
    public void render(Graphics graphics) {
        // draw the slider and the volume button(i.e. the dragger) on the slider
        graphics.drawImage(sliderImage, volumeSliderX, y, VOLUME_SLIDER_WIDTH , height, null);
        graphics.drawImage(volumeButtonImages[currentColumnIndex], dragButtonX, y, VOLUME_BUTTON_WIDTH, height, null);
    }

    @Override
    public void update() {
        currentColumnIndex = 0;
        if (isMouseHovered) {
            currentColumnIndex = 1;
        }

        if (isMousePressed) {
            currentColumnIndex = 2;
        }
    }

    public void updateVolume(int updSliderX) {
        // position x to the center of the dragger
        updSliderX = updSliderX - (VOLUME_BUTTON_WIDTH / 2);

        if (updSliderX > sliderDraggedMaxX) {
            dragButtonX = sliderDraggedMaxX;
        } else if (updSliderX < sliderDraggedMinX) {
            dragButtonX = sliderDraggedMinX;
        } else {
            dragButtonX = updSliderX;
        }

        // update hitBox to a new x value
        hitBox.x = dragButtonX;
    }

    // For audio controls we need to get a current volume value, where 0f - min, 1f - max
    public float calculateVolumeValue() {
        float range = sliderDraggedMaxX - sliderDraggedMinX;
        float relativeVolumeValue = dragButtonX - sliderDraggedMinX;
        return relativeVolumeValue / range;
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

    public boolean isResumed() {
        return isResumed;
    }

    public void setResumed(boolean resumed) {
        isResumed = resumed;
    }

    public boolean isRestarted() {
        return isRestarted;
    }

    public void setRestarted(boolean restarted) {
        isRestarted = restarted;
    }

    public boolean isExitedToMenu() {
        return isExitedToMenu;
    }

    public void setExitedToMenu(boolean exitedToMenu) {
        isExitedToMenu = exitedToMenu;
    }

    public int getDragButtonX() {
        return dragButtonX;
    }

    public void setDragButtonX(int dragButtonX) {
        this.dragButtonX = dragButtonX;
    }

    private void preloadImages() {
        BufferedImage volumeButtonAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_VOLUME_BUTTONS);

        // the fourth image on the atlas is a slider, it will be preloaded separately
        volumeButtonImages = new BufferedImage[3];

        for (int column = 0; column < volumeButtonImages.length; column++) {
            volumeButtonImages[column] = volumeButtonAtlas.getSubimage(column * VOLUME_BUTTON_WIDTH_DEFAULT,
                    0,
                    VOLUME_BUTTON_WIDTH_DEFAULT,
                    VOLUME_BUTTON_HEIGHT_DEFAULT);
        }

        sliderImage = volumeButtonAtlas.getSubimage(3 * VOLUME_BUTTON_WIDTH_DEFAULT,
                0,
                VOLUME_SLIDER_WIDTH_DEFAULT,
                VOLUME_SLIDER_HEIGHT_DEFAULT);
    }
}
