package ru.alexp.entities;

import ru.alexp.Config;
import ru.alexp.gameState.Playing;
import ru.alexp.levelObjects.Dialogue;
import ru.alexp.types.AtlasType;
import ru.alexp.utils.CollisionHelper;
import ru.alexp.utils.ResourceLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static ru.alexp.Config.Enemy.Crabby.DRAW_OFFSET_X;
import static ru.alexp.Config.Enemy.Crabby.DRAW_OFFSET_Y;


public class EnemyManager {
    private final Playing playing;
    private BufferedImage[][] crabAnimations;
    private BufferedImage[][] pinkStarAnimations;
    private BufferedImage[] questionMarkAnimations;

    public EnemyManager(Playing playing) {
        this.playing = playing;
        preloadEnemyImages();
    }

    public void render(Graphics graphics, Integer xLevelOffset) {
        renderCrabs(graphics, xLevelOffset);
        renderPinkStars(graphics, xLevelOffset);
    }

    public void update(int[][] levelData, Player player) {
        boolean isAnyActiveLeft = false;

        for (Crabby c : playing.getLevelManager().getCurrentLevel().getCrabs()) {
            if (!c.isActive()) continue;

            c.update(levelData, player);
            isAnyActiveLeft = true;
        }

        for (PinkStar pinkStar : playing.getLevelManager().getCurrentLevel().getPinkStars()) {
            if (!pinkStar.isActive()) continue;

            pinkStar.update(levelData, player);
            isAnyActiveLeft = true;
        }

        if (!isAnyActiveLeft) {
            playing.setReadyToCompleteLevel(true);
        }

        playing.getLevelManager().getCurrentLevel().getQuestionMark().update();

//        playing.getLevelManager().getCurrentLevel().getMessages().forEach(Message::update);
    }

    public void checkEnemyGotHit(Rectangle2D.Float playerAttackRange) {
        for (Crabby crabby : playing.getLevelManager().getCurrentLevel().getCrabs()) {
            if (!crabby.isActive() || crabby.getHeath().isDead()) continue;

            if (playerAttackRange.intersects(crabby.getHitBox())) {
                crabby.takeDamage(crabby.getHeath().getMaxHealth());
                return;
            }
        }

        for (PinkStar pinkStar : playing.getLevelManager().getCurrentLevel().getPinkStars()) {
            if (!pinkStar.isActive() || pinkStar.getHeath().isDead() || !pinkStar.isStunned()) continue;

            if (playerAttackRange.intersects(pinkStar.getHitBox())) {
                pinkStar.takeDamage(pinkStar.getHeath().getMaxHealth());
                return;
            }
        }
    }

    public void checkEnemyStumped(Player player) {
        for (Crabby crabby : playing.getLevelManager().getCurrentLevel().getCrabs()) {
            if (!crabby.isActive()) continue;

            if (CollisionHelper.isPlayerStumpsEnemy(player.getHitBox(), crabby.getHitBox())) {
                crabby.takeDamage(crabby.getHeath().getMaxHealth());
                break;
            }
        }
    }


    public void resetAll() {
        playing.getLevelManager().getCurrentLevel().getCrabs().forEach(Crabby::reset);
        playing.getLevelManager().getCurrentLevel().getPinkStars().forEach(PinkStar::reset);
    }

    private void preloadEnemyImages() {
        crabAnimations = new BufferedImage[5][9];
        var enemyCrabAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_ENEMY_CRAB);
        for (int row = 0; row < crabAnimations.length; row++) {
            for (int column = 0; column < crabAnimations[row].length; column++) {
                crabAnimations[row][column] = enemyCrabAtlas.getSubimage(column * Config.Enemy.Crabby.SPRITE_WIDTH_DEFAULT,
                        row * Config.Enemy.Crabby.SPRITE_HEIGHT_DEFAULT,
                        Config.Enemy.Crabby.SPRITE_WIDTH_DEFAULT,
                        Config.Enemy.Crabby.SPRITE_HEIGHT_DEFAULT);
            }
        }


        pinkStarAnimations = new BufferedImage[5][8];
        var pinkStarAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PINK_STAR);
        for (int row = 0; row < pinkStarAnimations.length; row++) {
            for (int column = 0; column < pinkStarAnimations[row].length; column++) {
                pinkStarAnimations[row][column] = pinkStarAtlas.getSubimage(column * Config.Enemy.Star.SPRITE_WIDTH_DEFAULT,
                        row * Config.Enemy.Star.SPRITE_HEIGHT_DEFAULT,
                        Config.Enemy.Star.SPRITE_WIDTH_DEFAULT,
                        Config.Enemy.Star.SPRITE_HEIGHT_DEFAULT);
            }
        }

        questionMarkAnimations = new BufferedImage[5];
        var questionMarkAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_QUESTION_MARK);
        for (int column = 0; column < questionMarkAnimations.length; column++) {
            questionMarkAnimations[column] = questionMarkAtlas.getSubimage(column * Config.DIALOGUE_DEFAULT_WIDTH,
                    0,
                    Config.DIALOGUE_DEFAULT_WIDTH,
                    Config.DIALOGUE_DEFAULT_HEIGHT);
        }

    }

    private void renderCrabs(Graphics graphics, int xLevelOffset) {
        for (Crabby crab : playing.getLevelManager().getCurrentLevel().getCrabs()) {
            if (!crab.isActive()) continue;

            int animationRowIndex = crab.getEnemyState().getAnimationRowIndex();
            graphics.drawImage(crabAnimations[animationRowIndex][crab.getAnimationIndex()],
                    (int) crab.getHitBox().x - xLevelOffset - DRAW_OFFSET_X + crab.getXFlip(),
                    (int) crab.getHitBox().y - DRAW_OFFSET_Y,
                    Config.Enemy.Crabby.SPRITE_WIDTH * crab.getWidthFlip(),
                    Config.Enemy.Crabby.SPRITE_HEIGHT,
                    null);

//            crab.drawAttackRangeBox(graphics, xLevelOffset);
        }
    }

    private void renderPinkStars(Graphics graphics, int xLevelOffset) {
        for (PinkStar star : playing.getLevelManager().getCurrentLevel().getPinkStars()) {
            if (!star.isActive()) continue;

            int animationRowIndex = star.getEnemyState().getAnimationRowIndex();
            graphics.drawImage(pinkStarAnimations[animationRowIndex][star.getAnimationIndex()],
                    (int) star.getHitBox().x - xLevelOffset - Config.Enemy.Star.DRAW_OFFSET_X + star.getXFlip(),
                    (int) star.getHitBox().y - Config.Enemy.Star.DRAW_OFFSET_Y,
                    Config.Enemy.Star.SPRITE_WIDTH * star.getWidthFlip(),
                    Config.Enemy.Star.SPRITE_HEIGHT,
                    null);


            if (star.isStunned()) {
                Dialogue questionMark = playing.getLevelManager().getCurrentLevel().getQuestionMark();
                int starCenterX = (int) (star.getHitBox().x - xLevelOffset - Config.Enemy.Star.DRAW_OFFSET_X + (star.getHitBox().width / 2));
                graphics.drawImage(questionMarkAnimations[questionMark.getAnimationIndex()],
                        starCenterX,
                        (int) star.getHitBox().y - Config.Enemy.Star.DRAW_OFFSET_Y - (Config.DIALOGUE_HEIGHT / 2),
                        Config.DIALOGUE_WIDTH,
                        Config.DIALOGUE_HEIGHT,
                        null);
            }
//            star.drawAttackRangeBox(graphics, xLevelOffset);
        }
    }
}
