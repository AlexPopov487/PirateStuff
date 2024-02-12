package org.example.levels;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.exception.LoadNextLevelException;
import org.example.levelObjects.BackTree;
import org.example.levelObjects.Key;
import org.example.levelObjects.Tree;
import org.example.types.AtlasType;
import org.example.types.GameState;
import org.example.types.LevelObjectType;
import org.example.utils.Helper;
import org.example.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import static org.example.Game.DEFAULT_TILE_SIZE;
import static org.example.utils.ResourceLoader.getSpriteAtlas;

public class LevelManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public static final int LVL_TEMPLATE_SPRITES_IN_HEIGHT = 4;
    public static final int LVL_TEMPLATE_SPRITES_IN_WIDTH = 12;

    private final Game game;

    private List<Level> levels;
    private int waterAnimationIndex;
    private int animationTick;

    private int currentLevelIndex = 0;

    private BufferedImage[] backStraightTreesAssets;


    private BufferedImage[] levelSprites;

    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprite();
        importLevels();
        importBackgroundObjects();
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevelIndex);
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

        if (currentLevelIndex >= levels.size()) {
            setFirstLevel(); // todo add allLevelsCompleted overlay
            GameState.setState(GameState.MENU);
            throw new LoadNextLevelException("No more levels left! The game is completed!");
        }

        return levels.get(currentLevelIndex);
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
                    (int) ((int) backTree.getY() - Config.LevelEnv.BACK_TREE_STRAIGHT_HEIGHT + backTree.getCustomYOffset()),
                    Config.LevelEnv.TREE_STRAIGHT_WIDTH,
                    Config.LevelEnv.BACK_TREE_STRAIGHT_HEIGHT,
                    null);
        }
    }
}
