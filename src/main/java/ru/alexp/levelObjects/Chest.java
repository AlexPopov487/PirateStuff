package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

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

    public boolean isAnimationCompleted() {
        return getAnimationIndex() >= objectType.getFrameCount() - 1;
    }
}
