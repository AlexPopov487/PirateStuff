package org.example.entities;

import org.example.gameState.Drawable;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Config.StatusBar.*;
import static org.example.Config.StatusBar.STAMINA_BAR_Y_START;
import static org.example.types.AtlasType.ATLAS_STATUS_BAR;

public class StatusBar extends BaseEntity implements Drawable {
    private final Health playerHealth;
    private final Stamina playerStamina;
    private BufferedImage statusBarAsset;
    private int heathBarFill = HEALTH_BAR_WIDTH;
    private int staminaBarFill = STAMINA_BAR_WIDTH;


    public StatusBar(Health playerHealth, Stamina playerStamina) {
        super(STATUS_BAR_X, STATUS_BAR_Y, STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT);
        this.playerHealth = playerHealth;
        this.playerStamina = playerStamina;
        preloadAssets();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(statusBarAsset, STATUS_BAR_X, STATUS_BAR_Y, STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, null);
        renderHealthBar(graphics);
        renderStaminaBar(graphics);
    }

    @Override
    public void update() {
        updateHeathBar();
        updateStaminaBar();
    }

    private void renderHealthBar(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(HEALTH_BAR_X_START, HEALTH_BAR_Y_START, heathBarFill, HEALTH_BAR_HEIGHT);
    }

    private void renderStaminaBar(Graphics graphics) {
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(STAMINA_BAR_X_START, STAMINA_BAR_Y_START, staminaBarFill, STAMINA_BAR_HEIGHT);
    }

    private void updateHeathBar() {
        heathBarFill = (int) ((playerHealth.getCurrentHealth() / (float) playerHealth.getMaxHealth()) * HEALTH_BAR_WIDTH);
    }

    private void updateStaminaBar() {
        staminaBarFill = (int) ((playerStamina.getCurrentValue() / (float) STAMINA_MAX_VALUE) * STAMINA_BAR_WIDTH);
        playerStamina.incRecoveryTick();
        if (playerStamina.getRecoveryTick() >= STAMINA_RECOVERY_DELAY) {
            playerStamina.setRecoveryTick(0);
            playerStamina.incCurrentValue(1);
        }
    }

    private void preloadAssets(){
        statusBarAsset = ResourceLoader.getSpriteAtlas(ATLAS_STATUS_BAR);
    }
}
