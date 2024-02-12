package ru.alexp.ui;

import java.awt.*;

public class PauseButtonBase {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Rectangle hitBox;

    public PauseButtonBase(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        initHitBox();
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    private void initHitBox() {
        hitBox = new Rectangle(x, y, width, height);
    }

}
