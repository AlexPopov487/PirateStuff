package org.example.types;

public enum LevelObjectType {
    POTION_BLUE(0,7),
    POTION_RED(1,7),
    BOX(0,8),
    BARREL(1, 8),
    SPIKE(0, 0);

    private final int spriteIndex;
    private final int frameCount;

    LevelObjectType(int spriteIndex, int frameCount) {
        this.spriteIndex = spriteIndex;
        this.frameCount = frameCount;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public int getFrameCount() {
        return frameCount;
    }
}
