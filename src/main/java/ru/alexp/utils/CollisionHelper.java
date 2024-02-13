package ru.alexp.utils;

import ru.alexp.Config;
import ru.alexp.GamePanel;

import java.awt.geom.Rectangle2D;

public class CollisionHelper {
    // check, whether border points of a tepm rectangle collide with any on the level objects
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] currentLevelData) {
        return !isSolid(x, y, currentLevelData)
                && !isSolid(x + width, y + height, currentLevelData)
                && !isSolid(x, y + height, currentLevelData)
                && !isSolid(x + width, y, currentLevelData);
    }


    public static boolean isTileSolid(int tileX, int tileY, int[][] lvlData, boolean checkWater) {
        int spriteIndex = lvlData[tileY][tileX];

        boolean isSolid = spriteIndex != Config.LevelEnv.TILE_VOID_INDEX;
        if (!checkWater) return isSolid;

        return (isSolid
                && spriteIndex != Config.LevelEnv.TILE_WATER_TOP_INDEX
                && spriteIndex != Config.LevelEnv.TILE_WATER_BOTTOM_INDEX);
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

    // todo redundant, but is kept to stay in sync with tutorial
    public static boolean isFloor(Rectangle2D.Float hitbox, float xStep, int[][] currentLevelData) {

        if (xStep > 0) { // moving right
            return isSolid(hitbox.x + xStep + hitbox.width, hitbox.y + hitbox.height + 1, currentLevelData);
        } else {
            return isSolid(hitbox.x + xStep, hitbox.y + hitbox.height + 1, currentLevelData);
        }
    }

    // check is there is any visible obstacle (or pit) between two points
    public static boolean isDistanceClearFromObstacle(int[][] levelData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int currentTileY, boolean checkPits) {
        int firstTileX = (int) (firstHitBox.x / GamePanel.getCurrentTileSize());
        int secondTileX = (int) (secondHitBox.x / GamePanel.getCurrentTileSize());

        if (firstTileX > secondTileX) {
            return isDistanceClear(secondTileX, firstTileX, currentTileY, levelData, checkPits);
        } else {
            return isDistanceClear(firstTileX, secondTileX, currentTileY, levelData, checkPits);
        }
    }

    public static boolean hasProjectileHitObstacle(Rectangle2D.Float projectileHitBox, int[][] levelData) {
        float projectileCenterX = projectileHitBox.x + projectileHitBox.width / 2;
        float projectileCenterY = projectileHitBox.y + projectileHitBox.height / 2;

        return isSolid(projectileCenterX, projectileCenterY, levelData);
    }

    public static boolean isPlayerStumpsEnemy(Rectangle2D.Float playerHitBox, Rectangle2D.Float enemyHitBox) {
        Rectangle2D.Float playerFeet = new Rectangle2D.Float(playerHitBox.x, playerHitBox.y + playerHitBox.height, playerHitBox.width, 5);
        return playerFeet.intersects(enemyHitBox.getX(), enemyHitBox.getY(), enemyHitBox.getWidth(), 5);
    }

    private static boolean isDistanceClear(int startX, int endX, int y, int[][] levelData, boolean checkPits) {
        // loop from the xTile of the first object to the xTile of the second object and check for obstacles
        for (int i = 0; i < endX - startX; i++) {
            if (isTileSolid(startX + i, y, levelData, true)) {
                return false;
            }

            if (checkPits) {
                // check tiles below to determine whether there is a pit between 2 objects
                if (!isTileSolid(startX + i, y + 1, levelData, true)) {
                    return false;
                }
            }

        }
        return true;
    }

    // check, whether a certain position (x, y) is either a solid lvl object or is out of bounds of the game window
    private static boolean isSolid(float x, float y, int[][] currentLevelData) {
        int maxLevelWidth = currentLevelData[0].length * GamePanel.getCurrentTileSize();
        if (x < 0 || x >= maxLevelWidth) return true;
        if (y < 0 || y >= GamePanel.getWindowHeight()) return true;

        int xIndex = (int) x / GamePanel.getCurrentTileSize();
        int yIndex = (int) y / GamePanel.getCurrentTileSize();

        return isTileSolid(xIndex, yIndex, currentLevelData, true);
    }
}
