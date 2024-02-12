package ru.alexp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.alexp.types.AtlasType;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        List<URL> levelFiles = getFilesUrlsFromDir("/levels");

        List<URL> levels = levelFiles.stream()
                .sorted(Comparator.comparing(URL::getFile))
                .toList();

        log.debug("ResourceLoader.getAllLevels(); found the following level files in /levels/ : [{}]", levels);
        return levels.stream().map(ResourceLoader::readImageFromFile).toList();
    }

    private static BufferedImage readImageFromFile(URL lvlFileUrl) {
        try {
            return ImageIO.read(lvlFileUrl);
        } catch (IOException e) {
            throw new RuntimeException(String.format("ResourceLoader.readImageFromFile(), failed to convert file to image, filename: [%s]", lvlFileUrl));
        }
    }

    public static Clip[] getSongs() {
        // NOTE that the file names should be prefixed with a number for correct sorting to ensure clip retrieval from array
        List<URL> songs = getFilesUrlsFromDir(AUDIO_PATH + SONG_PATH);

        return songs.stream()
                .sorted(Comparator.comparing(URL::getFile))
                .map(ResourceLoader::getClip)
                .toArray(Clip[]::new);
    }

    public static Clip[] getEffects() {
        // NOTE that the file names should be prefixed with a number for correct sorting to ensure clip retrieval from array
        List<URL> effects = getFilesUrlsFromDir(AUDIO_PATH + EFFECT_PATH);

        return effects.stream()
                .sorted(Comparator.comparing(URL::getFile))
                .map(ResourceLoader::getClip)
                .toArray(Clip[]::new);
    }

    private static Clip getClip(URL audioFileUrl) {
        Clip clip;
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(audioFileUrl)) {
            clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     It turned out there is a huge problem when trying to access resources from the packaged jar.
     Apparently, when inside a jar file, it is impossible to instantiate a File. Thus, the code below
     is used to correctly load the resources and support both IDE and a packages jar file run.
     The code is partially taken from here https://stackoverflow.com/a/28985785
    */
    private static List<URL> getFilesUrlsFromDir(String path) {
        List<String> filenames = getFilenamesFromDir(path);

        List<URL> urlList = filenames.stream()
                .filter(fileName -> Objects.nonNull(fileName) && !fileName.isBlank())
                .map(fileName -> path + "/" + fileName)
                .map(ResourceLoader.class::getResource)
                .toList();

        log.debug("getFilesUrlsFromDir() finished, path [{}], file urls [{}]", path, urlList);
        return urlList;
    }

    private static List<String> getFilenamesFromDir(String path) {
        log.debug("getFilenamesFromDir(), started, path [{}]", path);

        URL dirURL = ResourceLoader.class.getResource(path);
        List<String> filenames = new ArrayList<>();

        // If running from IDE
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            try {
                File dir = new File(dirURL.toURI());
                for (File file : Objects.requireNonNull(dir.listFiles())) {
                    filenames.add(file.getName());
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException("getFilenamesFromDir(), IDE run; failed to list files for URL " + dirURL, e);
            }
        } else if (dirURL == null) {
            // Handle case where the directory is a package in the classpath but not a File
            String me = ResourceLoader.class.getName().replace(".", "/") + ".class";
            dirURL = ResourceLoader.class.getResource(me);
        }

        // If running from JAR
        if (dirURL.getProtocol().equals("jar")) {
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            try (JarFile jar = new JarFile(java.net.URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();

                    // filter according to the path
                    if (name.startsWith(path.substring(1)) && !name.equals(path.substring(1))) {
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        if (!filenames.contains(entry)) {
                            filenames.add(entry);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("getFilenamesFromDir(), JAR run; failed to read jar file entries", e);
            }
        }

        log.debug("getFilenamesFromDir(), path [{}], dir file names [{}]", path, filenames);
        return filenames;
    }
}
