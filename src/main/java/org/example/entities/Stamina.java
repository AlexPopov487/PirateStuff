package org.example.entities;

import org.example.Config;

public class Stamina {
    private int recoveryTick;
    private int currentValue;

    public Stamina() {
        currentValue = Config.StatusBar.STAMINA_MAX_VALUE;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void incCurrentValue(int currentValue) {
        this.currentValue += currentValue;

        if (this.currentValue < 0) {
            this.currentValue = 0;
        } else if (this.currentValue > Config.StatusBar.STAMINA_MAX_VALUE) {
            this.currentValue = Config.StatusBar.STAMINA_MAX_VALUE;
        }
    }

    public void setCurrentValue(int currentValue) {
        if (currentValue < 0) {
            currentValue = 0;
        } else if (currentValue > Config.StatusBar.STAMINA_MAX_VALUE) {
            currentValue = Config.StatusBar.STAMINA_MAX_VALUE;
        }

        this.currentValue = currentValue;
    }

    public void subtractStamina(int amount) {
        setCurrentValue(this.currentValue - amount);
    }

    public int getRecoveryTick() {
        return recoveryTick;
    }

    public void setRecoveryTick(int recoveryTick) {
        this.recoveryTick = recoveryTick;
    }

    public void incRecoveryTick() {
        recoveryTick++;
    }

    public void reset() {
        currentValue = Config.StatusBar.STAMINA_MAX_VALUE;
    }
}
