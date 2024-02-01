package org.example.entities;

public class Health {
    private final int maxHealth;
    private int currentHeath;

    public Health(int maxHealth) {
        this.maxHealth = maxHealth;
        currentHeath = maxHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHeath;
    }

    public void setCurrentHeath(int currentHeath) {
        if (currentHeath < 0) {
            currentHeath = 0;
        } else if (currentHeath > maxHealth) {
            currentHeath = maxHealth;
        }

        this.currentHeath = currentHeath;
    }

    public void subtractHealth(int amount) {
        setCurrentHeath(currentHeath - amount);
    }

    public boolean isDead() {
        return currentHeath <= 0;}

    public void reset() {
        currentHeath = maxHealth;
    }
}
