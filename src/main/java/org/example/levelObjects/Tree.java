package org.example.levelObjects;

import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

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
