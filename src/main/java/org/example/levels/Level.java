package org.example.levels;

import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.levelObjects.*;
import org.example.levelObjects.Container;
import org.example.types.EnemyType;
import org.example.types.GrassType;
import org.example.types.LevelObjectType;
import org.example.utils.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private final BufferedImage levelAsset;
    private final List<Crabby> crabs = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Container> containers = new ArrayList<>();
    private final List<Spike> spikes = new ArrayList<>();
    private final List<Cannon> cannons =new ArrayList<>();
    private final List<Grass> grassList =new ArrayList<>();

    private int levelTilesCount;
    // represents how many tiles of the level remain unseen (i.e. for how many tiles it is possible to move the level to the left)
    private int maxLevelTilesOffset;
    // shows for how many tiles it is possible to move the level to the left in pixels
    private int maxLevelOffsetX;
    private int[][] levelData;
    private Point playerSpawnPosition;

    public Level(BufferedImage levelAsset) {
        this.levelAsset = levelAsset;
        levelData = new int[levelAsset.getHeight()][levelAsset.getWidth()];
        loadLevel();
        calculateLevelOffsets();
    }

    public int getLevelSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public List<Crabby> getCrabs() {
        return crabs;
    }

    public int getMaxLevelOffsetX() {
        return maxLevelOffsetX;
    }

    public Point getPlayerSpawnPosition() {
        return playerSpawnPosition;
    }

    public List<Potion> getPotions() {
        return potions;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public List<Spike> getSpikes() {
        return spikes;
    }

    public List<Cannon> getCannons() {
        return cannons;
    }

    public List<Grass> getGrassList() {
        return grassList;
    }

    private void loadLevel() {

        // Looping through the image colors just once. Instead of one per
        // object/enemy/etc..
        // Removed many methods in HelpMethods class.

        for (int y = 0; y < levelAsset.getHeight(); y++)
            for (int x = 0; x < levelAsset.getWidth(); x++) {
                Color c = new Color(levelAsset.getRGB(x, y));
                int red = c.getRed();
                int green = c.getGreen();
                int blue = c.getBlue();

                loadLevelData(red, x, y);
                loadEntities(green, x, y);
                loadObjects(blue, x, y);
            }
    }

    private void loadLevelData(int redValue, int x, int y) {

        // to avoid ArrayOutOfBounds when RED value is bigger than max index of outsideSpriteArray
        // + 2 is for the two water sprites
        if (redValue >= (LevelManager.LVL_TEMPLATE_SPRITES_IN_WIDTH * LevelManager.LVL_TEMPLATE_SPRITES_IN_HEIGHT) + 2) {
            redValue = 0;
        } else {
            levelData[y][x] = redValue;
        }

        switch (redValue) {
            case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 ->
                    grassList.add(new Grass(x * GamePanel.getCurrentTileSize(),
                            (y * GamePanel.getCurrentTileSize()) - GamePanel.getCurrentTileSize(),
                            Helper.getRandomGrassType()));
        }
    }

    private void loadEntities(int greenValue, int x, int y) {
        if (greenValue == EnemyType.CRAB.ordinal()) {
            crabs.add(new Crabby(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (greenValue == 100) {
            playerSpawnPosition = new Point(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize());
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        if (blueValue == LevelObjectType.BARREL.ordinal()) {
            containers.add(new Container(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.BARREL));
        } else if (blueValue == LevelObjectType.BOX.ordinal()) {
            containers.add(new Container(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.BOX));
        } else if (blueValue == LevelObjectType.CANNON_RIGHT.ordinal()) {
            cannons.add(new Cannon(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_RIGHT));
        } else if (blueValue == LevelObjectType.CANNON_LEFT.ordinal()) {
            cannons.add(new Cannon(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_LEFT));
        } else if (blueValue == LevelObjectType.SPIKE.ordinal()) {
            spikes.add(new Spike(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.SPIKE));
        } else if (blueValue == LevelObjectType.POTION_BLUE.ordinal()) {
            potions.add(new Potion(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_BLUE));
        } else if (blueValue == LevelObjectType.POTION_RED.ordinal()) {
            potions.add(new Potion(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_RED));
        }


        //todo add trees
//        switch (blueValue) {
//            case RED_POTION, BLUE_POTION ->
//                    potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
//            case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
//            case SPIKE -> spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
//            case CANNON_LEFT, CANNON_RIGHT ->
//                    cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
//            case TREE_ONE, TREE_TWO, TREE_THREE ->
//                    trees.add(new BackgroundTree(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
//        }
    }

    private void calculateLevelOffsets() {
//        levelTilesCount = levelData[0].length;
        levelTilesCount = levelAsset.getWidth();
        maxLevelTilesOffset = levelTilesCount - Game.TILE_VISIBLE_COUNT_WIDTH;
        maxLevelOffsetX = maxLevelTilesOffset * GamePanel.getCurrentTileSize();
    }

    public void resetLevelObjects() {
        containers.forEach(Container::reset);
        potions.forEach(Potion::reset);
        spikes.forEach(Spike::reset);
        cannons.forEach(Cannon::reset);
    }
}
