package org.example.levelObjects;

import org.example.Config;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Explosion extends LevelObject implements Drawable {
    public Explosion(float x, float y) {
        super(x, y, LevelObjectType.EXPLOSION, true);
        initHitBox(Config.LevelEnv.EXPLOSION_WIDTH, Config.LevelEnv.EXPLOSION_HEIGHT);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(35);
    }
}
