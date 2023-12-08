package org.example.entities;

import org.example.utils.PlayerConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.example.utils.PlayerConstants.*;

public class Player extends Entity {
    private final static int ANIMATION_SPEED = 15;
    private final static int PLAYER_SPEED = 1;
    private static final int CHARACTER_SPRITE_HEIGHT = 40;
    private static final int CHARACTER_SPRITE_WIDTH = 64;
    private final Directions directions;
    private final Actions actions;
    private BufferedImage[][] animations;    private int animationTick;
    private int animationIndex;

    PlayerConstants currentAnimation = SPRITE_JUMPING;

    public Player(float x, float y) {
        super(x, y);
        directions = new Directions();
        actions = new Actions();
        loadAnimations();
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

    public void render(Graphics g) {
        g.drawImage(animations[currentAnimation.getSpriteIndex()][animationIndex],
                (int) x,
                (int) y,
                128,
                80,
                null);

    }

    private void loadAnimations() {

        try (InputStream is = getClass().getResourceAsStream("/player_sprites.png") ) {
            var characterAtlas = ImageIO.read(is);

            animations = new BufferedImage[9][6];

            for (int spriteRow = 0; spriteRow < animations.length; spriteRow++) {

                for (int spriteColumn = 0; spriteColumn < animations[spriteRow].length; spriteColumn++) {
                    animations[spriteRow][spriteColumn] = characterAtlas.getSubimage(spriteColumn * CHARACTER_SPRITE_WIDTH,
                            spriteRow * CHARACTER_SPRITE_HEIGHT,
                            CHARACTER_SPRITE_WIDTH,
                            CHARACTER_SPRITE_HEIGHT);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load an image", e);
        }
    }

        private void updateCharacterPosition() {
        directions.setMoving(false);

            if (directions.isMovingLeft() && !directions.isMovingRight()) {
                x -= PLAYER_SPEED;
                directions.setMoving(true);
            } else if (directions.isMovingRight() && !directions.isMovingLeft()) {
                x += PLAYER_SPEED;
                directions.setMoving(true);
            }

            if (directions.isMovingUp() && !directions.isMovingDown()) {
                y -= PLAYER_SPEED;
                directions.setMoving(true);
            } else if (directions.isMovingDown() && !directions.isMovingUp()) {
                y += PLAYER_SPEED;
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

    public void setPlayerDirection(Directions directions) {
        }
}
