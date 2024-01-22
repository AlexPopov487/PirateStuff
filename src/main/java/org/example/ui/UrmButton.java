package org.example.ui;

import org.example.gameState.Drawable;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Config.*;

// URM stands for Unpause-Replay-Menu
public class UrmButton extends PauseButtonBase implements Drawable {

    private BufferedImage[][] urmButtonImages;
    private int currRowIndex;
    private int currentColumnIndex;
    private boolean isMouseHovered;
    private boolean isMousePressed;
    private boolean isResumed;
    private boolean isRestarted;
    private boolean isExitedToMenu;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        currRowIndex = rowIndex;
        preloadImages();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(urmButtonImages[currRowIndex][currentColumnIndex], x, y, URM_BUTTON_WIDTH, URM_BUTTON_HEIGHT, null);
    }

    @Override
    public void update() {
        if (isResumed){
            currRowIndex = 0;
        } else if (isRestarted) {
            currRowIndex = 1;
        } else if (isExitedToMenu) {
            currRowIndex = 2;
        }

        currentColumnIndex = 0;
        if (isMouseHovered) {
            currentColumnIndex = 1;
        }
        if (isMousePressed) {
            currentColumnIndex = 2;
        }
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



    private void preloadImages() {
        BufferedImage urmButtonAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_URM_BUTTONS);

        // on the actual atlas we have 3 rows and 3 columns of images
        urmButtonImages = new BufferedImage[3][3];

        for (int row = 0; row < urmButtonImages.length; row++) {
            for (int column = 0; column < urmButtonImages[row].length; column++) {
                urmButtonImages[row][column] = urmButtonAtlas.getSubimage(column * URM_BUTTON_WIDTH_DEFAULT,
                        row * URM_BUTTON_HEIGHT_DEFAULT,
                        URM_BUTTON_WIDTH_DEFAULT,
                        URM_BUTTON_HEIGHT_DEFAULT);
            }
        }
    }
}
