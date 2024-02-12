package ru.alexp.types;

public enum EnemyState {
    IDLE(0),
    RUNNING(1),
    ATTACKING(2),
    HIT(3),
    DEAD(4);

    private final int animationRowIndex;

    EnemyState(int animationRowIndex) {
        this.animationRowIndex = animationRowIndex;
    }

    public int getAnimationRowIndex() {
        return animationRowIndex;
    }
}
