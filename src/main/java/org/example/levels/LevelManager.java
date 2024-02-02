package org.example.levels;

import org.example.Game;
import org.example.GamePanel;
import org.example.types.GameState;
import org.example.types.AtlasType;
import org.example.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.example.Game.DEFAULT_TILE_SIZE;
import static org.example.utils.ResourceLoader.getSpriteAtlas;

public class LevelManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int LVL_TEMPLATE_SPRITES_IN_HEIGHT = 4;
    public static final int LVL_TEMPLATE_SPRITES_IN_WIDTH = 12;

    private final Game game;

    private List<Level> levels;
    private BufferedImage[] waterSprite;
    private int waterAnimationIndex;
    private int animationTick;

    private int currentLevelIndex = 0;


    private BufferedImage[] levelSprites;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprite();
        createWater();
        importLevels();
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void render(Graphics g, int xLevelOffset) {
        Level currLevel = getCurrentLevel();

        for (int row = 0; row < Game.TILE_VISIBLE_COUNT_HEIGHT; row++) {
            for (int column = 0; column < currLevel.getLevelData()[0].length; column++) {
                int index = currLevel.getLevelSpriteIndex(column, row);
                int x = GamePanel.getCurrentTileSize() * column - xLevelOffset;
                int y = GamePanel.getCurrentTileSize() * row;

                if (index == 48)
                    g.drawImage(waterSprite[waterAnimationIndex], x, y, GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize(), null);
                else if (index == 49)
                    g.drawImage(waterSprite[4], x, y, GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize(), null);
                else
                    g.drawImage(levelSprites[index], x, y, GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize(), null);
            }
        }
    }

    public void update() {
        updateWaterAnimation();
    }

    public void setFirstLevel() {
//        currentLevelIndex = 0; // todo
    }

    public Level loadNextLevel() {
        currentLevelIndex++;

        if (currentLevelIndex >= levels.size()) {
            setFirstLevel(); // todo add allLevelsCompleted overlay
            log.info("No more levels left! The game is completed!");
            GameState.setState(GameState.MENU);
        }

        return levels.get(currentLevelIndex);
    }

    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage img = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_WATER_TOP);
        for (int i = 0; i < 4; i++)
            waterSprite[i] = img.getSubimage(i * DEFAULT_TILE_SIZE, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
        waterSprite[4] = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_WATER);
    }

    private void importOutsideSprite() {

        BufferedImage levelAtlas = getSpriteAtlas(AtlasType.ATLAS_LEVEL_BLOCKS);
        // the level atlas is 12 sprites in width and 4 sprites in height. To store all sprites we need an array of 48 elements
        int indexCount = 0;
        levelSprites = new BufferedImage[LVL_TEMPLATE_SPRITES_IN_WIDTH * LVL_TEMPLATE_SPRITES_IN_HEIGHT];
        for (int row = 0; row < LVL_TEMPLATE_SPRITES_IN_HEIGHT; row++) {
            for (int column = 0; column < LVL_TEMPLATE_SPRITES_IN_WIDTH; column++) {

                levelSprites[indexCount] = levelAtlas.getSubimage(column * DEFAULT_TILE_SIZE,
                        row * DEFAULT_TILE_SIZE,
                        DEFAULT_TILE_SIZE,
                        DEFAULT_TILE_SIZE);

                indexCount++;
            }
        }
    }

    private void importLevels() {
        levels = ResourceLoader.getAllLevels().stream()
                .map(Level::new)
                .toList();
    }

    private void updateWaterAnimation() {
        animationTick++;
        if (animationTick >= 40) {
            animationTick = 0;
            waterAnimationIndex++;

            if (waterAnimationIndex >= 4)
                waterAnimationIndex = 0;
        }
    }
}
