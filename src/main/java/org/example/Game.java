package org.example;

import org.example.entities.Player;
import org.example.gameState.GameState;
import org.example.gameState.Menu;
import org.example.gameState.Playing;
import org.example.levels.LevelManager;
import org.example.utils.CollisionHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.Executors;

public class Game implements Runnable {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public final static int DEFAULT_TILE_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILE_COUNT_WIDTH = 26;
    public final static int TILE_COUNT_HEIGHT = 14;
    private final static int FPS = 120;
    private final static int UPS = 200;

    private final GamePanel gamePanel;
    private final Playing playing;
    private final Menu menu;

    public Game() {
        gamePanel = new GamePanel(this);
        menu = new Menu(this);
        playing = new Playing(this);
        GameWindow gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop(); // should be called last!

    }

    private void startGameLoop() {
        Executors.newSingleThreadExecutor().submit(this);
    }

    @Override
    public void run() {

        double oneSecInNano = 1_000_000_000.0;
        double frameTime = oneSecInNano / FPS;
        double updateTime = oneSecInNano / UPS;
        long lastFpsCheckTime = System.currentTimeMillis();

        long lastCheckTime = System.nanoTime();

        int currFpsCount = 0;
        int currUpsCount = 0;


        double deltaUps = 0.0;
        double deltaFps = 0.0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaUps += (currentTime - lastCheckTime) / updateTime;
            deltaFps += (currentTime - lastCheckTime) / frameTime;

            lastCheckTime = currentTime;
            if (deltaUps >= 1) {
                update();
                currUpsCount++;
                deltaUps--;
            }

            if (deltaFps >= 1) {
                gamePanel.repaint();

                currFpsCount++;
                deltaFps--;
            }


            // check for fps counter every second and reset counter
            long nowMillis = System.currentTimeMillis();
            if (nowMillis - lastFpsCheckTime >= 1000) {
                lastFpsCheckTime = nowMillis;
                log.debug("FPS is : {}, ups : {}", currFpsCount, currUpsCount);
                currFpsCount = 0;
                currUpsCount = 0;
            }

        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public void pauseGame() {
        if (GameState.PLAYING.equals(GameState.state)) {
            playing.getPlayer().getDirection().reset();
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case PLAYING -> playing.render(g);
            case MENU -> menu.render(g);
        }
    }

    private void update() {
        switch (GameState.state) {
            case PLAYING -> playing.update();
            case MENU -> menu.update();
            case OPTIONS -> {
            }
            case QUIT -> System.exit(0);
        }
    }
}
