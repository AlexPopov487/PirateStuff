package org.example.entities;

import org.example.Config;
import org.example.GamePanel;
import org.example.utils.CollisionHelper;
import org.example.utils.Helper;

import static org.example.Config.ENTITY_ANIMATION_SPEED;
import static org.example.Config.GRAVITY_FORCE;
import static org.example.Game.SCALE;

public abstract class Enemy extends Entity {
    protected static final float ATTACK_RANGE = GamePanel.getCurrentTileSize();
    protected static final float VISUAL_RANGE = GamePanel.getCurrentTileSize() * 5;
    private final Heath heath;
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

        getDirections().setMovingLeft(true); // todo for testing
        heath = new Heath(Config.Enemy.getMaxHealth(enemyType));
    }

    public void update(int[][] levelData, Player player) {
        updateBehavior(levelData, player);
        updateAnimationTick();
        updateAttackRange();
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
        heath.subtractHealth(amount);

        if (heath.getCurrentHeath() == 0) {
            enemyState = EnemyState.DEAD;
        } else {
            enemyState = EnemyState.HIT;
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

    public void reset() {
        // reset enemy's initial position
        hitBox.x = x;
        hitBox.y = y;

        isFirstPositionUpdate = true;
        isAttackPerformed = false;

        heath.reset();
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

        return CollisionHelper.isDistanceClearFromObstacle(levelData, hitBox, player.getHitBox(), enemyTileY);
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
}
