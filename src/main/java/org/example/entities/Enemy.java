package org.example.entities;

import org.example.Config;
import org.example.GamePanel;
import org.example.types.EnemyState;
import org.example.types.EnemyType;
import org.example.types.EntityType;
import org.example.utils.CollisionHelper;
import org.example.utils.Helper;

import static org.example.Config.ENTITY_ANIMATION_SPEED;
import static org.example.utils.CollisionHelper.*;

public abstract class Enemy extends Entity {
    protected static final float ATTACK_RANGE = GamePanel.getCurrentTileSize();
    protected static final float VISUAL_RANGE = GamePanel.getCurrentTileSize() * 5;
    private final Health health;
    private boolean isActive = true;
    private boolean isAttackPerformed = false;
    protected EnemyState enemyState;
    protected EnemyType enemyType;
    protected boolean isFirstPositionUpdate = true;
    protected int enemyTileY; // is necessary to determine, whether enemy and the player are at the same y position

    public Enemy(float x, float y, int width, int height, EnemyType enemyType) {
        super(x, y, width, height, Helper.generateGravitySettingForEntity(EntityType.ENEMY), Config.Enemy.WALK_SPEED);
        this.enemyType = enemyType;
        this.enemyState = EnemyState.IDLE;

        getDirections().setMovingLeft(true);
        health = new Health(Config.Enemy.getMaxHealth(enemyType));
    }

    public void update(int[][] levelData, Player player) {
        updateBehavior(levelData, player);
        updateAnimationTick();
        updateAttackRange();
    }

    public void placeEnemyAtTheFloor(int[][] levelData) {
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

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public EnemyState getEnemyState() {
        return enemyState;
    }

    public void setEnemyState(EnemyState enemyState) {
        this.enemyState = enemyState;
    }

    public void takeDamage(int amount) {
        health.subtractHealth(amount);

        if (health.getCurrentHealth() == 0) {
            changeEnemyStateTo(EnemyState.DEAD);
        } else {
            changeEnemyStateTo(EnemyState.HIT);
        }
    }

    protected void attack(Player player) {
        if (attackRange.intersects(player.hitBox)){
            player.getHeath().subtractHealth(Config.Enemy.getMaxDmg(enemyType));
            setAttackPerformed(true);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAttackPerformed() {
        return isAttackPerformed;
    }

    public void setAttackPerformed(boolean attackPerformed) {
        isAttackPerformed = attackPerformed;
    }

    protected void handleFirstUpdate(int[][] levelData) {
        if (!CollisionHelper.isOnTheFloor(hitBox, levelData)) {
            getDirections().setInAir(true);
        }
        isFirstPositionUpdate = false;
    }


    protected int getXFlip() {
        if (getDirections().isMovingLeft()) {
            return 0;
        } else {
            return width;
        }
    }

    protected int getWidthFlip() {
        if (getDirections().isMovingLeft()) {
            return 1;
        } else {
            return -1;
        }
    }

    public void reset() {
        // reset enemy's initial position
        hitBox.x = x;
        hitBox.y = y;

        isFirstPositionUpdate = true;
        isAttackPerformed = false;

        health.reset();
        changeEnemyStateTo(EnemyState.IDLE);
        isActive = true;
        getDirections().reset();
        getDirections().setMoving(true);
        getDirections().setMovingLeft(true);
        resetGravitySettings();
        initAttackRange();
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ENTITY_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= Config.Enemy.getSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;

                // protect from spawning repeated attack animations when player is nearby
                if (EnemyState.ATTACKING.equals(enemyState)) {
                    changeEnemyStateTo(EnemyState.IDLE);
                } else if (EnemyState.HIT.equals(enemyState)) {
                    enemyState = EnemyState.IDLE;
                } else if (EnemyState.DEAD.equals(enemyState)) {
                    isActive = false;
                }
            }
        }
    }

    protected abstract void updateBehavior(int[][] levelData, Player player);

    protected void changeEnemyStateTo(EnemyState enemyState) {
        this.enemyState = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitBox().y / GamePanel.getCurrentTileSize());

        if (enemyTileY != playerTileY) return false;

        if (!isPlayerInVisualRange(player)) return false;

        return CollisionHelper.isDistanceClearFromObstacle(levelData, hitBox, player.getHitBox(), enemyTileY, true);
    }

    protected void doPatrol(int[][] levelData) {
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

    protected void moveTowardsPlayer(Player player) {
        if (player.getHitBox().x > hitBox.x) {
            getDirections().setMovingLeft(false);
            getDirections().setMovingRight(true);
        } else {
            getDirections().setMovingLeft(true);
            getDirections().setMovingRight(false);
        }
    }

    protected boolean isPlayerInAttackingRange(Player player) {
        // returns a distance between 2 points (always positive)
        float distanceToPlayer = Math.abs(player.hitBox.x - hitBox.x);
        return distanceToPlayer <= ATTACK_RANGE;
    }

    private boolean isPlayerInVisualRange(Player player) {
        // returns a distance between 2 points (always positive)
        float distanceToPlayer = Math.abs(player.hitBox.x - hitBox.x);
        return distanceToPlayer <= VISUAL_RANGE;
    }

    protected void setEnemyTileY(int enemyTileY) {
        this.enemyTileY = enemyTileY;
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
}
