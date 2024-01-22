package org.example.levelObjects;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Container extends LevelObject implements Drawable {
    public Container(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, false);
        prepareHitBox(levelObjectType);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        if (shouldAnimate) {
            updateAnimationTick();
        }
    }

    private void prepareHitBox(LevelObjectType levelObjectType) {
        if (LevelObjectType.BOX.equals(levelObjectType)) {
            initHitBox(Config.LevelEnv.BOX_HIT_BOX_WIDTH, Config.LevelEnv.BOX_HIT_BOX_HEIGHT);
        } else if (LevelObjectType.BARREL.equals(levelObjectType)) {
            initHitBox(Config.LevelEnv.BARREL_HIT_BOX_WIDTH, Config.LevelEnv.BARREL_HIT_BOX_HEIGHT);
        }

        // By default objects are drawn from the top left corner of a tile (pixel)
        // offset to draw the container at the bottom on the tile (so the containers are places on the floor)
        hitBox.y += getyDrawOffset() + ((int) Game.SCALE * 2);
        // place the container at the center of the tile (x-axis)
        hitBox.x += (float) getxDrawOffset() / 2;
    }
}
