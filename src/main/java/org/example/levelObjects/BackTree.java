package org.example.levelObjects;

import org.example.GamePanel;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;
import org.example.utils.Helper;

import java.awt.*;

public class BackTree extends Tree implements Drawable {

    // An offset needed to draw background trees with different height
    private final int customYOffset;

    public BackTree(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType);
//        initHitBox();
        customYOffset = Helper.getRandomInt((int) (GamePanel.getCurrentTileSize()), (int) (GamePanel.getCurrentTileSize() * 1.8));
    }

    public int getCustomYOffset() {
        return customYOffset;
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(35);
    }
}
