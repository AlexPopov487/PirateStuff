package org.example.utils;

import org.example.GamePanel;

public class CollisionHelper {
    // check, whether border points of a tepm rectangle collide with any on the level objects
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] currentLevelData) {
        if (isNotSolid(x, y, currentLevelData))
            if (isNotSolid(x + width, y + height, currentLevelData))
                if (isNotSolid(x, y + height, currentLevelData))
                    if (isNotSolid(x + width, y, currentLevelData))
                        return true;

        return false;
    }

    // check, whether a certain position (x, y) is either a solid lvl object or is out of bounds of the game window
    private static boolean isNotSolid(float x, float y, int[][] currentLevelData) {
        if (x < 0 || x >= GamePanel.getWindowWidth()) return false;
        if (y < 0 || y >= GamePanel.getWindowHeight()) return false;

        int xIndex = (int) x / GamePanel.getCurrentTileSize();
        int yIndex = (int) y / GamePanel.getCurrentTileSize();

        int spriteIndex = currentLevelData[yIndex][xIndex];

        // a tile with index 11 is transparent i.e. not solid
        return spriteIndex == 11;
    }
}
