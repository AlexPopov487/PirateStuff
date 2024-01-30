package org.example.utils;

import org.example.types.AtlasType;
import org.example.types.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ResourceLoader {

    private final static Logger log = LoggerFactory.getLogger(ResourceLoader.class);
    private final static String RESOURCE_DIR_PATH = "/";
    private final static String AUDIO_PATH = "/audio";
    private final static String SONG_PATH = "/songs";
    private final static String EFFECT_PATH = "/effects";

    public static BufferedImage getSpriteAtlas(AtlasType atlasType) {
        BufferedImage characterAtlas;

        try (InputStream is = ResourceLoader.class.getResourceAsStream(RESOURCE_DIR_PATH + atlasType.getAtlasFileName())) {
            characterAtlas = ImageIO.read(is);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load an image", e);
        }
        return characterAtlas;
    }


    public static List<BufferedImage> getAllLevels() {
        File[] levelFiles = getAllFilesFromDir("/levels");

        List<File> levels = Arrays.stream(levelFiles).sorted().toList();

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

    public static Clip[] getSongs() {
        // NOTE that the file names should be prefixed with a number for correct sorting to ensure clip retrieval from array
        File[] songs = getAllFilesFromDir(AUDIO_PATH + SONG_PATH);

        return  Arrays.stream(songs)
                .sorted()
                .map(ResourceLoader::getClip)
                .toArray(Clip[]::new);
    }

    public static Clip[] getEffects() {
        // NOTE that the file names should be prefixed with a number for correct sorting to ensure clip retrieval from array
        File[] effects = getAllFilesFromDir(AUDIO_PATH + EFFECT_PATH);

        return Arrays.stream(effects)
                .sorted()
                .map(ResourceLoader::getClip)
                .toArray(Clip[]::new);
    }

    private static Clip getClip(File audioFile) {
        Clip clip;
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(audioFile)) {
            clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private static File[] getAllFilesFromDir(String path) {
        File dir;
        try {
            URL songsDirUrl = ResourceLoader.class.getResource(path);
            assert songsDirUrl != null;
            dir = new File(songsDirUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to find files by path " + path, e);
        }
        File[] files = Objects.requireNonNull(dir.listFiles());
        log.debug("getAllFilesFromDir(), found following files in dir [{}] : [{}]", path, files);
        return files;
    }
}
