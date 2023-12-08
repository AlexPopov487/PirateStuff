package org.example.entities;

public class Directions {
    private boolean isMoving = false;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp;
    private boolean isMovingDown;

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        isMovingLeft = movingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }

    public void setMovingRight(boolean movingRight) {
        isMovingRight = movingRight;
    }

    public boolean isMovingUp() {
        return isMovingUp;
    }

    public void setMovingUp(boolean movingUp) {
        isMovingUp = movingUp;
    }

    public boolean isMovingDown() {
        return isMovingDown;
    }

    public void setMovingDown(boolean movingDown) {
        isMovingDown = movingDown;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void reset() {
        isMoving = false;
        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;
    }
}
