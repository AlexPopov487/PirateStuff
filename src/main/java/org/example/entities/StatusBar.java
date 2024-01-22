package org.example.entities;

import org.example.gameState.Drawable;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Config.StatusBar.*;
import static org.example.types.AtlasType.ATLAS_STATUS_BAR;

public class StatusBar extends BaseEntity implements Drawable {
    private final Heath playerHeath;
    private BufferedImage statusBarAsset;
    private int heathBarFill = HEALTH_BAR_WIDTH;


    public StatusBar(Heath playerHeath) {
        super(STATUS_BAR_X, STATUS_BAR_Y, STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT);
        this.playerHeath = playerHeath;
        preloadAssets();
    }

    @Override
    public void render(Graphics graphics) {
        graphics.drawImage(statusBarAsset, STATUS_BAR_X, STATUS_BAR_Y, STATUS_BAR_WIDTH, STATUS_BAR_HEIGHT, null);
        renderHealthBar(graphics);
    }

    @Override
    public void update() {
        updateHeathBar();
    }

    private void renderHealthBar(Graphics graphics) {
        graphics.setColor(Color.RED);
        graphics.fillRect(HEALTH_BAR_X_START, HEALTH_BAR_Y_START, heathBarFill, HEALTH_BAR_HEIGHT);
    }

    private void updateHeathBar() {
        heathBarFill = (int) ((playerHeath.getCurrentHeath() / (float) playerHeath.getMaxHealth()) * HEALTH_BAR_WIDTH);
    }

    private void preloadAssets(){
        statusBarAsset = ResourceLoader.getSpriteAtlas(ATLAS_STATUS_BAR);
    }
}
