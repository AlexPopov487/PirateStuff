package org.example.entities;

import org.example.Config;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.example.Config.GRAVITY_FORCE;

public abstract class Entity extends BaseEntity {
    protected int animationTick;
    protected int animationIndex;
    private final Directions directions;
    private final GravitySettings gravitySettings;
    private final Health health;
    private final float walkSpeed;


    protected Rectangle2D.Float hitBox;

    // box that dynamically changes its position depending on player's pos; enemies will take damage, when inside the area
    protected Rectangle2D.Float attackRange;


    public Entity(float x, float y, int width, int height, GravitySettings gravitySettings, float walkSpeed) {
        super(x, y, width, height);
        this.walkSpeed = walkSpeed;
        this.directions = new Directions();
        this.gravitySettings = gravitySettings;
        this.health = new Health(100);
    }

    protected void updateHitBox() {
        hitBox.x = (int) x;
        hitBox.y = (int) y;
    }

    // FOR DEBUGGING
    protected void drawHitBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    // FOR DEBUGGING
    protected void drawAttackRangeBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) attackRange.x - xLevelOffset, (int) attackRange.y, (int) attackRange.width, (int) attackRange.height);
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    protected void resetGravitySettings(){
        getGravitySettings().setAirSpeed(Config.Enemy.AIR_SPEED);
        getGravitySettings().setGravityForce(GRAVITY_FORCE);
        getGravitySettings().setJumpSpeed(Config.Enemy.JUMP_SPEED);
        getGravitySettings().setPostCollisionFallSpeed(Config.Enemy.POST_COLLISION_FALL_SPEED);
    }

    public abstract void initAttackRange();
    /*
   attack range should follow the direction of the player
   attack range box position is intentionally not reset, i.e. even in neither of player's directions are set
   (e.g. the player is standing on the spot), the last active direction is set by default
   */

    public abstract void updateAttackRange();

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Directions getDirections() {
        return directions;
    }

    public GravitySettings getGravitySettings() {
        return gravitySettings;
    }

    public Health getHeath() {
        return health;
    }

    public float getWalkSpeed() {
        return walkSpeed;
    }
}
