package org.example.utils;

import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Crabby;
import org.example.entities.EnemyType;
import org.example.levels.LevelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResourceLoader {

    private final static Logger log = LoggerFactory.getLogger(ResourceLoader.class);
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


    public static List<BufferedImage> getAllLevels() {

        File lvlDir;
        try {
            URL lvlDirUrl = ResourceLoader.class.getResource("/levels");
            assert lvlDirUrl != null;
            lvlDir = new File(lvlDirUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("getAllLevels(), failed to find levels directory", e);
        }

        List<File> levels = Arrays.stream(Objects.requireNonNull(lvlDir.listFiles())).sorted().toList();

        List<String> levelFileNames = levels.stream().map(File::getName).toList();
        log.debug("ResourceLoader.getAllLevels(); found the following level files in /levels/ : [{}]", levelFileNames);
        return levels.stream().map(ResourceLoader::readImageFromFile).toList();
    }

    private static BufferedImage readImageFromFile(File lvlFile) {
        try {
            return ImageIO.read(lvlFile);
        } catch (IOException e) {
            throw new RuntimeException(String.format("ResourceLoader.readImageFromFile(), failed to convert file to image, filename: [%s]", lvlFile.getName()));
        }
    }

}
