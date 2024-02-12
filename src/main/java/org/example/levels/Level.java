package org.example.levels;

import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.PinkStar;
import org.example.levelObjects.Container;
import org.example.levelObjects.*;
import org.example.types.EnemyType;
import org.example.types.LevelObjectType;
import org.example.types.MessageType;
import org.example.utils.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Level {

    private final BufferedImage levelAsset;
    private final List<Crabby> crabs = new ArrayList<>();
    private final List<PinkStar> pinkStars = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Container> containers = new ArrayList<>();
    private final List<Spike> spikes = new ArrayList<>();
    private final List<Cannon> cannons = new ArrayList<>();
    private final List<Grass> grassList = new ArrayList<>();
    private final List<Tree> trees = new ArrayList<>();
    private final List<BackTree> backTrees = new ArrayList<>();
    private final List<Shark> sharks = new ArrayList<>();
    // separating water types into 2 lists, since we need only waterWaves during entity downed check
    private final List<Water> waterBodyList = new ArrayList<>();
    private final List<Water> waterWaveList = new ArrayList<>();
    private final List<Flag> flags = new ArrayList<>();
    private final List<Ship> ships = new ArrayList<>();
    private Key key;
    private Chest chest;
    private Dialogue questionMark;
    private final List<Message> messages = new ArrayList<>();

    private int levelTilesCount;
    // represents how many tiles of the level remain unseen (i.e. for how many tiles it is possible to move the level to the left)
    private int maxLevelTilesOffset;
    // shows for how many tiles it is possible to move the level to the left in pixels
    private int maxLevelOffsetX;
    private int[][] levelData;
    private Point playerSpawnPosition;
    private Point levelExit;


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

    public Point getLevelExit() {
        return levelExit;
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

    public List<Tree> getTrees() {
        return trees;
    }

    public List<Shark> getSharks() {
        return sharks;
    }

    public List<Water> getWaterBodyList() {
        return waterBodyList;
    }

    public List<Water> getWaterWaveList() {
        return waterWaveList;
    }

    public List<PinkStar> getPinkStars() {
        return pinkStars;
    }

    public Dialogue getQuestionMark() {
        return questionMark;
    }

    public List<BackTree> getBackTrees() {
        return backTrees;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Chest getChest() {
        return chest;
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

        questionMark = new Dialogue(0, 0, LevelObjectType.QUESTION_MARK);
    }

    private void loadLevelData(int redValue, int x, int y) {

        // to avoid ArrayOutOfBounds when RED value is bigger than max index of outsideSpriteArray
        if (redValue >= (LevelManager.LVL_TEMPLATE_SPRITES_IN_WIDTH * LevelManager.LVL_TEMPLATE_SPRITES_IN_HEIGHT)) {
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
        if (greenValue == EnemyType.CRAB.getGreenPixelValue()) {
            crabs.add(new Crabby(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (greenValue == EnemyType.STAR.getGreenPixelValue()) {
            pinkStars.add(new PinkStar(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (greenValue == 100) {
            playerSpawnPosition = new Point(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize());
        }
    }

    private void loadObjects(int blueValue, int x, int y) {
        if (blueValue == LevelObjectType.BARREL.getBluePixelValue()) {
            containers.add(new Container(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.BARREL));
        } else if (blueValue == LevelObjectType.BOX.getBluePixelValue()) {
            containers.add(new Container(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.BOX));
        } else if (blueValue == LevelObjectType.CANNON_RIGHT.getBluePixelValue()) {
            cannons.add(new Cannon(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_RIGHT));
        } else if (blueValue == LevelObjectType.CANNON_LEFT.getBluePixelValue()) {
            cannons.add(new Cannon(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.CANNON_LEFT));
        } else if (blueValue == LevelObjectType.SPIKE.getBluePixelValue()) {
            spikes.add(new Spike(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.SPIKE));
        } else if (blueValue == LevelObjectType.POTION_BLUE.getBluePixelValue()) {
            potions.add(new Potion(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_BLUE));
        } else if (blueValue == LevelObjectType.POTION_RED.getBluePixelValue()) {
            potions.add(new Potion(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.POTION_RED));
        } else if (blueValue == LevelObjectType.TREE_STRAIGHT.getBluePixelValue()) {
            trees.add(new Tree(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.TREE_STRAIGHT));
        } else if (blueValue == LevelObjectType.TREE_BEND_RIGHT.getBluePixelValue()) {
            trees.add(new Tree(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.TREE_BEND_RIGHT));
        } else if (blueValue == LevelObjectType.TREE_BEND_LEFT.getBluePixelValue()) {
            trees.add(new Tree(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.TREE_BEND_LEFT));
        } else if (blueValue == LevelObjectType.BACK_TREE_STRAIGHT.getBluePixelValue()) {
            backTrees.add(new BackTree(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.BACK_TREE_STRAIGHT));
        } else if (blueValue == LevelObjectType.SHARK.getBluePixelValue()) {
            sharks.add(new Shark(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (blueValue == LevelObjectType.WATER_BODY.getBluePixelValue()) {
            waterBodyList.add(new Water(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.WATER_BODY));
        } else if (blueValue == LevelObjectType.WATER_WAVE.getBluePixelValue()) {
            waterWaveList.add(new Water(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), LevelObjectType.WATER_WAVE));
        } else if (blueValue == LevelObjectType.FLAG.getBluePixelValue()) {
            flags.add(new Flag(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (blueValue == LevelObjectType.KEY.getBluePixelValue()) {
            key = new Key(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize());
        } else if (blueValue == LevelObjectType.CHEST.getBluePixelValue()) {
            chest = new Chest(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize());
        } else if (blueValue == LevelObjectType.SHIP.getBluePixelValue()) {
            ships.add(new Ship(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize()));
        } else if (blueValue == LevelObjectType.MESSAGE_ONBOARDING.getBluePixelValue()) {
            messages.add(new Message(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), MessageType.ONBOARDING));
        } else if (blueValue == LevelObjectType.MESSAGE_KEY_FOUND.getBluePixelValue()) {
            messages.add(new Message(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize(), MessageType.KEY_FOUND));
        } else if (blueValue == 100) {
            levelExit = new Point(x * GamePanel.getCurrentTileSize(), y * GamePanel.getCurrentTileSize());
        }
    }

    private void calculateLevelOffsets() {
        levelTilesCount = levelAsset.getWidth();
        maxLevelTilesOffset = levelTilesCount - Game.TILE_VISIBLE_COUNT_WIDTH;
        maxLevelOffsetX = maxLevelTilesOffset * GamePanel.getCurrentTileSize();
    }

    public void resetLevelObjects() {
        containers.forEach(Container::reset);
        potions.forEach(Potion::reset);
        spikes.forEach(Spike::reset);
        cannons.forEach(Cannon::reset);
        trees.forEach(Tree::reset);
        backTrees.forEach(Tree::reset);
        if (Objects.nonNull(chest)) {
            chest.reset();
        }
        flags.forEach(Flag::reset);
        sharks.forEach(Shark::reset);
    }
}
