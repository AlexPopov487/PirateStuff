package org.example.entities;

public class Actions {
    private boolean isAttacking;
    private boolean isPowerAttacking;

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isPowerAttacking() {
        return isPowerAttacking;
    }

    public void setPowerAttacking(boolean powerAttacking) {
        isPowerAttacking = powerAttacking;
    }

    public void resetAll() {
        isAttacking = false;
        isPowerAttacking = false;
    }
}
