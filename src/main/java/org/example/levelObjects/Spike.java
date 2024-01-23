package org.example.levelObjects;

import org.example.Config;
import org.example.types.LevelObjectType;

public class Spike extends LevelObject{
    public Spike(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, false);
        initHitBox(Config.LevelEnv.SPIKE_HIT_BOX_WIDTH, Config.LevelEnv.SPIKE_HIT_BOX_HEIGHT);
        // needed to place the spike perfectly at the bottom of the surface
        hitBox.y += getyDrawOffset();
    }
}
