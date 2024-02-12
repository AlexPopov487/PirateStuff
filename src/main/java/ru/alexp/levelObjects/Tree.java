package ru.alexp.levelObjects;

import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

public class Tree extends LevelObject implements Drawable {

    public Tree(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, true);
//        initHitBox();
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(35);
    }
}
