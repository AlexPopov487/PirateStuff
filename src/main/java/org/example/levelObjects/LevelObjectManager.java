package org.example.levelObjects;

import org.example.Config;
import org.example.entities.Heath;
import org.example.gameState.Playing;
import org.example.levels.Level;
import org.example.types.AtlasType;
import org.example.types.LevelObjectType;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class LevelObjectManager {
    private final Playing playing;
    private BufferedImage[][] potionAssets;
    private BufferedImage[][] containerAssets;
    private List<Potion> potions;
    private List<Container> containers;

    public LevelObjectManager(Playing playing) {
        this.playing = playing;
        preloadAssets(); // todo seems like managers have common behavior. Maybe a universal interface should be created
    }

    public void loadLevelObjects(Level level) {
        potions = level.getPotions();
        containers = level.getContainers();
    }

    public void update() {
        for (Potion potion : potions) {
            if (potion.isActive) {
                potion.update();
            }
        }

        for (Container container : containers) {
            if (container.isActive) {
                container.update();
            }
        }
    }

    public void render(Graphics g, int xLevelOffset) {
        renderPotions(g, xLevelOffset);
        renderContainers(g, xLevelOffset);
    }

    public void checkObjectCollected(Rectangle2D.Float playerHitBox) {
        // only potions can be collected
        for (Potion potion : potions) {
            if (!potion.isActive) continue;

            if (playerHitBox.intersects(potion.getHitBox())) {
                applyEffect(potion);
                potion.setActive(false);
            }
        }
    }

    public void checkObjectDestroyed(Rectangle2D.Float playerAttackRange) {
        for (Container container : containers) {
            if (!container.isActive) continue;

            if (playerAttackRange.intersects(container.getHitBox())) {
                container.setShouldAnimate(true);
                dropPotionFromContainer(container);
                return;
            }
        }
    }

    public void applyEffect(Potion potion) {
        if (LevelObjectType.POTION_RED.equals(potion.getObjectType())) {
            Heath playerHealth = playing.getPlayer().getHeath();
            playerHealth.setCurrentHeath(Config.LevelEnv.POTION_RED_VALUE + playerHealth.getCurrentHeath());
        } else if (LevelObjectType.POTION_BLUE.equals(potion.getObjectType())) {
            // todo change stamina
        }
    }

    public void resetAll() {
        containers.forEach(Container::reset);
        potions.forEach(Potion::reset);
    }

    private void renderPotions(Graphics g, int xLevelOffset) {
        for (Potion potion : potions) {
            if (!potion.isActive) continue;

            g.drawImage(potionAssets[potion.objectType.getSpriteIndex()][potion.getAnimationIndex()],
                    ((int) potion.getHitBox().x - potion.getxDrawOffset() - xLevelOffset),
                    ((int) potion.getHitBox().y - potion.getyDrawOffset()),
                    Config.LevelEnv.POTION_WIDTH,
                    Config.LevelEnv.POTION_HEIGHT,
                    null);
        }
    }

    private void renderContainers(Graphics g, int xLevelOffset) {
        for (Container container : containers) {
            if (!container.isActive) continue;

            g.drawImage(containerAssets[container.objectType.getSpriteIndex()][container.getAnimationIndex()],
                    ((int) container.getHitBox().x - container.getxDrawOffset() - xLevelOffset),
                    ((int) container.getHitBox().y - container.getyDrawOffset()),
                    Config.LevelEnv.CONTAINER_WIDTH,
                    Config.LevelEnv.CONTAINER_HEIGHT,
                    null);
        }
    }

    private void dropPotionFromContainer(Container container) {
        LevelObjectType droppedPotionType;
        if (LevelObjectType.BARREL.equals(container.getObjectType())) {
            droppedPotionType = LevelObjectType.POTION_RED;
        } else {
            droppedPotionType = LevelObjectType.POTION_BLUE;
        }
        potions.add(new Potion(container.getHitBox().x + container.getHitBox().width / 2,
                container.getHitBox().y - container.getHitBox().height / 4,
                droppedPotionType));
    }

    private void preloadAssets() {// TODO these method are all the same, should be moved to utils
        BufferedImage potionSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_POTION);
        potionAssets = new BufferedImage[2][7];

        for (int row = 0; row < potionAssets.length; row++) {
            for (int column = 0; column < potionAssets[row].length; column++) {
                potionAssets[row][column] = potionSprite.getSubimage(column * Config.LevelEnv.POTION_WIDTH_DEFAULT,
                        row * Config.LevelEnv.POTION_HEIGHT_DEFAULT,
                        Config.LevelEnv.POTION_WIDTH_DEFAULT,
                        Config.LevelEnv.POTION_HEIGHT_DEFAULT);
            }
        }

        BufferedImage containerSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_CONTAINER);
        containerAssets = new BufferedImage[2][8];

        for (int row = 0; row < containerAssets.length; row++) {
            for (int column = 0; column < containerAssets[row].length; column++) {
                containerAssets[row][column] = containerSprite.getSubimage(column * Config.LevelEnv.CONTAINER_WIDTH_DEFAULT,
                        row * Config.LevelEnv.CONTAINER_HEIGHT_DEFAULT,
                        Config.LevelEnv.CONTAINER_WIDTH_DEFAULT,
                        Config.LevelEnv.CONTAINER_HEIGHT_DEFAULT);
            }
        }
    }
}
