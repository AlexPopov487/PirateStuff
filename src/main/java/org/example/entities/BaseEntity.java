package org.example.entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class BaseEntity {
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    public BaseEntity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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
