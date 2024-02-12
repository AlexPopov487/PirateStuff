package ru.alexp.levelObjects;

import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

public class Flag extends LevelObject implements Drawable {
    public Flag(float x, float y) {
        super(x, y, LevelObjectType.FLAG, true);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(null);
    }
}
