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
        super(x, y, Config.Enemy.Crabby.SPRITE_WIDTH, Config.Enemy.Crabby.SPRITE_HEIGHT, EnemyType.CRAB);
        initHitBox(Config.Enemy.Crabby.HIT_BOX_WIDTH, Config.Enemy.Crabby.HIT_BOX_HEIGHT);
        initAttackRange();
    }

    @Override
    public void updateAttackRange() {
        attackRange.x = hitBox.x - Config.Enemy.Crabby.ATTACK_RANGE_OFFSET_X;
        attackRange.y = hitBox.y;
    }

    @Override
    public void initAttackRange() {
        attackRange = new Rectangle2D.Float(x, y, 82 * Game.SCALE, Config.Enemy.Crabby.HIT_BOX_HEIGHT);
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

        doPatrol(levelData);
    }
}
