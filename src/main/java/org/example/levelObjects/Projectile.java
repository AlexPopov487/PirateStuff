package org.example.levelObjects;

import org.example.Config;
import org.example.entities.Directions;
import org.example.types.LevelObjectType;

public class Projectile extends LevelObject {
    private Directions directions;

    public Projectile(float x, float y, LevelObjectType levelObjectType, LevelObjectType parentObjectType) {
        super(getXWitOffset(x, parentObjectType),
                y + Config.LevelEnv.CANNON_BALL_OFFSET_Y,
                levelObjectType,
                false);
        initHitBox(Config.LevelEnv.CANNON_BALL_HIT_BOX_WIDTH, Config.LevelEnv.CANNON_BALL_HIT_BOX_HEIGHT);
        this.directions = new Directions();

        if (LevelObjectType.CANNON_RIGHT.equals(parentObjectType)) {
            directions.setMovingRight(true);
        } else if (LevelObjectType.CANNON_LEFT.equals(parentObjectType)) {
            directions.setMovingLeft(true);
        }

    }

    public void updatePosition() {
        int directionMultiplier;
        if (directions.isMovingRight()) {
            directionMultiplier = 1;
        } else {
            directionMultiplier = -1;
        }

        hitBox.x += directionMultiplier * Config.LevelEnv.CANNON_BALL_SPEED;
    }

    public void setPosition(int x, int y) {
        hitBox.x = x;
        hitBox.y = y;
    }

    public void setDirections(Directions directions) {
        this.directions = directions;
    }

    private static float getXWitOffset(float x, LevelObjectType parentObjectType) {
        if (LevelObjectType.CANNON_RIGHT.equals(parentObjectType)) {
            x += Config.LevelEnv.CANNON_BALL_OFFSET_X_RIGHT;
        } else if (LevelObjectType.CANNON_LEFT.equals(parentObjectType)) {
            x += Config.LevelEnv.CANNON_BALL_OFFSET_X_LEFT;
        }
        return x;
    }
}
