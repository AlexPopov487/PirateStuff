package ru.alexp.entities;

import ru.alexp.Config;
import ru.alexp.types.EnemyState;
import ru.alexp.types.EnemyType;

import static ru.alexp.utils.CollisionHelper.canMoveHere;
import static ru.alexp.utils.CollisionHelper.isFloor;

public class PinkStar extends Enemy {
    private RollingState rollingState = RollingState.NONE;
    private Long rollingStartTime = null;
    private Long stunnedStartTime = null;
    private Integer currentRollingDirection = null;

    public PinkStar(float x, float y) {
        super(x, y, Config.Enemy.Star.SPRITE_WIDTH, Config.Enemy.Star.SPRITE_HEIGHT, EnemyType.STAR);
        initHitBox(Config.Enemy.Star.HIT_BOX_WIDTH, Config.Enemy.Star.HIT_BOX_HEIGHT);
        initAttackRange();
    }

    @Override
    public void initAttackRange() {
        attackRange = hitBox;
    }

    @Override
    public void updateAttackRange() {
        attackRange.x = hitBox.x;
        attackRange.y = hitBox.y;
    }

    public boolean isStunned() {
        return RollingState.STUNNED.equals(rollingState);
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
            case IDLE -> {
                if (RollingState.NONE.equals(rollingState)) {
                    changeEnemyStateTo(EnemyState.RUNNING);
                } else {
                    changeEnemyStateTo(EnemyState.ATTACKING);
                }
            }
            case RUNNING -> setRunningBehavior(levelData, player);
            case ATTACKING -> handleRollingStates(levelData, player);
            case HIT -> {

            }
            case DEAD -> {
            }
        }
    }

    private void handleRollingStates(int[][] levelData, Player player) {
        switch (rollingState) {
            case NONE -> {
                if (getAnimationIndex() < 3) return;

                if (getAnimationIndex() == 3) {
                    rollingState = RollingState.ROLLING;
                    rollingStartTime = System.currentTimeMillis();
                }
            }
            case ROLLING -> {
                // loop the animation while isRolling
                if (getAnimationIndex() < 3) {
                    setAnimationIndex(3);
                }

                if (currentRollingDirection != null && currentRollingDirection == 1) {
                    getDirections().moveRight();
                } else if (currentRollingDirection != null && currentRollingDirection == 0) {
                    getDirections().moveLeft();
                }

                rollUntilObstacleMet(levelData, player);
            }
            case STUNNED -> {
                if (getAnimationIndex() >= 3) {
                    setAnimationIndex(0);
                }

                if (stunnedStartTime == null) {
                    stunnedStartTime = System.currentTimeMillis();
                    rollingState = RollingState.STUNNED;
                    return;
                }

                if (isStunnedDurationExceeded()) {
                    stunnedStartTime = null;
                    rollingState = RollingState.NONE;
                    setAttackPerformed(false);
                    changeEnemyStateTo(EnemyState.IDLE);
                }

            }
        }
    }

    private void rollUntilObstacleMet(int[][] levelData, Player player) {
        float xStep = 0;

        if (getDirections().isMovingLeft()) {
            xStep = -getWalkSpeed();
        }
        if (getDirections().isMovingRight()) {
            xStep = getWalkSpeed();
        }

        if (canMoveHere(hitBox.x + xStep, hitBox.y, hitBox.width, hitBox.height, levelData)
                && isFloor(hitBox, xStep, levelData)
                && !isRollingDurationExceeded()) {
            hitBox.x += xStep;

            if (!isAttackPerformed()) {
                attack(player);
            }
        } else {
            rollingState = RollingState.STUNNED;
            rollingStartTime = null;
            currentRollingDirection = null;
            setWalkSpeed(Config.Enemy.WALK_SPEED);
        }
    }

    private boolean isRollingDurationExceeded() {
        return rollingStartTime != null && System.currentTimeMillis() - rollingStartTime >= Config.Enemy.Star.ROLLING_DURATION;
    }

    private boolean isStunnedDurationExceeded() {
        return stunnedStartTime != null && System.currentTimeMillis() - stunnedStartTime >= Config.Enemy.Star.STUNNED_DURATION;
    }

    private void setRunningBehavior(int[][] levelData, Player player) {
        if (canSeePlayer(levelData, player)) {
            rollTowardsPlayer(player);
            return;
        }

        doPatrol(levelData);
    }

    private void rollTowardsPlayer(Player player) {
        setWalkSpeed(Config.Enemy.Star.ROLLING_SPEED);
        if (player.getHitBox().x > hitBox.x) {
            currentRollingDirection = 1;
        } else {
            currentRollingDirection = 0;
        }

        setEnemyState(EnemyState.ATTACKING);
    }

    private enum RollingState {
        NONE,
        ROLLING,
        STUNNED,
    }
}
