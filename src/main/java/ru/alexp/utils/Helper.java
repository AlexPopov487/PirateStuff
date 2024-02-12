package ru.alexp.utils;

import ru.alexp.Config;
import ru.alexp.entities.GravitySettings;
import ru.alexp.types.EntityType;
import ru.alexp.types.GrassType;

import java.util.Random;

import static ru.alexp.Config.GRAVITY_FORCE;

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
