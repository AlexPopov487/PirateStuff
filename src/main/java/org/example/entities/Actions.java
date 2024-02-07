package org.example.entities;

public class Actions {
    private boolean isAttacking;
    private boolean isPowerAttacking;
    private boolean isTakingDamage;

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

    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean takingDamage) {
        isTakingDamage = takingDamage;
    }

    public void resetAll() {
        isAttacking = false;
        isPowerAttacking = false;
        isTakingDamage = false;
    }
}
