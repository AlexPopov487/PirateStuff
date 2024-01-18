package org.example.entities;

import org.example.Config;
import org.example.GamePanel;

import static org.example.Game.SCALE;
import static org.example.utils.CollisionHelper.isDistanceClearFromObstacle;

public abstract class Enemy extends Entity {
    protected final static float ENEMY_SPEED = 0.35f * SCALE;
    protected static final float ATTACK_RANGE = GamePanel.getCurrentTileSize();
    protected static final float VISUAL_RANGE = GamePanel.getCurrentTileSize() * 5;
    private boolean isActive = true;
    private boolean isAttackPerformed = false;
    private int animationIndex;
    protected EnemyState enemyState;
    protected EnemyType enemyType;
    private final Heath heath;
    private int animationTick;
    private int animationSpeed = 15; // todo convert to global variable for all entities
    protected boolean isFirstPositionUpdate = true;
    protected final Directions directions;
    protected final GravitySettings gravitySettings;
    protected int enemyTileY; // is necessary to determine, whether enemy and the player are at the same y position

    public Enemy(float x, float y, int width, int height, EnemyType enemyType) {
        super(x, y, width, height);
        directions = new Directions();
        gravitySettings = new GravitySettings(0f, 0.04f * SCALE, -2.25f * SCALE, 0.5f * SCALE);
//        initHitBox(x, y, width, height);
        this.enemyType = enemyType;
        this.enemyState = EnemyState.IDLE;

        directions.setMovingLeft(true); // todo for testing
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
        directions.reset();
        directions.setMoving(true);
        directions.setMovingLeft(true);
        resetGravitySettings();
        initAttackRange();
    }

    private void resetGravitySettings(){// todo move values to constants
        gravitySettings.setAirSpeed(0f);
        gravitySettings.setGravityForce(0.04f * SCALE);
        gravitySettings.setJumpSpeed(-2.25f * SCALE);
        gravitySettings.setPostCollisionFallSpeed(0.5f * SCALE);
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= animationSpeed) {
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
        ;

        return isDistanceClearFromObstacle(levelData, hitBox, player.getHitBox(), enemyTileY);
    }

    protected void moveTowardsPlayer(Player player) {
        if (player.getHitBox().x > hitBox.x) {
            directions.setMovingLeft(false);
            directions.setMovingRight(true);
        } else {
            directions.setMovingLeft(true);
            directions.setMovingRight(false);
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
