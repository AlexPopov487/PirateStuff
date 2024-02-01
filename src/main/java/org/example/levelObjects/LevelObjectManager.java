package org.example.levelObjects;

import org.example.Config;
import org.example.entities.Health;
import org.example.entities.Player;
import org.example.gameState.Playing;
import org.example.levels.Level;
import org.example.types.AtlasType;
import org.example.types.LevelObjectType;
import org.example.utils.CollisionHelper;
import org.example.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LevelObjectManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Playing playing;
    private BufferedImage[][] potionAssets;
    private BufferedImage[][] containerAssets;
    private BufferedImage spikeAsset;
    private BufferedImage[] cannonAssets;
    private BufferedImage projectileAsset;
    // todo this is not ok that potions, containers and enemies arrays are stored twice (in Level and ObjectManager/EnemyManager)
    private List<Potion> potions;
    private List<Container> containers;
    private List<Spike> spikes;
    private List<Cannon> cannons;
    private final List<Projectile> projectiles = new ArrayList<>();

    public LevelObjectManager(Playing playing) {
        this.playing = playing;
        preloadAssets(); // todo seems like managers have common behavior. Maybe a universal interface should be created
    }

    public void loadLevelObjects(Level level) {
        potions = new ArrayList<>(level.getPotions());
        containers = new ArrayList<>(level.getContainers());
        spikes = new ArrayList<>(level.getSpikes());
        cannons = new ArrayList<>(level.getCannons());
        projectiles.clear();
    }

    public void update(int[][] levelData, Player player) {
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

        updateCannons(levelData, player);
        updateProjectiles(levelData, player);
    }

    public void render(Graphics g, int xLevelOffset) {
        renderPotions(g, xLevelOffset);
        renderContainers(g, xLevelOffset);
        renderSpikeTraps(g, xLevelOffset);
        renderCannons(g, xLevelOffset);
        renderProjectiles(g, xLevelOffset);
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
            // the container.shouldAnimate check ensures that the object can be hit only once
            if (!container.isActive || container.shouldAnimate) continue;

            if (playerAttackRange.intersects(container.getHitBox())) {
                container.setShouldAnimate(true);
                dropPotionFromContainer(container);
                return;
            }
        }
    }

    public void checkSpikeTrapTouched(Rectangle2D.Float playerHitBox) {
        for (Spike spike : spikes) {
            if (!spike.isActive) continue;

            if (playerHitBox.intersects(spike.hitBox)) {
                playing.getPlayer().getHeath().setCurrentHeath(0);
            }
        }
    }

    public void applyEffect(Potion potion) {
        if (LevelObjectType.POTION_RED.equals(potion.getObjectType())) {
            Health playerHealth = playing.getPlayer().getHeath();
            playerHealth.setCurrentHeath(Config.LevelEnv.POTION_RED_VALUE + playerHealth.getCurrentHealth());
        } else if (LevelObjectType.POTION_BLUE.equals(potion.getObjectType())) {
            // todo change stamina
        }
    }

    public void resetAll() {
        loadLevelObjects(playing.getLevelManager().getCurrentLevel());

        containers.forEach(Container::reset);
        potions.forEach(Potion::reset);
        spikes.forEach(Spike::reset);
        cannons.forEach(Cannon::reset);
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

    private void renderSpikeTraps(Graphics g, int xLevelOffset) {
        for (Spike spike : spikes) {
            if (!spike.isActive) continue;

            g.drawImage(spikeAsset,
                    ((int) spike.getHitBox().x - spike.getxDrawOffset() - xLevelOffset),
                    ((int) spike.getHitBox().y - spike.getyDrawOffset()),
                    Config.LevelEnv.SPIKE_WIDTH,
                    Config.LevelEnv.SPIKE_HEIGHT,
                    null);
        }
    }

    private void renderCannons(Graphics g, int xLevelOffset) {
        for (Cannon cannon : cannons) {
            int currentX = (int) (cannon.getHitBox().x - xLevelOffset);
            int currentWidth = Config.LevelEnv.CANNON_WIDTH;

            // Flip image if needed
            if (LevelObjectType.CANNON_RIGHT.equals(cannon.getObjectType())) {
                currentX += currentWidth;
                currentWidth = currentWidth * -1;
            }

            g.drawImage(cannonAssets[cannon.getAnimationIndex()],
                    currentX,
                    (int) cannon.getHitBox().y,
                    currentWidth,
                    Config.LevelEnv.CANNON_HEIGHT,
                    null);
        }
    }

    private void renderProjectiles(Graphics g, int xLevelOffset) {
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive) continue;

            g.drawImage(projectileAsset, (int) (projectile.getHitBox().x - xLevelOffset),
                    (int) projectile.getHitBox().y,
                    Config.LevelEnv.CANNON_BALL_WIDTH,
                    Config.LevelEnv.CANNON_BALL_HEIGHT,
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

        playing.getGame().getAudioPlayer().playEffect(Config.Audio.POISON_FOUND_EFFECT_INDEX);
    }

    private void updateCannons(int[][] levelData, Player player) {
        for (Cannon cannon : cannons) {
            if (System.currentTimeMillis() - cannon.getLastShotMillis() < Cannon.SHOOT_DELAY_MILLIS) continue;

            if (!cannon.shouldAnimate && cannon.canSeePlayer(levelData, player)) {
                cannon.setShouldAnimate(true);
            }

            cannon.update();
            shootCannon(cannon);
        }
    }

    private void updateProjectiles(int[][] levelData, Player player) {
        for (Projectile projectile : projectiles) {
            if (!projectile.isActive) continue;
            projectile.updatePosition();

            if (projectile.getHitBox().intersects(player.getHitBox())) {
                player.getHeath().subtractHealth(30);
                projectile.setActive(false); // todo probably there should be one last animation of a ball explosion
            } else if (CollisionHelper.hasProjectileHitObstacle(projectile.getHitBox(), levelData)) {
                projectile.setActive(false);
            }
        }
    }

    private void shootCannon(Cannon cannon) {
        // Do not shoot the cannon right away. Instead, what for cannon shooting animation to begin and only then shoot
        if (cannon.getAnimationIndex() == 4 && cannon.getAnimationTick() == 0) {
            projectiles.add(new Projectile(cannon.getHitBox().x, cannon.getHitBox().y, LevelObjectType.PROJECTILE, cannon.objectType));
            playing.getGame().getAudioPlayer().playEffect(Config.Audio.CANNON_EFFECT_INDEX);

            cannon.setLastShotMillis(System.currentTimeMillis());
        }
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

        spikeAsset = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_SPIKE_TRAP);


        BufferedImage cannonSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_CANNON);
        cannonAssets = new BufferedImage[7];

        for (int column = 0; column < cannonAssets.length; column++) {
            cannonAssets[column] = cannonSprite.getSubimage(column * Config.LevelEnv.CANNON_WIDTH_DEFAULT,
                    0,
                    Config.LevelEnv.CANNON_WIDTH_DEFAULT,
                    Config.LevelEnv.CANNON_HEIGHT_DEFAULT);
        }


        projectileAsset = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PROJECTILE);
    }
}

