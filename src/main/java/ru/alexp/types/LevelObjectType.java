package ru.alexp.types;

public enum LevelObjectType {
    POTION_BLUE(0,7, 0),
    POTION_RED(1,7, 1),
    BOX(0,8,2),
    BARREL(1, 8, 3),
    SPIKE(0, 0, 4),
    CANNON_LEFT(0, 7, 5),
    CANNON_RIGHT(0, 7, 6),
    PROJECTILE(0,0, -1),
    EXPLOSION(0,7, -1),
    TREE_STRAIGHT(0, 4,7),
    TREE_BEND_RIGHT(0, 4,8),
    TREE_BEND_LEFT(0, 4,9),
    BACK_TREE_STRAIGHT(0, 4, 13),
    WATER_WAVE(0, 0,12),
    WATER_BODY(0, 4, 11),
    SHARK(0, 0, 10),
    QUESTION_MARK(0, 5, -1),
    FLAG(0, 9, 14),
    KEY(0, 8, 15),
    CHEST(0, 10, 16),
    SHIP(0, 4, 17),
    MESSAGE_ONBOARDING(0, 0, 18),
    MESSAGE_KEY_FOUND(0, 0, 19);

    private final int spriteIndex;
    private final int frameCount;
    private final int bluePixelValue;

    LevelObjectType(int spriteIndex, int frameCount, int bluePixelValue) {
        this.spriteIndex = spriteIndex;
        this.frameCount = frameCount;
        this.bluePixelValue = bluePixelValue;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getBluePixelValue() {
        return bluePixelValue;
    }
}
