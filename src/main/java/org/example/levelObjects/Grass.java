package org.example.levelObjects;

import org.example.Config;
import org.example.entities.BaseEntity;
import org.example.types.GrassType;

public class Grass extends BaseEntity {


    private final GrassType grassType;
    public Grass(float x, float y, GrassType grassType) {
        super(x, y, Config.LevelEnv.GRASS_WIDTH, Config.LevelEnv.GRASS_HEIGHT);
        this.grassType = grassType;
    }

    public GrassType getGrassType() {
        return grassType;
    }
}
