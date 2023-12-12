package org.example.entities;

import org.example.Game;
import org.example.utils.AtlasType;
import org.example.utils.CollisionHelper;
import org.example.utils.PlayerConstants;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Game.*;
import static org.example.utils.PlayerConstants.*;

public class Player extends Entity {
    private final static int ANIMATION_SPEED = 15;
    private final static int PLAYER_SPEED = 1;
    private final static int CHARACTER_HITBOX_WIDTH = 20;
    private final static int CHARACTER_HITBOX_HEIGHT = 28;
    private final Directions directions;
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
        initHitBox(x, y, CHARACTER_HITBOX_WIDTH * SCALE, CHARACTER_HITBOX_HEIGHT * SCALE);
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
        drawHitBox(g);

    }

    private void updateCharacterPosition() {
        directions.setMoving(false);

        if (directions.isNoDirectionSet()) return;

        float xDestination = hitBox.x;
        float yDestination = hitBox.y;

        if (directions.isMovingLeft() && !directions.isMovingRight()) {
            xDestination -= PLAYER_SPEED;
        } else if (directions.isMovingRight() && !directions.isMovingLeft()) {
            xDestination += PLAYER_SPEED;
        }

        if (directions.isMovingUp() && !directions.isMovingDown()) {
            yDestination -= PLAYER_SPEED;
        } else if (directions.isMovingDown() && !directions.isMovingUp()) {
            yDestination += PLAYER_SPEED;
        }

        if (CollisionHelper.canMoveHere(xDestination, yDestination, hitBox.width, hitBox.height, currentLevelData)) {
            hitBox.x = xDestination;
            hitBox.y = yDestination;
            directions.setMoving(true);
        }
    }

    private void setCharacterAnimation() {

        var previousAnimation = currentAnimation;

        if (directions.isMoving()) {
            currentAnimation = SPRITE_RUNNING;
        } else {
            currentAnimation = SPRITE_IDLE;
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
