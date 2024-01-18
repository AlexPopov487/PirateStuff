package org.example.entities;

public class Directions {
    private boolean isMoving = false;
    private boolean isMovingLeft;
    private boolean isMovingRight;
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
    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void reset() {
        isMoving = false;
        isMovingLeft = false;
        isMovingRight = false;
        isJumping = false;
        isInAir = false;
    }

    public boolean isNoDirectionSet() {
        return !isMovingLeft && !isMovingRight && !isInAir;
    }

    public boolean isMovingToRightAndLeft() {
        return isMovingRight && isMovingLeft;}
}
