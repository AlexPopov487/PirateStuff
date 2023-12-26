package org.example.levels;

import org.example.Game;
import org.example.GamePanel;
import org.example.utils.AtlasType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.example.Game.DEFAULT_TILE_SIZE;
import static org.example.utils.ResourceLoader.getSpriteAtlas;

public class LevelManager {
    public static final int LVL_TEMPLATE_SPRITES_IN_HEIGHT = 4;
    public static final int LVL_TEMPLATE_SPRITES_IN_WIDTH = 12;

    private final Game game;

    private final Level levelOne;

    private BufferedImage[] levelSprites;
    public LevelManager(Game game) {
        this.game = game;
        importOutsideSprite();
//        this.levelSprite = getSpriteAtlas(AtlasType.ATLAS_LEVEL);
        levelOne = new Level(ResourceLoader.getLevelData());

    }

    public void render(Graphics g, int xLevelOffset) {
        for (int row = 0; row < Game.TILE_VISIBLE_COUNT_HEIGHT; row++) {
            for (int column = 0; column < levelOne.getLevelData()[0].length; column++) {
                int index = levelOne.getLevelSpriteIndex(column, row);

                g.drawImage(levelSprites[index],
                        (column * GamePanel.getCurrentTileSize()) - xLevelOffset,
                        row * GamePanel.getCurrentTileSize(),
                        GamePanel.getCurrentTileSize(),
                        GamePanel.getCurrentTileSize(),
                        null);
            }
        }
    }

    public void update(){

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

    public Level getCurrentLevel() {
        return levelOne;
    }
}
