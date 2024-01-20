package org.example.entities;

import org.example.gameState.Playing;
import org.example.utils.AtlasType;
import org.example.utils.CollisionHelper;
import org.example.utils.PlayerConstants;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.example.Game.SCALE;
import static org.example.gameState.Playing.CHARACTER_SPRITE_HEIGHT;
import static org.example.gameState.Playing.CHARACTER_SPRITE_WIDTH;
import static org.example.utils.CollisionHelper.*;
import static org.example.utils.PlayerConstants.*;

public class Player extends Entity {
    private final static int ANIMATION_SPEED = 15;
    private final static float PLAYER_SPEED = 1f * SCALE;
    private final static int CHARACTER_HITBOX_WIDTH = 20;
    private final static int CHARACTER_HITBOX_HEIGHT = 27;
    private final Playing playing;
    private final Directions directions;
    private final GravitySettings gravitySettings;
    private final Actions actions;
    private boolean isAttackPerformed = false;
    private final StatusBar statusBar;
    private final Heath heath;
    private BufferedImage[][] animations;
    private int animationTick;
    private int animationIndex;
    private int[][] currentLevelData;
    // 21, 4 is the offset from 0,0 where the actual character sprite is drawn on a png image
    private float xDrawOffset = 21 * SCALE; // todo move to constants as I did to the enemy class
    private float yDrawOffset = 4 * SCALE;

    // for handling rendering player sprite when going left
    private int xFlip = 0;
    private int widthFlip = 1;

    PlayerConstants currentAnimation = SPRITE_JUMPING;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        directions = new Directions();
        actions = new Actions();
        heath = new Heath(100);
        statusBar = new StatusBar(heath);
        loadAnimations();
        initHitBox(x, y, (int) (CHARACTER_HITBOX_WIDTH * SCALE), (int) (CHARACTER_HITBOX_HEIGHT * SCALE));
        initAttackRange();
        // todo move values to constants
        gravitySettings = new GravitySettings(0f, 0.02f * SCALE, -1.25f * SCALE, 0.25f * SCALE);
    }

    public void setSpawnPosition(Point spawnPoint) {
        this.y = spawnPoint.y;
        this.x = spawnPoint.x;
        hitBox.x = x;
        hitBox.y = y;
    }

    public void setCurrentLevelData(int[][] currentLevelData) {
        this.currentLevelData = currentLevelData;
    }

    public void setInAirIfPlayerNotOnFloor(Player player, int[][] currentLevelData) {
        if (!CollisionHelper.isOnTheFloor(player.getHitBox(), currentLevelData)) {
            player.getDirection().setInAir(true);
        }
    }

    public Directions getDirection() {
        return directions;
    }

    public Actions getActions() {
        return actions;
    }

    public void update() {
        updateCharacterPosition();
        setCharacterAnimation();

        updateAnimationTick();
        statusBar.update();
        updateAttackRange();
    }

    public void setPlayerDirection(Directions directions) {
    }

    public void render(Graphics g, int xLevelOffset) {
        statusBar.render(g);

        g.drawImage(animations[currentAnimation.getSpriteIndex()][animationIndex],
                (int) (hitBox.x - xDrawOffset) - xLevelOffset + xFlip,
                (int) (hitBox.y - yDrawOffset),
                width * widthFlip,
                height,
                null);
//        drawHitBox(g, xLevelOffset);
//        drawAttackRangeBox(g, xLevelOffset);
    }

    public Heath getHeath() {
        return heath;
    }

    public void updateAttackRange() {
        if (directions.isMovingRight()) {
            attackRange.x = hitBox.x + hitBox.width + 10 * SCALE;
        } else if (directions.isMovingLeft()) {
            attackRange.x = hitBox.x - hitBox.width - 10 * SCALE;
        }

        attackRange.y = hitBox.y + 10 * SCALE;
    }

    public void initAttackRange() {
        attackRange = new Rectangle2D.Float(x, y, 20 * SCALE, 20 * SCALE);
    }

    public void reset() {
        directions.reset();
        resetInAir();
        actions.resetAll();
        heath.reset();
        resetAnimations();
        currentAnimation = SPRITE_IDLE;

        // reset player position
        hitBox.x = x;
        hitBox.y = y;

        setInAirIfPlayerNotOnFloor(this, currentLevelData);
        initAttackRange();
    }

    private void resetGravitySettings(){// todo move values to constants
        gravitySettings.setAirSpeed(0f);
        gravitySettings.setGravityForce(0.02f * SCALE);
        gravitySettings.setJumpSpeed(-1.25f * SCALE);
        gravitySettings.setPostCollisionFallSpeed(0.25f * SCALE);
    }

    private void updateCharacterPosition() {
        directions.setMoving(false);

        if (directions.isJumping()) {
            handleJump();
        }

        if (directions.isNoDirectionSet()) return;

        float xDestination = hitBox.x;

        if (directions.isMovingLeft()) {
            xDestination -= PLAYER_SPEED;
            xFlip = width;
            widthFlip = -1;
        }

        if (directions.isMovingRight()) {
            xDestination += PLAYER_SPEED;
            xFlip = 0;
            widthFlip = 1;
        }

        if (!directions.isInAir() && !isOnTheFloor(hitBox, currentLevelData)) {
            directions.setInAir(true);
        }

        if (directions.isInAir()) {
            float yDestination = hitBox.y + gravitySettings.getAirSpeed();

            if (canMoveHere(hitBox.x, yDestination, hitBox.width, hitBox.height, currentLevelData)) {
                hitBox.y = yDestination;
                gravitySettings.setAirSpeed(gravitySettings.getAirSpeed() + gravitySettings.getGravityForce());
                updatePlayerXPos(xDestination);
            } else {
             /*
             if we assume that updating position will lead to collision, the pos update will not be made,
             meaning that there still will be some space between the player and the obstacle. Thus, we need to
             move the player as close to the obstacle as possible
            */
                hitBox.y = getClosestToObstacleYPos(hitBox, yDestination);

                if (gravitySettings.getAirSpeed() > 0) { // falling on the floor
                    resetInAir();
                } else { // colliding with the roof, need to bounce
                    gravitySettings.setAirSpeed(gravitySettings.getPostCollisionFallSpeed());
                }
                updatePlayerXPos(xDestination);
            }

        } else {
            updatePlayerXPos(xDestination);
        }

        directions.setMoving(true);
    }

    private void handleJump() {
        if (directions.isInAir()) return;

        directions.setInAir(true);
        gravitySettings.setAirSpeed(gravitySettings.getJumpSpeed());
    }

    private void resetInAir() {
        directions.setInAir(false);
        gravitySettings.setAirSpeed(0);
    }

    private void updatePlayerXPos(float xDestination) {
        if (canMoveHere(xDestination, hitBox.y, hitBox.width, hitBox.height, currentLevelData)) {
            hitBox.x = xDestination;
        } else {
            /*
             if we assume that updating position will lead to collision, the pos update will not be made,
             meaning that there still will be some space between the player and the obstacle. Thus, we need to
             move the player as close to the obstacle as possible
            */
            hitBox.x = getClosestToObstacleXPos(hitBox, xDestination);
        }
    }

    private void setCharacterAnimation() {

        var previousAnimation = currentAnimation;

        if (directions.isMoving() && !directions.isMovingToRightAndLeft()) {
            currentAnimation = SPRITE_RUNNING;
        } else {
            currentAnimation = SPRITE_IDLE;
        }

        if (directions.isInAir()) {
            if (directions.isJumping()) {
                currentAnimation = SPRITE_JUMPING;
            } else {
                currentAnimation = SPRITE_FALLING;
            }
        }

        if (actions.isAttacking()) {
            currentAnimation = SPRITE_ATTACK;

            // the sprite's attacking animation takes too long, thus we shorten it by one sprite
            if (previousAnimation != SPRITE_ATTACK) {
                previousAnimation = currentAnimation;
                resetAnimationTick();
                animationIndex = 1;
            }
            attack();
        }

        // this is necessary to avoid starting new animation from the index of a previous animation (leads to flickering and bugs)
        if (previousAnimation != currentAnimation) {
            resetAnimationTick();
        }
    }

    private void attack() {
        /*
        the attack is registered only at specific animation index (when it looks like an enemy lands a strike)
        and only once per attack animation
        */
        if (animationIndex != 1 || isAttackPerformed) return;

        playing.checkEnemyHit(attackRange);
        isAttackPerformed = true;

    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= currentAnimation.getFrameCount()) {
                animationIndex = 0;
                resetAnimations();
            }
        }
    }

    private void resetAnimations() {
        actions.setAttacking(false);
        isAttackPerformed = false;
    }

    private void loadAnimations() {

        var characterAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYER);

        animations = new BufferedImage[7][8];

        for (int spriteRow = 0; spriteRow < animations.length; spriteRow++) {

            for (int spriteColumn = 0; spriteColumn < animations[spriteRow].length; spriteColumn++) {
                animations[spriteRow][spriteColumn] = characterAtlas.getSubimage(spriteColumn * CHARACTER_SPRITE_WIDTH,
                        spriteRow * CHARACTER_SPRITE_HEIGHT,
                        CHARACTER_SPRITE_WIDTH,
                        CHARACTER_SPRITE_HEIGHT);
            }
        }
    }
}
