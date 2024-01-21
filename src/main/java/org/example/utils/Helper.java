package org.example.utils;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.EnemyType;
import org.example.entities.EntityType;
import org.example.entities.GravitySettings;
import org.example.levels.LevelManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.example.Config.GRAVITY_FORCE;

public class Helper {
    /*
     collect an array of indexes that are compatible with outsideSpriteArray from LevelManager.importOutsideSprite()
     The indexes in the levelDataArray are taken using level template asset, where each pixel's red color value serves
     as a tile index for the outsideData array.
    */
    public static int[][] getLevelDataFromAsset(BufferedImage levelAsset) {

        int[][] levelData = new int[levelAsset.getHeight()][levelAsset.getWidth()];

        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                int lvlBlockIndex = pixelColor.getRed();

                // to avoid ArrayOutOfBounds when RED value is bigger than max index of outsideSpriteArray
                if (lvlBlockIndex >= (LevelManager.LVL_TEMPLATE_SPRITES_IN_WIDTH * LevelManager.LVL_TEMPLATE_SPRITES_IN_HEIGHT)) {
                    lvlBlockIndex = 0;
                }

                levelData[row][colunm] = lvlBlockIndex;
            }
        }
        return levelData;
    }

    // Crabs are defined by pixels of green color on the level template image
    public static List<Crabby> getCrabsFromLevelAsset(BufferedImage levelAsset) {

        var crabs = new ArrayList<Crabby>();
        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int crabBlockIndex = pixelColor.getGreen();
                if (crabBlockIndex == EnemyType.CRAB.ordinal()) { // todo make it a CRAB property, not ordinal() to ensure independence of the enum order

                    crabs.add(new Crabby(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize()));
                }
            }
        }

        return crabs;
    }

    public static Point getPlayerSpawnPoint(BufferedImage levelAsset){

        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int playerBlockIndex = pixelColor.getGreen();
                if (playerBlockIndex == 100) { // todo make it a Player property, not a raw number

                    return new Point(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize());
                }
            }
        }
        // top left corner
        return new Point(GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize());
    }

    public static GravitySettings generateGravitySettingForEntity(EntityType entityType) {
         return switch (entityType) {
            case PLAYER -> new GravitySettings(Config.Player.AIR_SPEED, GRAVITY_FORCE, Config.Player.JUMP_SPEED, Config.Player.POST_COLLISION_FALL_SPEED);
            case ENEMY -> new GravitySettings(Config.Enemy.AIR_SPEED, GRAVITY_FORCE, Config.Enemy.JUMP_SPEED, Config.Enemy.POST_COLLISION_FALL_SPEED);
        };
    }
}
