package org.example.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;



    protected Rectangle2D.Float hitBox;


    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void updateHitBox() {
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }

    // todo for debugging purposes
    protected void drawHitBox(Graphics g) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void initHitBox(float x, float y, float width, float height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public void update() {

    }

    public void render() {

    }
}
