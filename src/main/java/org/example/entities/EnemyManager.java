package org.example.entities;

import org.example.Config;
import org.example.gameState.Playing;
import org.example.levels.Level;
import org.example.types.EnemyType;
import org.example.types.AtlasType;
import org.example.utils.CollisionHelper;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.example.Config.Enemy.CRAB_DRAW_OFFSET_X;
import static org.example.Config.Enemy.CRAB_DRAW_OFFSET_Y;

public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] enemyCrabAnimations;
    private List<Crabby> crabbyList;


    public EnemyManager(Playing playing) {
        this.playing = playing;
        preloadEnemyImages();
    }

    public void render(Graphics graphics, Integer xLevelOffset) {
        renderCrabs(graphics, xLevelOffset);
    }

    public void update(int[][] levelData, Player player) {
        boolean isAnyActiveLeft = false;

        for (Crabby c : crabbyList) {
            if (!c.isActive()) continue;

            c.update(levelData, player);
            isAnyActiveLeft = true;
        }

        if (!isAnyActiveLeft) {
            playing.setCurrLevelCompleted(true);
        }
    }

    public void checkEnemyGotHit(Rectangle2D.Float playerAttackRange) {
        for (Crabby crabby : crabbyList) {
            if (!crabby.isActive()) continue;

            if (playerAttackRange.intersects(crabby.getHitBox())) {
                crabby.takeDamage(Config.Enemy.CRAB_DAMAGE);
                break;
            }
        }
    }

    public void checkEnemyStumped(Player player) {
        for (Crabby crabby : crabbyList) {
            if (!crabby.isActive()) continue;

            if (CollisionHelper.isPlayerStumpsEnemy(player.getHitBox(), crabby.getHitBox())) {
                //todo play punch sound
                crabby.takeDamage(Config.Enemy.CRAB_DAMAGE);
                break;
            }
        }
    }

    // called from Playing class, since enemies should be reloaded every time the next level is started
    public void loadEnemies(Level currentLevel) {
        crabbyList = currentLevel.getCrabs();
    }


    public void resetAll() {
        crabbyList.forEach(Crabby::reset);
    }

    private void preloadEnemyImages() {
        enemyCrabAnimations = new BufferedImage[5][9];

        var enemyCrabAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_ENEMY_CRAB);

        for (int row = 0; row < enemyCrabAnimations.length; row++) {
            for (int column = 0; column < enemyCrabAnimations[row].length; column++) {
                enemyCrabAnimations[row][column] = enemyCrabAtlas.getSubimage(column * Config.Enemy.CRAB_SPRITE_WIDTH_DEFAULT,
                        row * Config.Enemy.CRAB_SPRITE_HEIGHT_DEFAULT,
                        Config.Enemy.CRAB_SPRITE_WIDTH_DEFAULT,
                        Config.Enemy.CRAB_SPRITE_HEIGHT_DEFAULT);
            }
        }
    }

    private void renderCrabs(Graphics graphics, int xLevelOffset) {
        for (Crabby crab : crabbyList) {
            if (!crab.isActive()) continue;

            int animationRowIndex = Config.Enemy.getSpriteAnimationRowIndex(EnemyType.CRAB, crab.getEnemyState());
            graphics.drawImage(enemyCrabAnimations[animationRowIndex][crab.getAnimationIndex()],
                    (int) crab.getHitBox().x - xLevelOffset - CRAB_DRAW_OFFSET_X + crab.getXFlip(),
                    (int) crab.getHitBox().y - CRAB_DRAW_OFFSET_Y,
                    Config.Enemy.CRAB_SPRITE_WIDTH * crab.getWidthFlip(),
                    Config.Enemy.CRAB_SPRITE_HEIGHT,
                    null);

//            crab.drawAttackRangeBox(graphics, xLevelOffset);
        }
    }
}
