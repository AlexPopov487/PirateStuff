package org.example.entities;

import org.example.Config;
import org.example.gameState.Playing;
import org.example.utils.AtlasType;
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
    private final List<Crabby> crabbyList = ResourceLoader.getCrabs();


    public EnemyManager(Playing playing) {
        this.playing = playing;
        preloadEnemyImages();
    }

    public void render(Graphics graphics, Integer xLevelOffset) {
        renderCrabs(graphics, xLevelOffset);
    }

    public void update(int[][] levelData) {
        for (Crabby c : crabbyList) {
            if (!c.isActive()) continue;

            c.update(levelData, playing.getPlayer());
        }
    }

    public void checkEnemyGotHit(Rectangle2D.Float playerAttackRange) {
        for (Crabby crabby : crabbyList) {
            if (!crabby.isActive()) continue;

            if (playerAttackRange.intersects(crabby.getHitBox())) {
                crabby.takeDamage(10); // todo remove raw value
                break;
            }
        }
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
