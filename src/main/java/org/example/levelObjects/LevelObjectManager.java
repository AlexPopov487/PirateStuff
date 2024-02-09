package org.example.levelObjects;

import org.example.Config;
import org.example.GamePanel;
import org.example.entities.Health;
import org.example.entities.Player;
import org.example.entities.Stamina;
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

import static org.example.Game.DEFAULT_TILE_SIZE;

public class LevelObjectManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Playing playing;
    private BufferedImage[][] potionAssets;
    private BufferedImage[][] containerAssets;
    private BufferedImage spikeAsset;
    private BufferedImage[] cannonAssets;
    private BufferedImage[] grassAssets;
    private BufferedImage[] straightTreesAssets;
    private BufferedImage[] bendTreesAssets;
    private BufferedImage projectileAsset;
    private BufferedImage sharkAsset;
    private BufferedImage[] waterAsset;
    private BufferedImage[] flagAsset;

    private final List<Projectile> projectiles = new ArrayList<>();

    public LevelObjectManager(Playing playing) {
        this.playing = playing;
        preloadAssets(); // todo seems like managers have common behavior. Maybe a universal interface should be created
    }

    public void loadLevelObjects(Level level) {
        projectiles.clear();
    }

    public void update(int[][] levelData, Player player) {
        for (Potion potion : playing.getLevelManager().getCurrentLevel().getPotions()) {
            if (potion.isActive) {
                potion.update();
            }
        }

        for (Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
            if (container.isActive) {
                container.update();
            }
        }

        for (Tree tree : playing.getLevelManager().getCurrentLevel().getTrees()) {
            tree.update();
        }

        for (Shark shark : playing.getLevelManager().getCurrentLevel().getSharks()) {
            shark.updatePosition(playing.getLevelManager().getCurrentLevel().getLevelData());
        }

        for (Flag flag : playing.getLevelManager().getCurrentLevel().getFlags()) {
            flag.update();
        }

        updateWater();


        updateCannons(levelData, player);
        updateProjectiles(levelData, player);
    }

    public void render(Graphics g, int xLevelOffset) {
        renderPotions(g, xLevelOffset);
        renderContainers(g, xLevelOffset);
        renderSpikeTraps(g, xLevelOffset);
        renderCannons(g, xLevelOffset);
        renderProjectiles(g, xLevelOffset);
        renderGrass(g, xLevelOffset);
        renderTrees(g, xLevelOffset);
        renderSharks(g, xLevelOffset);
        renderWater(g, xLevelOffset);
        renderFlag(g, xLevelOffset);
    }

    public void checkObjectCollected(Rectangle2D.Float playerHitBox) {
        // only potions can be collected
        for (Potion potion : playing.getLevelManager().getCurrentLevel().getPotions()) {
            if (!potion.isActive) continue;

            if (playerHitBox.intersects(potion.getHitBox())) {
                if (applyEffect(potion)) {
                    potion.setActive(false);
                }
            }
        }
    }

    public void checkObjectDestroyed(Rectangle2D.Float playerAttackRange) {
        for (Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
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
        for (Spike spike : playing.getLevelManager().getCurrentLevel().getSpikes()) {
            if (!spike.isActive) continue;

            if (playerHitBox.intersects(spike.hitBox)) {
                playing.getPlayer().getHeath().setCurrentHeath(0);
            }
        }
    }

    public void checkDrowned(Rectangle2D.Float playerHitBox) {
        for (Water water : playing.getLevelManager().getCurrentLevel().getWaterWaveList()) {
            if (water.getHitBox().intersects(playerHitBox)) {
                playing.getPlayer().getHeath().setCurrentHeath(0);
            }
        }
    }

    public boolean applyEffect(Potion potion) {
        if (LevelObjectType.POTION_RED.equals(potion.getObjectType())) {
            Health playerHealth = playing.getPlayer().getHeath();

            if (playerHealth.getCurrentHealth() == playing.getPlayer().getHeath().getMaxHealth()) {
                return false;
            }
            playerHealth.setCurrentHeath(Config.LevelEnv.POTION_RED_VALUE + playerHealth.getCurrentHealth());
            return true;
        } else if (LevelObjectType.POTION_BLUE.equals(potion.getObjectType())) {
            Stamina playerStamina = playing.getPlayer().getStamina();
            if (playerStamina.getCurrentValue() == Config.StatusBar.STAMINA_MAX_VALUE) {
                return false;
            }
            playerStamina.incCurrentValue(Config.LevelEnv.POTION_BLUE_VALUE);
            return true;
        }
        return false;
    }

    public void resetAll() {
        loadLevelObjects(playing.getLevelManager().getCurrentLevel());

        playing.getLevelManager().getCurrentLevel().resetLevelObjects();
    }

    private void renderPotions(Graphics g, int xLevelOffset) {
        for (Potion potion : playing.getLevelManager().getCurrentLevel().getPotions()) {
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
        for (Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
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
        for (Spike spike : playing.getLevelManager().getCurrentLevel().getSpikes()) {
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
        for (Cannon cannon : playing.getLevelManager().getCurrentLevel().getCannons()) {
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

    private void renderGrass(Graphics g, int xLevelOffset) {
        for (Grass grass : playing.getLevelManager().getCurrentLevel().getGrassList()) {
            int currentX = (int) (grass.getX() - xLevelOffset);

            g.drawImage(grassAssets[grass.getGrassType().ordinal()],
                    currentX,
                    (int) grass.getY(),
                    Config.LevelEnv.GRASS_WIDTH,
                    Config.LevelEnv.GRASS_HEIGHT,
                    null);
        }
    }

    private void renderTrees(Graphics g, int xLevelOffset) {
        for (Grass grass : playing.getLevelManager().getCurrentLevel().getGrassList()) {
            int currentX = (int) (grass.getX() - xLevelOffset);

            g.drawImage(grassAssets[grass.getGrassType().ordinal()],
                    currentX,
                    (int) grass.getY(),
                    Config.LevelEnv.GRASS_WIDTH,
                    Config.LevelEnv.GRASS_HEIGHT,
                    null);
        }

        for (Tree tree : playing.getLevelManager().getCurrentLevel().getTrees()) {
            int currentX = (int) (tree.getX() - xLevelOffset);

            if (LevelObjectType.TREE_STRAIGHT.equals(tree.getObjectType())) {
                g.drawImage(
                        straightTreesAssets[tree.getAnimationIndex()],
                        currentX,
                        (int) tree.getY() - Config.LevelEnv.TREE_STRAIGHT_HEIGHT + (GamePanel.getCurrentTileSize() * 2),
                        Config.LevelEnv.TREE_STRAIGHT_WIDTH,
                        Config.LevelEnv.TREE_STRAIGHT_HEIGHT,
                        null);
            } else if (LevelObjectType.TREE_BEND_RIGHT.equals(tree.getObjectType())) {
                g.drawImage(bendTreesAssets[tree.getAnimationIndex()],
                        (int) (currentX + (GamePanel.getCurrentTileSize() / 2.5f)),
                        (int) ((int) tree.getY() - Config.LevelEnv.TREE_BEND_HEIGHT + (GamePanel.getCurrentTileSize() / 1.25)),
                        Config.LevelEnv.TREE_BEND_WIDTH,
                        Config.LevelEnv.TREE_BEND_HEIGHT,
                        null);
            } else if (LevelObjectType.TREE_BEND_LEFT.equals(tree.getObjectType())) {
                g.drawImage(bendTreesAssets[tree.getAnimationIndex()],
                        (int) (currentX + (GamePanel.getCurrentTileSize() / 1.65f)),
                        (int) ((int) tree.getY() - Config.LevelEnv.TREE_BEND_HEIGHT + (GamePanel.getCurrentTileSize() / 1.25)),
                        -Config.LevelEnv.TREE_BEND_WIDTH,
                        Config.LevelEnv.TREE_BEND_HEIGHT,
                        null);
            }
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

    private void renderSharks(Graphics g, int xLevelOffset) {
        for (Shark shark : playing.getLevelManager().getCurrentLevel().getSharks()) {


            g.drawImage(sharkAsset,
                    (int) shark.getHitBox().x - xLevelOffset - Config.LevelEnv.SHARK_DRAW_OFFSET_X + shark.getXFlip(),
                    (int) shark.getHitBox().y + Config.LevelEnv.SHARK_DRAW_OFFSET_Y,
                    Config.LevelEnv.SHARK_WIDTH * shark.getWidthFlip(),
                    Config.LevelEnv.SHARK_HEIGHT,
                    null);
        }
    }

    private void renderWater(Graphics g, int xLevelOffset) {

        for (Water water : playing.getLevelManager().getCurrentLevel().getWaterBodyList()) {

            g.drawImage(waterAsset[waterAsset.length - 1],
                    (int) water.getHitBox().x - xLevelOffset,
                    (int) water.getHitBox().y,
                    GamePanel.getCurrentTileSize(),
                    GamePanel.getCurrentTileSize(),
                    null);
        }

        for (Water water : playing.getLevelManager().getCurrentLevel().getWaterWaveList()) {
            g.drawImage(waterAsset[water.getAnimationIndex()],
                    (int) water.getHitBox().x - xLevelOffset,
                    (int) water.getHitBox().y,
                    GamePanel.getCurrentTileSize(),
                    GamePanel.getCurrentTileSize(),
                    null);
        }
    }

    private void renderFlag(Graphics g, int xLevelOffset) {
        for (Flag flag : playing.getLevelManager().getCurrentLevel().getFlags()) {

            int currentX = (int) (flag.getX() - xLevelOffset);

            g.drawImage(
                    flagAsset[flag.getAnimationIndex()],
                    currentX,
                    (int) flag.getY() + GamePanel.getCurrentTileSize() - Config.LevelEnv.FLAG_HEIGHT,
                    Config.LevelEnv.FLAG_WIDTH,
                    Config.LevelEnv.FLAG_HEIGHT,
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
        playing.getLevelManager().getCurrentLevel().getPotions().add(new Potion(container.getHitBox().x + container.getHitBox().width / 2,
                container.getHitBox().y - container.getHitBox().height / 4,
                droppedPotionType));

        playing.getGame().getAudioPlayer().playEffect(Config.Audio.POISON_FOUND_EFFECT_INDEX);
    }

    private void updateCannons(int[][] levelData, Player player) {
        for (Cannon cannon : playing.getLevelManager().getCurrentLevel().getCannons()) {
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
                player.takeDamage(30);
                projectile.setActive(false); // todo probably there should be one last animation of a ball explosion
            } else if (CollisionHelper.hasProjectileHitObstacle(projectile.getHitBox(), levelData)) {
                projectile.setActive(false);
            }
        }
    }

    private void updateWater() {
        for (Water water : playing.getLevelManager().getCurrentLevel().getWaterWaveList()) {
            water.update();
        }

        for (Water water : playing.getLevelManager().getCurrentLevel().getWaterBodyList()) {
            water.update();
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

        BufferedImage grassSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_GRASS);
        grassAssets = new BufferedImage[2];

        for (int column = 0; column < grassAssets.length; column++) {
            grassAssets[column] = grassSprite.getSubimage(column * Config.LevelEnv.GRASS_WIDTH_DEFAULT,
                    0,
                    Config.LevelEnv.GRASS_WIDTH_DEFAULT,
                    Config.LevelEnv.GRASS_HEIGHT_DEFAULT);
        }

        BufferedImage straightTreeSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_TREE_STRAIGHT);
        straightTreesAssets = new BufferedImage[4];

        for (int column = 0; column < straightTreesAssets.length; column++) {
            straightTreesAssets[column] = straightTreeSprite.getSubimage(column * Config.LevelEnv.TREE_STRAIGHT_WIDTH_DEFAULT,
                    0,
                    Config.LevelEnv.TREE_STRAIGHT_WIDTH_DEFAULT,
                    Config.LevelEnv.TREE_STRAIGHT_HEIGHT_DEFAULT);
        }


        BufferedImage bendTreeSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_TREE_BEND);
        bendTreesAssets = new BufferedImage[4];

        for (int column = 0; column < bendTreesAssets.length; column++) {
            bendTreesAssets[column] = bendTreeSprite.getSubimage(column * Config.LevelEnv.TREE_BEND_WIDTH_DEFAULT,
                    0,
                    Config.LevelEnv.TREE_BEND_WIDTH_DEFAULT,
                    Config.LevelEnv.TREE_BEND_HEIGHT_DEFAULT);
        }

        sharkAsset = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_SHARK);

        // Indexes 0-3 are for wave animation, index 4 is for non-animated water body
        waterAsset = new BufferedImage[5];
        BufferedImage img = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_WATER_TOP);
        for (int i = 0; i < 4; i++)
            waterAsset[i] = img.getSubimage(i * DEFAULT_TILE_SIZE, 0, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE);
        waterAsset[4] = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_WATER);


        flagAsset = new BufferedImage[9];
        BufferedImage flagSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_FLAG);
        for (int i = 0; i < flagAsset.length; i++) {
            flagAsset[i] = flagSprite.getSubimage(i * Config.LevelEnv.FLAG_WIDTH_DEFAULT, 0, Config.LevelEnv.FLAG_WIDTH_DEFAULT, Config.LevelEnv.FLAG_HEIGHT_DEFAULT);
        }
    }
}

