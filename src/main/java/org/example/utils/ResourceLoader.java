package org.example.utils;

import org.example.Game;
import org.example.levels.LevelManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ResourceLoader {

    private final static String RESOURCE_DIR_PATH = "/";
    public static BufferedImage getSpriteAtlas(AtlasType atlasType) {
        BufferedImage characterAtlas;

        try (InputStream is = ResourceLoader.class.getResourceAsStream(RESOURCE_DIR_PATH + atlasType.getAtlasFileName() ) ) {
            characterAtlas = ImageIO.read(is);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load an image", e);
        }

        return characterAtlas;
    }

    /*
     collect an array of indexes that are compatible with outsideSpriteArray from LevelManager.importOutsideSprite()
     The indexes in the levelDataArray are taken using level_*_data image, where each pixel's red color value serves
     as a tile index for the outsideData array.
    */
    public static int[][] getLevelData() {
        int[][] levelData = new int[Game.TILE_COUNT_HEIGHT][Game.TILE_COUNT_WIDTH];

        BufferedImage levelTemplate = getSpriteAtlas(AtlasType.ATLAS_LEVEL_ONE);

        for (int row = 0; row < Game.TILE_COUNT_HEIGHT; row++) {
            for (int colunm = 0; colunm < Game.TILE_COUNT_WIDTH; colunm++) {
                Color pixelColor = new Color(levelTemplate.getRGB(colunm, row));
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
}
