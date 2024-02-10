package org.example.levelObjects;

import org.example.Config;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Key extends LevelObject implements Drawable {
    private final HoverEffect hoverEffect;
    private boolean isHoverActive = true;
    private final float initX;
    private final float initY;

    public Key(float x, float y) {
        super(x, y, LevelObjectType.KEY, true);
        initX = x;
        initY = y;
        initHitBox(Config.LevelEnv.KEY_WIDTH, Config.LevelEnv.KEY_HEIGHT);
        hoverEffect = new HoverEffect();
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(null);
        if (isHoverActive) {
            hoverEffect.update(Config.LevelEnv.KEY_HOVER_OFFSET_MAX, Config.LevelEnv.KEY_HOVER_INC_STEP, getHitBox(), y);
        }
    }

    public HoverEffect getHoverEffect() {
        return hoverEffect;
    }

    public boolean isHoverActive() {
        return isHoverActive;
    }

    public void setHoverActive(boolean hoverActive) {
        isHoverActive = hoverActive;
    }

    @Override
    public void reset() {
        super.reset();
        // if key was collected, its position was changed to be displayed under the status bar. Restoring init positions.
        x = initX;
        y = initY;
        initHitBox(Config.LevelEnv.KEY_WIDTH, Config.LevelEnv.KEY_HEIGHT);
        isHoverActive = true;
    }
}
