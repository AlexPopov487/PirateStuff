package ru.alexp.utils;

public enum PlayerConstants {
    SPRITE_IDLE(0, 5),
    SPRITE_RUNNING(1, 6),
    SPRITE_JUMPING(2, 3),
    SPRITE_FALLING(3, 1),
    SPRITE_ATTACK (4, 3),
    SPRITE_HIT(5, 4),
    SPRITE_DEAD(6, 8);

    private final int spriteIndex;
    private final int frameCount;

    PlayerConstants(int spriteIndex, int frameCount) {
        this.spriteIndex = spriteIndex;
        this.frameCount = frameCount;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
