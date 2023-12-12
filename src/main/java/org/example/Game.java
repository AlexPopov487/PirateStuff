package org.example;

import org.example.entities.Player;
import org.example.levels.LevelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.Executors;

public class Game implements Runnable{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // todo move to a separate util class
    public static final int CHARACTER_SPRITE_HEIGHT = 40;
    public static final int CHARACTER_SPRITE_WIDTH = 64;
    public final static int DEFAULT_TILE_SIZE = 32;
    public final static float SCALE = 1f;
    public final static int TILE_COUNT_WIDTH = 26;
    public final static int TILE_COUNT_HEIGHT = 14;
    private final static int FPS = 120;
    private final static int UPS = 200;

    private final GamePanel gamePanel;
    private final Player player;
    private final LevelManager levelManager;


    public Game() {
        levelManager = new LevelManager(this);
        player = new Player(200, 200, (int) (CHARACTER_SPRITE_WIDTH * SCALE), (int) (CHARACTER_SPRITE_HEIGHT * SCALE));
        player.setCurrentLevelData(levelManager.getCurrentLevel().getLevelData());

        gamePanel = new GamePanel(this);
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

    public void render(Graphics g) {
        levelManager.render(g);
        player.render(g);
    }

    public Player getPlayer() {
        return player;}

    public void pauseGame() {
        player.getDirection().reset();
    }

    private void update() {
        levelManager.update();
        player.update();
    }
}
