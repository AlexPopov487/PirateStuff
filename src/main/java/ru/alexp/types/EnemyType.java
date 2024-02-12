package ru.alexp.types;

public enum EnemyType {
    CRAB(0),
    STAR(1);

    private final int greenPixelValue;

    EnemyType(int greenPixelValue) {
        this.greenPixelValue = greenPixelValue;
    }

    public int getGreenPixelValue() {
        return greenPixelValue;
    }
}
