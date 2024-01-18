package org.example.entities;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.utils.CollisionHelper;

import java.awt.geom.Rectangle2D;

import static org.example.utils.CollisionHelper.*;

public class Crabby extends Enemy {
    private static final int HIT_BOT_WIDTH = 22;
    private static final int HIT_BOT_HEIGHT = 19;
    private static final int ATTACK_RANGE_OFFSET_X = (int) (30 * Game.SCALE);


    public Crabby(float x, float y) {
        super(x, y, Config.Enemy.CRAB_SPRITE_WIDTH, Config.Enemy.CRAB_SPRITE_HEIGHT, EnemyType.CRAB);
        initHitBox(x, y, (int) (HIT_BOT_WIDTH * Game.SCALE), (int) (HIT_BOT_HEIGHT * Game.SCALE));
        initAttackRange();
    }

    public int getXFlip() {
        if (directions.isMovingLeft()) {
            return 0;
        } else {
            return width;
        }
    }

    public int getWidthFlip() {
        if (directions.isMovingLeft()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public void updateAttackRange() {
        attackRange.x = hitBox.x - ATTACK_RANGE_OFFSET_X;
        attackRange.y = hitBox.y;
    }

    @Override
    public void initAttackRange() {
        attackRange = new Rectangle2D.Float(x, y, 82 * Game.SCALE, HIT_BOT_HEIGHT * Game.SCALE);
    }

    @Override
    protected void updateBehavior(int[][] levelData, Player player) {
        if (isFirstPositionUpdate) {
            handleFirstUpdate(levelData);
        }

        if (directions.isInAir()) {
            placeEnemyAtTheFloor(levelData);
            return;
        }

        switch (enemyState) {
            case IDLE -> changeEnemyStateTo(EnemyState.RUNNING);
            case RUNNING -> setRunningBehavior(levelData, player);
            case ATTACKING -> {
                /*
                 the attack is registered only at specific animation index (when it looks like an enemy lands a strike)
                 and only once per attack animation
                */
                if (getAnimationIndex() == 0) setAttackPerformed(false);
                if (getAnimationIndex() != 3 || isAttackPerformed()) return;

                attack(player);
            }
            case HIT -> {
            }
            case DEAD -> {
            }
        }
    }

    private void setRunningBehavior(int[][] levelData, Player player) {

        if (canSeePlayer(levelData, player)) {
            moveTowardsPlayer(player);
        }

        if (isPlayerInAttackingRange(player)) {
            changeEnemyStateTo(EnemyState.ATTACKING);
        }

        float xDestination = 0;

        if (directions.isMovingLeft()) {
            xDestination = -ENEMY_SPEED;
        }
        if (directions.isMovingRight()) {
            xDestination = ENEMY_SPEED;
        }

        if (canMoveHere(hitBox.x + xDestination, hitBox.y, hitBox.width, hitBox.height, levelData)
                && isFloor(hitBox, xDestination, levelData, directions.isMovingLeft())) {
            hitBox.x += xDestination;
        } else {
            // if enemy has reached either wall or edge, change patrol direction
            changeWalkingDir();
        }
    }

    private void handleFirstUpdate(int[][] levelData) {
        if (!CollisionHelper.isOnTheFloor(hitBox, levelData)) {
            directions.setInAir(true);
        }
        isFirstPositionUpdate = false;
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

    private void placeEnemyAtTheFloor(int[][] levelData) {
        float yDestination = hitBox.y + gravitySettings.getAirSpeed();

        if (CollisionHelper.canMoveHere(hitBox.x, yDestination, hitBox.width, hitBox.height, levelData)) {
            hitBox.y = yDestination;
            gravitySettings.setAirSpeed(gravitySettings.getAirSpeed() + gravitySettings.getGravityForce());
        } else {
        /*
         if we assume that updating position will lead to collision, the pos update will not be made,
         meaning that there still will be some space between the player and the obstacle. Thus, we need to
         move the player as close to the obstacle as possible
        */
            directions.setInAir(false);
            hitBox.y = getClosestToObstacleYPos(hitBox, yDestination);
        }

        setEnemyTileY((int) (hitBox.getY() / GamePanel.getCurrentTileSize()));
    }

}
