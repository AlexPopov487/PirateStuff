package org.example.entities;

public class Directions {
    private boolean isMoving = false;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isMovingUp; // todo redundant
    private boolean isMovingDown; // todo redundant
    private boolean isInAir; // todo probably move to gravitySettings
    private boolean isJumping;


    public boolean isJumping() {
        return isJumping;
    }

    public void setJumping(boolean jumping) {
        isJumping = jumping;
    }

    public boolean isInAir() {
        return isInAir;
    }

    public void setInAir(boolean inAir) {
        isInAir = inAir;
    }

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
        isJumping = false;
    }

    public boolean isNoDirectionSet() {
        return !isMovingLeft && !isMovingRight && !isInAir;
    }
}
