package org.example.levelObjects;

import org.example.Config;
import org.example.Game;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;

import java.awt.*;

public class Potion extends LevelObject  implements Drawable {

    private float hoverOffset;
    private int hoverDirection = 1;

    public Potion(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, true);
        initHitBox(Config.LevelEnv.POTION_HIT_BOX_WIDTH, Config.LevelEnv.POTION_HIT_BOX_HEIGHT);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick();
        updateHoverEffect();
    }

    private void updateHoverEffect() {
        hoverOffset += (0.075f * Game.SCALE * hoverDirection);

        if (hoverOffset >= Config.LevelEnv.POTION_HOVER_OFFSET_MAX) {
            hoverDirection = -1;
        } else if (hoverOffset < 0) {
            hoverDirection = 1;
        }

        hitBox.y = y + hoverOffset;
    }
}
