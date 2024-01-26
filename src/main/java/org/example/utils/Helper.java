package org.example.utils;

import org.example.Config;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.GravitySettings;
import org.example.levelObjects.Cannon;
import org.example.levelObjects.Container;
import org.example.levelObjects.Potion;
import org.example.levelObjects.Spike;
import org.example.levels.LevelManager;
import org.example.types.EnemyType;
import org.example.types.EntityType;
import org.example.types.LevelObjectType;

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

    // Potions are defined by pixels of blue color on the level template image
    public static List<Potion> getPotionsFromLevelAsset(BufferedImage levelAsset) {

        var potions = new ArrayList<Potion>();
        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int potionBlockIndex = pixelColor.getBlue();
                if (potionBlockIndex == LevelObjectType.POTION_BLUE.ordinal()) { // todo make it a POTION property, not ordinal() to ensure independence of the enum order
                    potions.add(new Potion(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_BLUE));
                } else if (potionBlockIndex == LevelObjectType.POTION_RED.ordinal()) {
                    potions.add(new Potion(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_RED));
                }
            }
        }

        return potions;
    }

    // todo this is all copy-paste from the method above.
    // Containers are defined by pixels of blue color on the level template image
    public static List<Container> getContainersFromLevelAsset(BufferedImage levelAsset) {

        var containers = new ArrayList<Container>();
        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int container = pixelColor.getBlue();
                if (container == LevelObjectType.BOX.ordinal()) { // todo make it a POTION property, not ordinal() to ensure independence of the enum order
                    containers.add(new Container(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.BOX));
                } else if (container == LevelObjectType.BARREL.ordinal()) {
                    containers.add(new Container(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.BARREL));
                }
            }
        }

        return containers;
    }

    public static List<Spike> getSpikesFromLevelAsset(BufferedImage levelAsset) {
        var spikes = new ArrayList<Spike>();

        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int spike = pixelColor.getBlue();
                if (spike == LevelObjectType.SPIKE.ordinal()) { // todo make it a POTION property, not ordinal() to ensure independence of the enum order
                    spikes.add(new Spike(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.SPIKE));
                }
            }
        }

        return spikes;
    }

    public static List<Cannon> getCannonsFromLevelAsset(BufferedImage levelAsset) {
        var cannons = new ArrayList<Cannon>();

        for (int row = 0; row < levelAsset.getHeight(); row++) {
            for (int colunm = 0; colunm < levelAsset.getWidth(); colunm++) {
                Color pixelColor = new Color(levelAsset.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int cannon = pixelColor.getBlue();
                if (cannon == LevelObjectType.CANNON_RIGHT.ordinal()) {
                    cannons.add(new Cannon(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_RIGHT));
                } else if (cannon == LevelObjectType.CANNON_LEFT.ordinal()) { // todo make it a CANNON property, not ordinal() to ensure independence of the enum order
                    cannons.add(new Cannon(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_LEFT));
                }
            }
        }

        return cannons;
    }

    public static Point getPlayerSpawnPoint(BufferedImage levelAsset) {

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
            case PLAYER ->
                    new GravitySettings(Config.Player.AIR_SPEED, GRAVITY_FORCE, Config.Player.JUMP_SPEED, Config.Player.POST_COLLISION_FALL_SPEED);
            case ENEMY ->
                    new GravitySettings(Config.Enemy.AIR_SPEED, GRAVITY_FORCE, Config.Enemy.JUMP_SPEED, Config.Enemy.POST_COLLISION_FALL_SPEED);
        };
    }
}
