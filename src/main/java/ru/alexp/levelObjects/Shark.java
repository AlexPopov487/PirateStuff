package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.entities.Directions;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

import static ru.alexp.utils.CollisionHelper.*;

public class Shark extends LevelObject implements Drawable {
    private final Directions directions;
    private final HoverEffect hoverEffect;


    public Shark(float x, float y) {
        super(x, y, LevelObjectType.SHARK, false);
        initHitBox(Config.LevelEnv.SHARK_WIDTH, Config.LevelEnv.SHARK_HEIGHT);
        hoverEffect = new HoverEffect();
        directions = new Directions();
        directions.setMovingRight(true);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
    }

    // USED INSTEAD OF UPDATE
    public void updatePosition(int[][] levelData) {
        hoverEffect.update(Config.LevelEnv.SHARK_HOVER_OFFSET_MAX, Config.LevelEnv.SHARK_HOVER_INC_STEP, hitBox, y);

        float xStep = 0;

        if (directions.isMovingLeft()) {
            xStep = -Config.Enemy.WALK_SPEED;
        }
        if (directions.isMovingRight()) {
            xStep = Config.Enemy.WALK_SPEED;
        }

        /*
         passing y instead of hitbox.y here because hitbox.y varies all the time due to hover effect,
         whereas y value remains intact
        */
        if (canMoveHere(hitBox.x + xStep, y, hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xStep;
        } else {
            // if enemy has reached either wall or edge, change patrol direction
            changeWalkingDir();
        }


    }

    public Directions getDirections() {
        return directions;
    }

    public int getXFlip() {
        if (getDirections().isMovingLeft()) {
            return (int) hitBox.width;
        } else {
            return 0;
        }
    }

    public int getWidthFlip() {
        if (getDirections().isMovingLeft()) {
            return -1;
        } else {
            return 1;
        }
    }

    private void changeWalkingDir() {
        if (directions.isMovingLeft()) {
            directions.setMovingLeft(false);
            directions.setMovingRight(true);
        } else {
            directions.setMovingLeft(true);
            directions.setMovingRight(false);
        }
    }
}
