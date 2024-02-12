package ru.alexp.types;

public enum MessageType {
    ONBOARDING(0),
    KEY_FOUND(1);


    private final int animationIndex;

    MessageType(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}
