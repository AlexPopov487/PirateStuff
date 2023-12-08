package org.example.entities;

public abstract class Entity {

    protected float x;
    protected float y;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
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
