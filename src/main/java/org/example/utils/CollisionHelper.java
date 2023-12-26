package org.example.utils;

import org.example.Game;
import org.example.GamePanel;

import java.awt.geom.Rectangle2D;

public class CollisionHelper {
    // check, whether border points of a tepm rectangle collide with any on the level objects
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] currentLevelData) {
        if (!isSolid(x, y, currentLevelData))
            if (!isSolid(x + width, y + height, currentLevelData))
                if (!isSolid(x, y + height, currentLevelData))
                    if (!isSolid(x + width, y, currentLevelData))
                        return true;

        return false;
    }


    // todo do not understand how this works
    // check, whether a certain position (x, y) is either a solid lvl object or is out of bounds of the game window
    private static boolean isSolid(float x, float y, int[][] currentLevelData) {
        int maxLevelWidth = currentLevelData[0].length * GamePanel.getCurrentTileSize();
        if (x < 0 || x >= maxLevelWidth) return true;
        if (y < 0 || y >= GamePanel.getWindowHeight()) return true;

        int xIndex = (int) x / GamePanel.getCurrentTileSize();
        int yIndex = (int) y / GamePanel.getCurrentTileSize();

        int spriteIndex = currentLevelData[yIndex][xIndex];

        // a tile with index 11 is transparent i.e. not solid
        return spriteIndex != 11;
    }

    public static float getClosestToObstacleXPos(Rectangle2D.Float hitbox, float xDestination) {

        int currentTile = (int) hitbox.x / GamePanel.getCurrentTileSize();

        if (xDestination > hitbox.x) { // we are moving to the right
            int currTileXPosInPixels = currentTile * GamePanel.getCurrentTileSize();
            int playerToTileXOffset = (int) (GamePanel.getCurrentTileSize() - hitbox.width);
            float closestToObstacleXPos = currTileXPosInPixels + playerToTileXOffset;
            // subtract 1 so player does not overlap with the texture but only is positioned next to it
            return closestToObstacleXPos - 1;
        } else { // we are moving to the left
            return currentTile * GamePanel.getCurrentTileSize();
        }
    }

    public static float getClosestToObstacleYPos(Rectangle2D.Float hitbox, float yDestination) {

        int currentTile = (int) hitbox.y / GamePanel.getCurrentTileSize();

        if (yDestination > hitbox.y) { // falling to the floor
            int currTileYPosInPixels = currentTile * GamePanel.getCurrentTileSize();
            int playerToTileYOffset = (int) (GamePanel.getCurrentTileSize() - hitbox.height);
            float closestToObstacleYPos = currTileYPosInPixels + playerToTileYOffset;
            // subtract 1 so player does not overlap with the texture but only is positioned next to it
            return closestToObstacleYPos - 1;
        } else { // jumping
            return currentTile * GamePanel.getCurrentTileSize();
        }
    }

    public static boolean isOnTheFloor(Rectangle2D.Float hitbox, int[][] currentLevelData) {
        // check that pixels below bottom left and bottom right are solid
        return isSolid(hitbox.x, hitbox.y + hitbox.height + 1, currentLevelData)
                || isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, currentLevelData);
    }
}
