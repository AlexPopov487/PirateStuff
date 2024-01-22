package org.example;


import org.example.types.EnemyState;
import org.example.types.EnemyType;
import org.example.types.LevelObjectType;

import static org.example.Game.SCALE;

public class Config {
    public static final int BUTTON_WIDTH_DEFAULT = 140;
    public static final int BUTTON_HEIGHT_DEFAULT = 56;
    public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * SCALE);
    public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * SCALE);


    public static final int SOUND_BUTTON_WIDTH_DEFAULT = 42;
    public static final int SOUND_BUTTON_HEIGHT_DEFAULT = 42;
    public static final int SOUND_BUTTON_WIDTH = (int) (SOUND_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int SOUND_BUTTON_HEIGHT = (int) (SOUND_BUTTON_HEIGHT_DEFAULT * SCALE);

    public static final int URM_BUTTON_WIDTH_DEFAULT = 56;
    public static final int URM_BUTTON_HEIGHT_DEFAULT = 56;
    public static final int URM_BUTTON_WIDTH = (int) (URM_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int URM_BUTTON_HEIGHT = (int) (URM_BUTTON_HEIGHT_DEFAULT * SCALE);

    public static final int VOLUME_BUTTON_WIDTH_DEFAULT = 28;
    public static final int VOLUME_BUTTON_HEIGHT_DEFAULT = 44;
    public static final int VOLUME_SLIDER_WIDTH_DEFAULT = 215;
    public static final int VOLUME_SLIDER_HEIGHT_DEFAULT = 44;
    public static final int VOLUME_BUTTON_WIDTH = (int) (VOLUME_BUTTON_WIDTH_DEFAULT * SCALE);
    public static final int VOLUME_BUTTON_HEIGHT = (int) (VOLUME_BUTTON_HEIGHT_DEFAULT * SCALE);
    public static final int VOLUME_SLIDER_WIDTH = (int) (VOLUME_SLIDER_WIDTH_DEFAULT * SCALE);
    public static final int VOLUME_SLIDER_HEIGHT = (int) (VOLUME_SLIDER_HEIGHT_DEFAULT * SCALE);


    public static final float GRAVITY_FORCE = (float) (0.04 * SCALE);
    public static final int ENTITY_ANIMATION_SPEED = 15;

    public static class LevelEnv {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * SCALE);

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * SCALE);


//        public static final int POTION_RED = 0;
//        public static final int POTION_BLUE = 1;
//        public static final int BARREL = 2;
//        public static final int BOX = 3;

        public static final int POTION_RED_VALUE = 15;
        public static final int POTION_BLUE_VALUE = 10;

        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * SCALE);
        public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * SCALE);

        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * SCALE);

        public static final int POTION_HIT_BOX_WIDTH = (int) (7 * SCALE);
        public static final int POTION_HIT_BOX_HEIGHT = (int) (14 * SCALE);
        public static final int POTION_DRAW_OFFSET_X = (int) (3 * SCALE);
        public static final int POTION_DRAW_OFFSET_Y = (int) (2 * SCALE);

        public static final int BOX_HIT_BOX_WIDTH = (int) (25 * SCALE);
        public static final int BOX_HIT_BOX_HEIGHT = (int) (18 * SCALE);
        public static final int BOX_DRAW_OFFSET_X = (int) (7 * SCALE);
        public static final int BOX_DRAW_OFFSET_Y = (int) (12 * SCALE);

        public static final int BARREL_HIT_BOX_WIDTH = (int) (23 * SCALE);
        public static final int BARREL_HIT_BOX_HEIGHT = (int) (25 * SCALE);
        public static final int BARREL_DRAW_OFFSET_X = (int) (8 * SCALE);
        public static final int BARREL_DRAW_OFFSET_Y = (int) (5 * SCALE);


        public static final int POTION_HOVER_OFFSET_MAX = (int) (7 * SCALE);

    }

    public static class Player {
        public static final float AIR_SPEED = 0f;
        public static final float JUMP_SPEED = (float) (-2.25f * SCALE); // todo think of a better naming
        public static final float POST_COLLISION_FALL_SPEED = (float) (0.5f * SCALE);
        public static final float WALK_SPEED = 1f * SCALE;
        public static final int HIT_BOX_WIDTH = (int) (20 * SCALE);
        public static final int HIT_BOX_HEIGHT = (int) (27 * SCALE);

        // 21, 4 is the offset from 0,0 where the actual character sprite is drawn on a png image
        public static final float DRAW_OFFSET_X = 21 * SCALE;
        public static final float DRAW_OFFSET_Y = 4 * SCALE;

    }

    public static class Enemy {
        public static final int CRAB_SPRITE_WIDTH_DEFAULT = 72;
        public static final int CRAB_SPRITE_HEIGHT_DEFAULT = 32;
        public static final int CRAB_SPRITE_WIDTH = (int) (72 * SCALE);
        public static final int CRAB_SPRITE_HEIGHT = (int) (32 * SCALE);

        // 26 is the pixel difference between the sprite start and the hit box start
        public static final int CRAB_DRAW_OFFSET_X = (int) (26 * SCALE);
        // 9 is the pixel difference between the sprite start and the hit box start
        public static final int CRAB_DRAW_OFFSET_Y = (int) (9 * SCALE);

        public static final float AIR_SPEED = 0f;
        public static final float JUMP_SPEED = (float) (-2.25f * SCALE);
        public static final float POST_COLLISION_FALL_SPEED = (float) (0.5f * SCALE);
        public static final float WALK_SPEED = 0.35f * SCALE;

        public static final int CRAB_HIT_BOT_WIDTH = (int) (22 * Game.SCALE);
        public static final int CRAB_HIT_BOT_HEIGHT = (int) (19 * Game.SCALE);
        public static final int CRAB_ATTACK_RANGE_OFFSET_X = (int) (30 * Game.SCALE);

        public static final int CRAB_DAMAGE = 10;


        public static int getSpriteAmount(EnemyType enemyType, EnemyState enemyState) {
            return switch (enemyType) {
                case CRAB -> switch (enemyState) {
                    case IDLE -> 9;
                    case RUNNING -> 6;
                    case ATTACKING -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                };
            };
        }

        //todo if the sprite animation order is the same for the other enemy, this logic should be put into the EnemyState as a property
        public static int getSpriteAnimationRowIndex(EnemyType enemyType, EnemyState enemyState) {
            return switch (enemyType) {
                case CRAB -> switch (enemyState) {
                    case IDLE -> 0;
                    case RUNNING -> 1;
                    case ATTACKING -> 2;
                    case HIT -> 3;
                    case DEAD -> 4;
                };
            };
        }

        public static int getMaxHealth(EnemyType enemyType) {
            return switch (enemyType) {
                case CRAB -> 10;
            };
        }

        public static int getMaxDmg(EnemyType enemyType) {
            return switch (enemyType) {
                case CRAB -> 15;
            };
        }
    }

    public static class StatusBar {
        public static final int STATUS_BAR_WIDTH = (int) (192 * Game.SCALE);
        public static final int STATUS_BAR_HEIGHT = (int) (58 * Game.SCALE);
        public static final int STATUS_BAR_X = (int) (10 * Game.SCALE);
        public static final int STATUS_BAR_Y = (int) (10 * Game.SCALE);
        public static final int HEALTH_BAR_WIDTH = (int) (150 * Game.SCALE);
        public static final int HEALTH_BAR_HEIGHT = (int) (4 * Game.SCALE);
        public static final int HEALTH_BAR_X_START = (int) (34 * Game.SCALE) + STATUS_BAR_X;
        public static final int HEALTH_BAR_Y_START = (int) (14 * Game.SCALE) + STATUS_BAR_Y;
    }
}
