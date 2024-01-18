package org.example.utils;

import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.EnemyType;
import org.example.levels.LevelManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

        BufferedImage levelTemplate = getSpriteAtlas(AtlasType.ATLAS_LEVEL_ONE);
        int[][] levelData = new int[levelTemplate.getHeight()][levelTemplate.getWidth()];

        for (int row = 0; row < levelTemplate.getHeight(); row++) {
            for (int colunm = 0; colunm < levelTemplate.getWidth(); colunm++) {
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

    // Crabs are defined by pixels of green color on the level template image
    public static List<Crabby> getCrabs() {
        BufferedImage levelTemplate = getSpriteAtlas(AtlasType.ATLAS_LEVEL_ONE);

        var crabs = new ArrayList<Crabby>();
        for (int row = 0; row < levelTemplate.getHeight(); row++) {
            for (int colunm = 0; colunm < levelTemplate.getWidth(); colunm++) {
                Color pixelColor = new Color(levelTemplate.getRGB(colunm, row));
                // if we find a pixel where its green value = 0, we draw a crab on that specific position
                int crabBlockIndex = pixelColor.getGreen();
                if (crabBlockIndex == EnemyType.CRAB.ordinal()) { // todo make it a CRAB property, not ordinal() to ensure independence of the enum order

                    crabs.add(new Crabby(colunm * GamePanel.getCurrentTileSize(), row * GamePanel.getCurrentTileSize()));
                }
            }
        }

        return crabs;
    }
}
