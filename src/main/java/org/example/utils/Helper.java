package org.example.utils;

import org.example.Config;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.GravitySettings;
import org.example.levelObjects.*;
import org.example.levelObjects.Container;
import org.example.levels.LevelManager;
import org.example.types.EnemyType;
import org.example.types.EntityType;
import org.example.types.GrassType;
import org.example.types.LevelObjectType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.example.Config.GRAVITY_FORCE;

public class Helper {
    private final static Random random = new Random();

    public static GravitySettings generateGravitySettingForEntity(EntityType entityType) {
        return switch (entityType) {
            case PLAYER ->
                    new GravitySettings(Config.Player.AIR_SPEED, GRAVITY_FORCE, Config.Player.JUMP_SPEED, Config.Player.POST_COLLISION_FALL_SPEED);
            case ENEMY ->
                    new GravitySettings(Config.Enemy.AIR_SPEED, GRAVITY_FORCE, Config.Enemy.JUMP_SPEED, Config.Enemy.POST_COLLISION_FALL_SPEED);
        };
    }

    public static GrassType getRandomGrassType()  {
        return GrassType.values()[random.nextInt(GrassType.values().length -1)];
    }

    public static int getRandomInt(int min, int max){
        return random.nextInt(min, max);
    }
}
