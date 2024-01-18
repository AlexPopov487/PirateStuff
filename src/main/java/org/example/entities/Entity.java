package org.example.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;


    protected Rectangle2D.Float hitBox;

    // box that dynamically changes its position depending on player's pos; enemies will take damage, when inside the area
    protected Rectangle2D.Float attackRange;


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
    protected void drawHitBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void drawAttackRangeBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackRange.x - xLevelOffset, (int) attackRange.y, (int) attackRange.width, (int) attackRange.height);
    }

    protected void initHitBox(float x, float y, int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    public abstract void initAttackRange();

    /*
   attack range should follow the direction of the player
   attack range box position is intentionally not reset, i.e. even in neither of player's directions are set
   (e.g. the player is standing on the spot), the last active direction is set by default
   */
    public abstract void updateAttackRange();

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
}
