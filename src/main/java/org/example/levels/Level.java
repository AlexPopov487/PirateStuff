package org.example.levels;

import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.levelObjects.Container;
import org.example.levelObjects.Potion;
import org.example.levelObjects.Spike;
import org.example.utils.Helper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Level {

    private final BufferedImage levelAsset;
    private List<Crabby> crabs;
    private List<Potion> potions;
    private List<Container> containers;
    private List<Spike> spikes;

    private int levelTilesCount;
    // represents how many tiles of the level remain unseen (i.e. for how many tiles it is possible to move the level to the left)
    private int maxLevelTilesOffset;
    // shows for how many tiles it is possible to move the level to the left in pixels
    private int maxLevelOffsetX;
    private final int[][] levelData;
    private Point playerSpawnPosition;

    public Level(BufferedImage levelAsset) {
        this.levelAsset = levelAsset;
        levelData = Helper.getLevelDataFromAsset(levelAsset);
        prepareEnemies();
        preparePotions();
        prepareContainers();
        prepareSpikes();
        calculateLevelOffsets();
        calculatePlayerSpawnPosition();
    }

    public int getLevelSpriteIndex(int x, int y){
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

    private void calculatePlayerSpawnPosition() {
        playerSpawnPosition = Helper.getPlayerSpawnPoint(levelAsset);
    }

    private void prepareEnemies() {
        crabs = Helper.getCrabsFromLevelAsset(levelAsset);
    }

    private void calculateLevelOffsets() {
//        levelTilesCount = levelData[0].length;
        levelTilesCount = levelAsset.getWidth();
        maxLevelTilesOffset = levelTilesCount - Game.TILE_VISIBLE_COUNT_WIDTH;
        maxLevelOffsetX = maxLevelTilesOffset * GamePanel.getCurrentTileSize();
    }

    private void prepareContainers() {
        containers = Helper.getContainersFromLevelAsset(levelAsset);
    }

    private void preparePotions() {
        potions = Helper.getPotionsFromLevelAsset(levelAsset);
    }

    private void prepareSpikes() {
        spikes = Helper.getSpikesFromLevelAsset(levelAsset);
    }
}
