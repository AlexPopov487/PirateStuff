package ru.alexp.ui;

import ru.alexp.gameState.Drawable;
import ru.alexp.types.GameState;
import ru.alexp.types.AtlasType;
import ru.alexp.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ru.alexp.Config.*;

public class MenuButton implements Drawable {
    private static final int X_OFFSET_CENTER = BUTTON_WIDTH / 2;
    private int x;
    private int y;
    private int rowIndex;
    private GameState state;
    private BufferedImage[] buttonStates;
    private int buttonStateIndex = 0;
    private boolean isMouseOver;
    private boolean isMousePressed;
    private Rectangle hitBox;

    public MenuButton(int x, int y, int rowIndex, GameState state) {
        this.x = x;
        this.y = y;
        this.rowIndex = rowIndex;
        this.state = state;
        preloadButtonImages();
        initHitBox();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(buttonStates[buttonStateIndex], x - X_OFFSET_CENTER, y, BUTTON_WIDTH, BUTTON_HEIGHT, null);
    }

    @Override
    public void update() {
        buttonStateIndex = 0;
        if (isMouseOver) {
            buttonStateIndex = 1;
        }
        if (isMousePressed) {
            buttonStateIndex = 2;
        }
    }

    public void updateGameState() {
        GameState.setState(state);
    }

    public void resetMenuButtonState() {
        isMouseOver = false;
        isMousePressed = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public boolean isMouseOver() {
        return isMouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        isMouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        isMousePressed = mousePressed;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    private void preloadButtonImages() {
        buttonStates = new BufferedImage[3];
        BufferedImage buttonAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_MENU_BUTTONS);

        for (int column = 0; column < buttonStates.length; column++) {
            buttonStates[column] = buttonAtlas.getSubimage(column * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    private void initHitBox() {
        hitBox = new Rectangle(x - X_OFFSET_CENTER, y, BUTTON_WIDTH, BUTTON_HEIGHT);
    }
}
