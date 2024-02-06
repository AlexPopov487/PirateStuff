package org.example.levelObjects;

import org.example.Config;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Dialogue extends LevelObject implements Drawable {

    public Dialogue(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, true);
        initHitBox(Config.DIALOGUE_WIDTH, Config.DIALOGUE_HEIGHT);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(35);
    }
}
