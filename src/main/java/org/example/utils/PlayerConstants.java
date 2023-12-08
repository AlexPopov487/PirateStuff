package org.example.utils;

public enum PlayerConstants {
    SPRITE_IDLE(0, 5),
    SPRITE_RUNNING(1, 6),
    SPRITE_JUMPING(2, 3),
    SPRITE_FALLING(3, 1),
    SPRITE_GROUND(4, 2),
    SPRITE_HIT(5, 4),
    SPRITE_ATTACK_1(6, 3),
    SPRITE_ATTACK_JUMP_1(7, 3),
    SPRITE_ATTACK_JUMP_2(8, 3);

    private final int spriteIndex;
    private final int spriteAmount;

    PlayerConstants(int spriteIndex, int spriteAmount) {
        this.spriteIndex = spriteIndex;
        this.spriteAmount = spriteAmount;
    }

    public int getSpriteAmount() {
        return spriteAmount;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }
}
