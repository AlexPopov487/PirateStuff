package org.example;

import org.example.inputs.KeyboardInputs;
import org.example.inputs.MouseInputs;
import org.example.utils.Direction;
import org.example.utils.PlayerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.example.utils.Direction.STILL;
import static org.example.utils.PlayerConstants.*;

public class GamePanel extends JPanel {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int CHARACTER_SPRITE_HEIGHT = 40;
    private static final int CHARACTER_SPRITE_WIDTH = 64;
    private final static int ANIMATION_SPEED = 15;

    private int xDelta = 0;
    private int yDelta = 0;

    private BufferedImage characterAtlas;
    private BufferedImage[][] animations;

    private int animationTick;
    private int animationIndex;

    PlayerConstants currentAnimation = SPRITE_JUMPING;
    Direction currPlayerDirection = STILL;


    public GamePanel() {
        importImage();
        loadAnimations();
        addKeyListener(new KeyboardInputs(this));
        MouseInputs mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        setPanelSize();

    }

    private void loadAnimations() {
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

    private void importImage() {
        try (InputStream is = getClass().getResourceAsStream("/player_sprites.png") ) {
            characterAtlas = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load an image", e);
        }
    }

    /*
     the resolution (or the size) should be set from game panel and not from game window. Otherwise, the size will
     include window borders
    */
    private void setPanelSize() {
        setPreferredSize(new Dimension(1280, 800));
    }

    /* For the sake of understanding, refer to a Graphics g as a Brush with which we can draw
    * something inside JPanel */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(animations[currentAnimation.getSpriteIndex()][animationIndex], xDelta, yDelta, 128, 80, null);
    }

    private void updateCharacterPosition() {
        if (STILL.equals(currPlayerDirection)) return;

        switch (currPlayerDirection) {
            case LEFT -> xDelta-=5;
            case RIGHT -> xDelta+=5;
            case DOWN -> yDelta+=5;
            case UP -> yDelta-=5;
        }
    }

    private void setCharacterAnimation() {
        if (STILL.equals(currPlayerDirection)) {
            currentAnimation = SPRITE_IDLE;
        } else {
            currentAnimation = SPRITE_RUNNING;
        }
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= currentAnimation.getSpriteAmount()) {
                animationIndex = 0;
            }
        }
    }

    public void setPlayerDirection(Direction direction) {
        currPlayerDirection = direction;
    }

    public void updateGameLogic() {
        setCharacterAnimation();
        updateCharacterPosition();

        updateAnimationTick();
    }
}
