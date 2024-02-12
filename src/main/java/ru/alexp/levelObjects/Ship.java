package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

public class Ship extends LevelObject implements Drawable {
    private final HoverEffect hoverEffect;

    public Ship(float x, float y) {
        super(x, y, LevelObjectType.SHIP, true);
        initHitBox(Config.LevelEnv.SHARK_WIDTH, Config.LevelEnv.SHARK_HEIGHT);
        hoverEffect = new HoverEffect();
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(null);
        hoverEffect.update(Config.LevelEnv.SHARK_HOVER_OFFSET_MAX,
                Config.LevelEnv.SHARK_HOVER_INC_STEP,
                hitBox,
                y);
    }
}
