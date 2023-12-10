package org.example.levels;

public class Level {

    private final int[][] levelData;

    public Level(int[][] levelData) {
        this.levelData = levelData;
    }

    public int getLevelSpriteIndex(int x, int y){
        return levelData[y][x];
    }
}
