package org.example;


import org.example.types.EnemyState;
import org.example.types.EnemyType;

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

    public static final int DIALOGUE_DEFAULT_WIDTH = 14;
    public static final int DIALOGUE_DEFAULT_HEIGHT = 12;
    public static final int DIALOGUE_WIDTH = (int) (DIALOGUE_DEFAULT_WIDTH * Game.SCALE);
    public static final int DIALOGUE_HEIGHT = (int) (DIALOGUE_DEFAULT_HEIGHT * Game.SCALE);


    public static final float GRAVITY_FORCE = (float) (0.04 * SCALE);
    public static final int ENTITY_ANIMATION_SPEED = 15;

    public static class LevelEnv {
        /*Corresponds to the indexes in int[][] levelData array.*/
        public static final int TILE_VOID_INDEX = 11;
        public static final int TILE_WATER_TOP_INDEX = 48;
        public static final int TILE_WATER_BOTTOM_INDEX = 49;


        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * SCALE);

        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * SCALE);



        public static final int CONTAINER_WIDTH_DEFAULT = 40;
        public static final int CONTAINER_HEIGHT_DEFAULT = 30;
        public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * SCALE);
        public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * SCALE);
        public static final int BOX_HIT_BOX_WIDTH = (int) (25 * SCALE);
        public static final int BOX_HIT_BOX_HEIGHT = (int) (18 * SCALE);
        public static final int BOX_DRAW_OFFSET_X = (int) (7 * SCALE);
        public static final int BOX_DRAW_OFFSET_Y = (int) (12 * SCALE);
        public static final int BARREL_HIT_BOX_WIDTH = (int) (23 * SCALE);
        public static final int BARREL_HIT_BOX_HEIGHT = (int) (25 * SCALE);
        public static final int BARREL_DRAW_OFFSET_X = (int) (8 * SCALE);
        public static final int BARREL_DRAW_OFFSET_Y = (int) (5 * SCALE);


        public static final int POTION_WIDTH_DEFAULT = 12;
        public static final int POTION_HEIGHT_DEFAULT = 16;
        public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * SCALE);
        public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * SCALE);
        public static final int POTION_HIT_BOX_WIDTH = (int) (7 * SCALE);
        public static final int POTION_HIT_BOX_HEIGHT = (int) (14 * SCALE);
        public static final int POTION_DRAW_OFFSET_X = (int) (3 * SCALE);
        public static final int POTION_DRAW_OFFSET_Y = (int) (2 * SCALE);
        public static final int POTION_RED_VALUE = 15;
        public static final int POTION_BLUE_VALUE = 100;
        public static final int POTION_HOVER_OFFSET_MAX = (int) (7 * SCALE);
        public static final float POTION_HOVER_INC_STEP = 0.075f;


        public static final int SPIKE_WIDTH_DEFAULT = 32;
        public static final int SPIKE_HEIGHT_DEFAULT = 32;
        public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * SCALE);
        public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * SCALE);
        public static final int SPIKE_HIT_BOX_WIDTH = (int) (32 * SCALE);
        public static final int SPIKE_HIT_BOX_HEIGHT = (int) (16 * SCALE);
        public static final int SPIKE_DRAW_OFFSET_X = 0;
        public static final int SPIKE_DRAW_OFFSET_Y = (int) (16 * SCALE);

        public static final int CANNON_WIDTH_DEFAULT = 40;
        public static final int CANNON_HEIGHT_DEFAULT = 26;
        public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * SCALE);
        public static final int CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * SCALE);
        public static final int CANNON_HIT_BOX_WIDTH = (int) (40 * SCALE);
        public static final int CANNON_HIT_BOX_HEIGHT = (int) (26 * SCALE);


        public static final int CANNON_BALL_WIDTH_DEFAULT = 15;
        public static final int CANNON_BALL_HEIGHT_DEFAULT = 15;
        public static final int CANNON_BALL_WIDTH = (int) (CANNON_BALL_WIDTH_DEFAULT * SCALE);
        public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_HEIGHT_DEFAULT * SCALE);
        public static final int CANNON_BALL_HIT_BOX_WIDTH = (int) (40 * SCALE);
        public static final int CANNON_BALL_HIT_BOX_HEIGHT = (int) (26 * SCALE);

        // offsets are needed to tune up the cannon ball spawn point exactly to the end of the cannon barrel
        public static final int CANNON_BALL_OFFSET_X_LEFT = (int) (-3 * SCALE);
        public static final int CANNON_BALL_OFFSET_X_RIGHT = (int) (29 * SCALE);
        public static final int CANNON_BALL_OFFSET_Y = (int) (5 * SCALE);
        public static final float CANNON_BALL_SPEED =  (0.75f * SCALE);



        public static final int GRASS_WIDTH_DEFAULT = 32;
        public static final int GRASS_HEIGHT_DEFAULT = 32;
        public static final int GRASS_WIDTH = (int) (GRASS_WIDTH_DEFAULT * SCALE);
        public static final int GRASS_HEIGHT = (int) (GRASS_HEIGHT_DEFAULT * SCALE);


        public static final int TREE_STRAIGHT_WIDTH_DEFAULT = 39;
        public static final int TREE_STRAIGHT_HEIGHT_DEFAULT = 92;
        public static final int BACK_TREE_STRAIGHT_HEIGHT_DEFAULT = 64;
        public static final int TREE_STRAIGHT_WIDTH = (int) (TREE_STRAIGHT_WIDTH_DEFAULT * SCALE);
        public static final int TREE_STRAIGHT_HEIGHT = (int) (TREE_STRAIGHT_HEIGHT_DEFAULT * SCALE);
        public static final int BACK_TREE_STRAIGHT_HEIGHT = (int) (BACK_TREE_STRAIGHT_HEIGHT_DEFAULT * SCALE);


        public static final int TREE_BEND_WIDTH_DEFAULT = 62;
        public static final int TREE_BEND_HEIGHT_DEFAULT = 54;
        public static final int TREE_BEND_WIDTH = (int) (TREE_BEND_WIDTH_DEFAULT * SCALE);
        public static final int TREE_BEND_HEIGHT = (int) (TREE_BEND_HEIGHT_DEFAULT * SCALE);


        public static final int FLAG_WIDTH_DEFAULT = 50;
        public static final int FLAG_HEIGHT_DEFAULT = 109;
        public static final int FLAG_WIDTH = (int) (FLAG_WIDTH_DEFAULT * SCALE * 1.1);
        public static final int FLAG_HEIGHT = (int) (FLAG_HEIGHT_DEFAULT * SCALE * 1.1);


        public static final int SHARK_WIDTH_DEFAULT = 122;
        public static final int SHARK_HEIGHT_DEFAULT = 32;
        public static final int SHARK_WIDTH = (int) ((SHARK_WIDTH_DEFAULT * 0.75) * SCALE);
        public static final int SHARK_HEIGHT = (int) ((SHARK_HEIGHT_DEFAULT) * SCALE);
        // 2 is the pixel difference between the sprite start and the hit box start
        public static final int SHARK_DRAW_OFFSET_X = (int) (2 * SCALE);
        // 2 is the pixel difference between the sprite start and the hit box start
        public static final int SHARK_DRAW_OFFSET_Y = (int) (2 * SCALE);
        public static final int SHARK_HOVER_OFFSET_MAX = (int) (3 * SCALE);
        public static final float SHARK_HOVER_INC_STEP = 0.045f;

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


        public static final int POWER_ATTACK_STAMINA_COST = 100;

    }

    public static class Enemy {
        public static class Crabby {
            public static final int SPRITE_WIDTH_DEFAULT = 72;
            public static final int SPRITE_HEIGHT_DEFAULT = 32;
            public static final int SPRITE_WIDTH = (int) (72 * SCALE);
            public static final int SPRITE_HEIGHT = (int) (32 * SCALE);
            // 26 is the pixel difference between the sprite start and the hit box start
            public static final int DRAW_OFFSET_X = (int) (26 * SCALE);
            // 9 is the pixel difference between the sprite start and the hit box start
            public static final int DRAW_OFFSET_Y = (int) (9 * SCALE);
            public static final int HIT_BOX_WIDTH = (int) (22 * SCALE);
            public static final int HIT_BOX_HEIGHT = (int) (19 * SCALE);
            public static final int ATTACK_RANGE_OFFSET_X = (int) (30 * SCALE);
        }

        public static class Star {
            public static final int SPRITE_WIDTH_DEFAULT = 34;
            public static final int SPRITE_HEIGHT_DEFAULT = 30;
            public static final int SPRITE_WIDTH = (int) (SPRITE_WIDTH_DEFAULT * Game.SCALE);
            public static final int SPRITE_HEIGHT = (int) (SPRITE_HEIGHT_DEFAULT * Game.SCALE);
            public static final int HIT_BOX_WIDTH = (int) (17 * Game.SCALE);
            public static final int HIT_BOX_HEIGHT = (int) (21 * Game.SCALE);
            public static final int DRAW_OFFSET_X = (int) (9 * Game.SCALE);
            public static final int DRAW_OFFSET_Y = (int) (7 * Game.SCALE);

            public static final long ROLLING_DURATION = 2000;
            public static final long STUNNED_DURATION = 2000;
            public static final float ROLLING_SPEED = Config.Enemy.WALK_SPEED * 3;
        }

        
        public static final float AIR_SPEED = 0f;
        public static final float JUMP_SPEED = (float) (-2.25f * SCALE);
        public static final float POST_COLLISION_FALL_SPEED = (float) (0.5f * SCALE);
        public static final float WALK_SPEED = 0.35f * SCALE;


        public static int getSpriteAmount(EnemyType enemyType, EnemyState enemyState) {
            return switch (enemyType) {
                case CRAB -> switch (enemyState) {
                    case IDLE -> 9;
                    case RUNNING -> 6;
                    case ATTACKING -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                };
                case STAR -> switch (enemyState) {
                    case IDLE -> 8;
                    case RUNNING -> 6;
                    case ATTACKING -> 7;
                    case HIT -> 4;
                    case DEAD -> 5;
                };
            };
        }

        public static int getMaxHealth(EnemyType enemyType) {
            return switch (enemyType) {
                case CRAB -> 10;
                case STAR -> 0;
            };
        }

        public static int getMaxDmg(EnemyType enemyType) {
            return switch (enemyType) {
                case CRAB -> 15;
                case STAR -> 25;
            };
        }
    }

    public static class StatusBar {
        public static final int STATUS_BAR_WIDTH = (int) (192 * SCALE);
        public static final int STATUS_BAR_HEIGHT = (int) (58 * SCALE);
        public static final int STATUS_BAR_X = (int) (10 * SCALE);
        public static final int STATUS_BAR_Y = (int) (10 * SCALE);
        public static final int HEALTH_BAR_WIDTH = (int) (150 * SCALE);
        public static final int HEALTH_BAR_HEIGHT = (int) (4 * SCALE);
        public static final int HEALTH_BAR_X_START = (int) (34 * SCALE) + STATUS_BAR_X;
        public static final int HEALTH_BAR_Y_START = (int) (14 * SCALE) + STATUS_BAR_Y;

        public static final int STAMINA_BAR_WIDTH = (int) (104 * SCALE);
        public static final int STAMINA_BAR_HEIGHT = (int) (2 * SCALE);
        public static final int STAMINA_BAR_X_START = (int) (44 * SCALE) + STATUS_BAR_X;
        public static final int STAMINA_BAR_Y_START = (int) (34 * SCALE) + STATUS_BAR_Y;
        public static final int STAMINA_RECOVERY_DELAY = 20;
        public static final int STAMINA_MAX_VALUE = 200;

    }

    public static class Audio {
        public static int MENU_SONG_INDEX = 0;
        public static int LEVEL_1_SONG_INDEX = 1;
        public static int LEVEL_2_SONG_INDEX = 2;

        public static int DIE_EFFECT_INDEX = 0;
        public static int JUMP_EFFECT_INDEX = 1;
        public static int GAME_OVER_EFFECT_INDEX = 2;
        public static int LEVEL_COMPLETED_EFFECT_INDEX = 3;
        public static int ATTACK_1_EFFECT_INDEX = 4;
        public static int ATTACK_2_EFFECT_INDEX = 5;
        public static int ATTACK_3_EFFECT_INDEX = 6;
        public static int CANNON_EFFECT_INDEX = 7;
        public static int POISON_FOUND_EFFECT_INDEX = 8;
        public static int HIT_EFFECT_INDEX = 9;
//        public static int PLAYER_HURT_EFFECT_INDEX = 10;
    }
}
