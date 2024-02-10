package org.example.levelObjects;

import org.example.Config;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Chest extends LevelObject implements Drawable {

    public Chest(float x, float y) {
        super(x, y, LevelObjectType.CHEST, false);
        initHitBox(Config.LevelEnv.CHEST_WIDTH, Config.LevelEnv.CHEST_HEIGHT);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        if (shouldAnimate) {
            updateAnimationTick(35);
        }
    }
}
