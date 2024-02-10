package org.example.levelObjects;

import org.example.Config;
import org.example.entities.BaseEntity;
import org.example.types.LevelObjectType;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static org.example.Config.ENTITY_ANIMATION_SPEED;

public class LevelObject extends BaseEntity {
    protected Rectangle2D.Float hitBox;
    protected LevelObjectType objectType;
    // some objects (e.g. barrels and boxes) do not have to be animated all the time, but only when they are hit
    protected boolean shouldAnimate;
    protected boolean isActive = true;
    protected int animationTick;
    protected int animationIndex;
    private int xDrawOffset;
    private int yDrawOffset;

    public LevelObject(float x, float y, LevelObjectType levelObjectType, boolean shouldAnimate) {
        super(x, y, 0, 0);
        this.objectType = levelObjectType;
        this.shouldAnimate = shouldAnimate;
        initDrawOffsets();
    }

    public void reset() {
        animationTick = 0;
        animationIndex = 0;
        isActive = true;

        if (LevelObjectType.BARREL.equals(objectType)
                || LevelObjectType.BOX.equals(objectType)
                || LevelObjectType.CANNON_LEFT.equals(objectType)
                || LevelObjectType.CANNON_RIGHT.equals(objectType)
                || LevelObjectType.CHEST.equals(objectType)) {
            shouldAnimate = false;
        } else {
            shouldAnimate = true;
        }
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public LevelObjectType getObjectType() {
        return objectType;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setShouldAnimate(boolean shouldAnimate) {
        this.shouldAnimate = shouldAnimate;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, width, height);
    }

    // FOR DEBUGGING
    protected void drawHitBox(Graphics g, int xLevelOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void updateAnimationTick(Integer customAnimationSpeed) {
        animationTick++;
        int animationSpeed = customAnimationSpeed == null ? ENTITY_ANIMATION_SPEED : customAnimationSpeed;
        if (animationTick >= animationSpeed) {
            animationTick = 0;
            animationIndex++;
            if (animationIndex >= objectType.getFrameCount()) {
                animationIndex = 0;

                if (LevelObjectType.BARREL.equals(objectType)
                        || LevelObjectType.BOX.equals(objectType)) {
                    shouldAnimate = false;
                    isActive = false;
                } else if (LevelObjectType.CANNON_RIGHT.equals(objectType)
                        || LevelObjectType.CANNON_LEFT.equals(objectType)) {
                    shouldAnimate = false;
                } else if (LevelObjectType.CHEST.equals(objectType)) {
                    shouldAnimate = false;
                    animationIndex = objectType.getFrameCount() - 1;
                } else if (LevelObjectType.EXPLOSION.equals(objectType)) {
                    isActive = false;
                }
            }
        }
    }

    private void initDrawOffsets() {
        switch (objectType) {
            case POTION_BLUE, POTION_RED -> {
                xDrawOffset = Config.LevelEnv.POTION_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.POTION_DRAW_OFFSET_Y;
            }
            case BOX -> {
                xDrawOffset = Config.LevelEnv.BOX_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.BOX_DRAW_OFFSET_Y;
            }
            case BARREL -> {
                xDrawOffset = Config.LevelEnv.BARREL_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.BARREL_DRAW_OFFSET_Y;
            }
            case SPIKE -> {
                xDrawOffset = Config.LevelEnv.SPIKE_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.SPIKE_DRAW_OFFSET_Y;
            }
            case KEY -> {
                xDrawOffset = Config.LevelEnv.KEY_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.KEY_DRAW_OFFSET_Y;
            }
            case CHEST -> {
                xDrawOffset = Config.LevelEnv.CHEST_DRAW_OFFSET_X;
                yDrawOffset = Config.LevelEnv.CHEST_DRAW_OFFSET_Y;
            }
        }
    }
}
