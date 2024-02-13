package ru.alexp.levels;

import ru.alexp.Config;
import ru.alexp.Game;
import ru.alexp.GamePanel;
import ru.alexp.exception.LoadNextLevelException;
import ru.alexp.levelObjects.BackTree;
import ru.alexp.levelObjects.Key;
import ru.alexp.levelObjects.Tree;
import ru.alexp.types.AtlasType;
import ru.alexp.types.GameState;
import ru.alexp.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

public class LevelManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int LVL_TEMPLATE_SPRITES_IN_HEIGHT = 4;
    public static final int LVL_TEMPLATE_SPRITES_IN_WIDTH = 12;

    private final int levelCount = ResourceLoader.getAllLevelsCount();
    private Level currentLevel;
    private int currentLevelIndex = 0;

    private BufferedImage[] backStraightTreesAssets;


    private BufferedImage[] levelSprites;

    public LevelManager() {
        importOutsideSprite();
        importCurrentLevel();
        importBackgroundObjects();
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public void render(Graphics g, int xLevelOffset) {
        Level currLevel = getCurrentLevel();
        renderBackgroundObjects(g, xLevelOffset, currLevel);

        for (int row = 0; row < Game.TILE_VISIBLE_COUNT_HEIGHT; row++) {
            for (int column = 0; column < currLevel.getLevelData()[0].length; column++) {
                int index = currLevel.getLevelSpriteIndex(column, row);
                int x = GamePanel.getCurrentTileSize() * column - xLevelOffset;
                int y = GamePanel.getCurrentTileSize() * row;

                g.drawImage(levelSprites[index], x, y, GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize(), null);
            }
        }
    }

    public void update() {
        updateBackgroundObjects();
    }

    public void setFirstLevel() {
        currentLevelIndex = 0;
        Key key = getCurrentLevel().getKey();
        if (Objects.nonNull(key)) {
            key.reset();
        }
    }

    public Level loadNextLevel() throws LoadNextLevelException {
        currentLevelIndex++;

        if (currentLevelIndex >= levelCount) {
            setFirstLevel(); // todo add allLevelsCompleted overlay
            GameState.setState(GameState.MENU);
            throw new LoadNextLevelException("No more levels left! The game is completed!");
        }

        BufferedImage levelImage = ResourceLoader.getLevelImageByIndex(currentLevelIndex);
        currentLevel = new Level(levelImage);
        return currentLevel;
    }

    private void importOutsideSprite() {

        BufferedImage levelAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_LEVEL_BLOCKS);
        // the level atlas is 12 sprites in width and 4 sprites in height. To store all sprites we need an array of 48 elements
        int indexCount = 0;
        levelSprites = new BufferedImage[LVL_TEMPLATE_SPRITES_IN_WIDTH * LVL_TEMPLATE_SPRITES_IN_HEIGHT];
        for (int row = 0; row < LVL_TEMPLATE_SPRITES_IN_HEIGHT; row++) {
            for (int column = 0; column < LVL_TEMPLATE_SPRITES_IN_WIDTH; column++) {

                levelSprites[indexCount] = levelAtlas.getSubimage(column * Game.DEFAULT_TILE_SIZE,
                        row * Game.DEFAULT_TILE_SIZE,
                        Game.DEFAULT_TILE_SIZE,
                        Game.DEFAULT_TILE_SIZE);

                indexCount++;
            }
        }
    }

    private void importCurrentLevel() {
        currentLevel = new Level(ResourceLoader.getLevelImageByIndex(currentLevelIndex));
    }

    private void importBackgroundObjects() {
        BufferedImage backStraightTreeSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_BACK_TREE_STRAIGHT);
        backStraightTreesAssets = new BufferedImage[4];

        for (int column = 0; column < backStraightTreesAssets.length; column++) {
            backStraightTreesAssets[column] = backStraightTreeSprite.getSubimage(column * Config.LevelEnv.TREE_STRAIGHT_WIDTH_DEFAULT,
                    0,
                    Config.LevelEnv.TREE_STRAIGHT_WIDTH_DEFAULT,
                    Config.LevelEnv.BACK_TREE_STRAIGHT_HEIGHT_DEFAULT);
        }
    }

    private void updateBackgroundObjects() {
        getCurrentLevel().getBackTrees().forEach(Tree::update);
    }

    private void renderBackgroundObjects(Graphics g, int xLevelOffset, Level currLevel) {
        for (BackTree backTree : currLevel.getBackTrees()) {
            int currentX = (int) (backTree.getX() - xLevelOffset);

            g.drawImage(
                    backStraightTreesAssets[backTree.getAnimationIndex()],
                    currentX,
                    (int) backTree.getY() - Config.LevelEnv.BACK_TREE_STRAIGHT_HEIGHT + backTree.getCustomYOffset(),
                    Config.LevelEnv.TREE_STRAIGHT_WIDTH,
                    Config.LevelEnv.BACK_TREE_STRAIGHT_HEIGHT,
                    null);
        }
    }
}
