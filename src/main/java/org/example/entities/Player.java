package org.example.entities;

import org.example.Game;
import org.example.utils.AtlasType;
import org.example.utils.CollisionHelper;
import org.example.utils.PlayerConstants;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Game.*;
import static org.example.gameState.Playing.CHARACTER_SPRITE_HEIGHT;
import static org.example.gameState.Playing.CHARACTER_SPRITE_WIDTH;
import static org.example.utils.CollisionHelper.*;
import static org.example.utils.PlayerConstants.*;

public class Player extends Entity {
    private final static int ANIMATION_SPEED = 15;
    private final static float PLAYER_SPEED = 1f * SCALE;
    private final static int CHARACTER_HITBOX_WIDTH = 20;
    private final static int CHARACTER_HITBOX_HEIGHT = 27;
    private final Directions directions;
    private final GravitySettings gravitySettings;
    private final Actions actions;
    private BufferedImage[][] animations;
    private int animationTick;
    private int animationIndex;
    private int[][] currentLevelData;
    // 21, 4 is the offset from 0,0 where the actual character sprite is drawn on a png image
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    PlayerConstants currentAnimation = SPRITE_JUMPING;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        directions = new Directions();
        actions = new Actions();
        loadAnimations();
        initHitBox(x, y, (int) (CHARACTER_HITBOX_WIDTH * SCALE), (int) (CHARACTER_HITBOX_HEIGHT * SCALE));
        gravitySettings = new GravitySettings(0f, 0.04f, -2.25f, 0.5f);
    }

    public void setCurrentLevelData(int[][] currentLevelData) {
        this.currentLevelData = currentLevelData;
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
    }

    public void setPlayerDirection(Directions directions) {
    }

    public void render(Graphics g) {
        g.drawImage(animations[currentAnimation.getSpriteIndex()][animationIndex],
                (int) (hitBox.x - xDrawOffset),
                (int) (hitBox.y - yDrawOffset),
                width,
                height,
                null);
//        drawHitBox(g);
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
        }
        if (directions.isMovingRight()) {
            xDestination += PLAYER_SPEED;
        }

        if (!directions.isInAir() && !isOnTheFloor(hitBox, currentLevelData)) {
            directions.setInAir(true);
        }

        if (directions.isInAir()) {
            float yDestination = hitBox.y + gravitySettings.getAirSpeed();

            if (CollisionHelper.canMoveHere(hitBox.x, yDestination, hitBox.width, hitBox.height, currentLevelData)) {
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
        if (CollisionHelper.canMoveHere(xDestination, hitBox.y, hitBox.width, hitBox.height, currentLevelData)) {
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

        if (directions.isMoving()) {
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
            currentAnimation = SPRITE_ATTACK_1;
        }

        // this is necessary to avoid starting new animation from the index of a previous animation (leads to flickering and bugs)
        if (previousAnimation != currentAnimation) {
            resetAnimationTick();
        }
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

            if (animationIndex >= currentAnimation.getSpriteAmount()) {
                animationIndex = 0;
                resetAnimations();
            }
        }
    }

    private void resetAnimations() {
        actions.setAttacking(false);
    }

    private void loadAnimations() {

        var characterAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYER);

        animations = new BufferedImage[9][6];

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
