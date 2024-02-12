package ru.alexp.entities;

import ru.alexp.gameState.Drawable;
import ru.alexp.levelObjects.Key;
import ru.alexp.utils.ResourceLoader;
import ru.alexp.Config;
import ru.alexp.types.AtlasType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StatusBar extends BaseEntity implements Drawable {
    private final Health playerHealth;
    private final Stamina playerStamina;
    private BufferedImage statusBarAsset;
    private int heathBarFill = Config.StatusBar.HEALTH_BAR_WIDTH;
    private int staminaBarFill = Config.StatusBar.STAMINA_BAR_WIDTH;
    private Key key;


    public StatusBar(Health playerHealth, Stamina playerStamina) {
        super(Config.StatusBar.STATUS_BAR_X, Config.StatusBar.STATUS_BAR_Y, Config.StatusBar.STATUS_BAR_WIDTH, Config.StatusBar.STATUS_BAR_HEIGHT);
        this.playerHealth = playerHealth;
        this.playerStamina = playerStamina;
        preloadAssets();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(statusBarAsset, Config.StatusBar.STATUS_BAR_X, Config.StatusBar.STATUS_BAR_Y, Config.StatusBar.STATUS_BAR_WIDTH, Config.StatusBar.STATUS_BAR_HEIGHT, null);
        renderHealthBar(graphics);
        renderStaminaBar(graphics);
    }

    @Override
    public void update() {
        updateHeathBar();
        updateStaminaBar();
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    private void renderHealthBar(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(Config.StatusBar.HEALTH_BAR_X_START, Config.StatusBar.HEALTH_BAR_Y_START, heathBarFill, Config.StatusBar.HEALTH_BAR_HEIGHT);
    }

    private void renderStaminaBar(Graphics graphics) {
        graphics.setColor(Color.CYAN);
        graphics.fillRect(Config.StatusBar.STAMINA_BAR_X_START, Config.StatusBar.STAMINA_BAR_Y_START, staminaBarFill, Config.StatusBar.STAMINA_BAR_HEIGHT);
    }

    private void updateHeathBar() {
        heathBarFill = (int) ((playerHealth.getCurrentHealth() / (float) playerHealth.getMaxHealth()) * Config.StatusBar.HEALTH_BAR_WIDTH);
    }

    private void updateStaminaBar() {
        staminaBarFill = (int) ((playerStamina.getCurrentValue() / (float) Config.StatusBar.STAMINA_MAX_VALUE) * Config.StatusBar.STAMINA_BAR_WIDTH);
        playerStamina.incRecoveryTick();
        if (playerStamina.getRecoveryTick() >= Config.StatusBar.STAMINA_RECOVERY_DELAY) {
            playerStamina.setRecoveryTick(0);
            playerStamina.incCurrentValue(1);
        }
    }

    private void preloadAssets(){
        statusBarAsset = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_STATUS_BAR);
    }
}
