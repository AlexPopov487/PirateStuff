package org.example.entities;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.types.EnemyState;
import org.example.types.EnemyType;
import org.example.utils.CollisionHelper;

import java.awt.geom.Rectangle2D;

import static org.example.utils.CollisionHelper.*;

public class Crabby extends Enemy {
    public Crabby(float x, float y) {
        super(x, y, Config.Enemy.CRAB_SPRITE_WIDTH, Config.Enemy.CRAB_SPRITE_HEIGHT, EnemyType.CRAB);
        initHitBox(Config.Enemy.CRAB_HIT_BOT_WIDTH, Config.Enemy.CRAB_HIT_BOT_HEIGHT);
        initAttackRange();
    }

    public int getXFlip() {
        if (getDirections().isMovingLeft()) {
            return 0;
        } else {
            return width;
        }
    }

    public int getWidthFlip() {
        if (getDirections().isMovingLeft()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public void updateAttackRange() {
        attackRange.x = hitBox.x - Config.Enemy.CRAB_ATTACK_RANGE_OFFSET_X;
        attackRange.y = hitBox.y;
    }

    @Override
    public void initAttackRange() {
        attackRange = new Rectangle2D.Float(x, y, 82 * Game.SCALE, Config.Enemy.CRAB_HIT_BOT_HEIGHT);
    }

    @Override
    protected void updateBehavior(int[][] levelData, Player player) {
        if (isFirstPositionUpdate) {
            handleFirstUpdate(levelData);
        }

        if (getDirections().isInAir()) {
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

            if (isPlayerInAttackingRange(player)) {
                changeEnemyStateTo(EnemyState.ATTACKING);
            }
        }

        float xStep = 0;

        if (getDirections().isMovingLeft()) {
            xStep = -getWalkSpeed();
        }
        if (getDirections().isMovingRight()) {
            xStep = getWalkSpeed();
        }

        if (canMoveHere(hitBox.x + xStep, hitBox.y, hitBox.width, hitBox.height, levelData)
                && isFloor(hitBox, xStep, levelData)) {
            hitBox.x += xStep;
        } else {
            // if enemy has reached either wall or edge, change patrol direction
            changeWalkingDir();
        }
    }

    private void handleFirstUpdate(int[][] levelData) {
        if (!CollisionHelper.isOnTheFloor(hitBox, levelData)) {
            getDirections().setInAir(true);
        }
        isFirstPositionUpdate = false;
    }


    private void changeWalkingDir() {
        if (getDirections().isMovingLeft()) {
            getDirections().setMovingLeft(false);
            getDirections().setMovingRight(true);
        } else {
            getDirections().setMovingLeft(true);
            getDirections().setMovingRight(false);
        }
    }

    private void placeEnemyAtTheFloor(int[][] levelData) {
        float yDestination = hitBox.y + getGravitySettings().getAirSpeed();

        if (CollisionHelper.canMoveHere(hitBox.x, yDestination, hitBox.width, hitBox.height, levelData)) {
            hitBox.y = yDestination;
            getGravitySettings().setAirSpeed(getGravitySettings().getAirSpeed() + getGravitySettings().getGravityForce());
        } else {
        /*
         if we assume that updating position will lead to collision, the pos update will not be made,
         meaning that there still will be some space between the player and the obstacle. Thus, we need to
         move the player as close to the obstacle as possible
        */
            getDirections().setInAir(false);
            hitBox.y = getClosestToObstacleYPos(hitBox, yDestination);
        }

        setEnemyTileY((int) (hitBox.getY() / GamePanel.getCurrentTileSize()));
    }

}
