package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.GamePanel;
import ru.alexp.entities.Health;
import ru.alexp.entities.Player;
import ru.alexp.entities.Stamina;
import ru.alexp.gameState.Playing;
import ru.alexp.levels.Level;
import ru.alexp.types.AtlasType;
import ru.alexp.types.LevelObjectType;
import ru.alexp.utils.CollisionHelper;
import ru.alexp.utils.ResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static ru.alexp.Config.StatusBar.*;
import static ru.alexp.Game.DEFAULT_TILE_SIZE;

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
    private BufferedImage[] flagAssets;
    private BufferedImage[] keyAssets;
    private BufferedImage[] chestAssets;
    private BufferedImage[] shipAssets;
    private BufferedImage[] explosionAssets;
    private BufferedImage[] messageAsset;

    private final List<Projectile> projectiles = new CopyOnWriteArrayList<>();
    private Explosion explosion;

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

        for (ru.alexp.levelObjects.Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
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

        if (Objects.nonNull(explosion) && explosion.isActive) {
            explosion.update();
        }

        for (Ship ship : playing.getLevelManager().getCurrentLevel().getShips()) {
            ship.update();
        }

        updateWater();

        updateCannons(levelData, player);

        updateProjectiles(levelData, player);

        updateKey();

        updateChest();
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
        renderShips(g, xLevelOffset);
        renderWater(g, xLevelOffset);
        renderFlag(g, xLevelOffset);
        renderKey(g, xLevelOffset);
        renderChest(g, xLevelOffset);
        renderExplosion(g, xLevelOffset);
        renderScriptMessage(g, xLevelOffset);
    }

    public void checkObjectCollected(Rectangle2D.Float playerHitBox) {
        for (Potion potion : playing.getLevelManager().getCurrentLevel().getPotions()) {
            if (!potion.isActive) continue;

            if (playerHitBox.intersects(potion.getHitBox())) {
                if (applyEffect(potion)) {
                    potion.setActive(false);
                }
            }
        }

        checkKeyCollected(playerHitBox);
        checkChestTouched(playerHitBox);
    }

    public void checkExitReached(Rectangle2D.Float playerHitBox) {
        Point levelExit = playing.getLevelManager().getCurrentLevel().getLevelExit();
        if (Objects.nonNull(levelExit) && playing.isReadyToCompleteLevel()) {
            boolean hasPlayerReachedExit = isHasPlayerReachedExit(playerHitBox, levelExit);
            if (hasPlayerReachedExit) {
                playing.setCurrLevelCompleted(true);
            }
        }
    }

    public void checkObjectDestroyed(Rectangle2D.Float playerAttackRange) {
        for (ru.alexp.levelObjects.Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
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
        Key key = playing.getLevelManager().getCurrentLevel().getKey();
        if (Objects.nonNull(key) && !playing.getPlayer().isKeyCollected())
            explosion = null;
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

    private boolean isHasPlayerReachedExit(Rectangle2D.Float playerHitBox, Point levelExit) {
        return playerHitBox.intersects(levelExit.x, levelExit.y, GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize());
    }

    private void renderContainers(Graphics g, int xLevelOffset) {
        for (ru.alexp.levelObjects.Container container : playing.getLevelManager().getCurrentLevel().getContainers()) {
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
            if (!projectile.isActive) {
                continue;
            }

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
                    flagAssets[flag.getAnimationIndex()],
                    currentX,
                    (int) flag.getY() + GamePanel.getCurrentTileSize() - Config.LevelEnv.FLAG_HEIGHT,
                    Config.LevelEnv.FLAG_WIDTH,
                    Config.LevelEnv.FLAG_HEIGHT,
                    null);
        }
    }

    private void renderKey(Graphics g, int xLevelOffset) {
        Key key = playing.getLevelManager().getCurrentLevel().getKey();
        drawKey(g, xLevelOffset, key);

        Key statusBarKey = playing.getPlayer().getStatusBar().getKey();
        drawKey(g, 0, statusBarKey);
    }

    private void drawKey(Graphics g, int xLevelOffset, Key key) {
        if (Objects.isNull(key) || !key.isActive) return;

        g.drawImage(
                keyAssets[key.getAnimationIndex()],
                ((int) key.getHitBox().x - key.getxDrawOffset() - xLevelOffset),
                (int) ((int) key.getHitBox().y - key.getyDrawOffset() + (GamePanel.getCurrentTileSize() / 2.5)),
                Config.LevelEnv.KEY_WIDTH,
                Config.LevelEnv.KEY_HEIGHT,
                null); 
    }

    private void renderChest(Graphics g, int xLevelOffset) {

        Chest chest = playing.getLevelManager().getCurrentLevel().getChest();

        if (Objects.isNull(chest)) return;

        g.drawImage(
                chestAssets[chest.getAnimationIndex()],
                ((int) chest.getHitBox().x - chest.getxDrawOffset() - xLevelOffset),
                ((int) chest.getHitBox().y - chest.getyDrawOffset()),
                Config.LevelEnv.CHEST_WIDTH,
                Config.LevelEnv.CHEST_HEIGHT,
                null);
    }

    private void renderExplosion(Graphics g, int xLevelOffset) {

        if (Objects.nonNull(explosion) && explosion.isActive) {
            g.drawImage(
                    explosionAssets[explosion.getAnimationIndex()],
                    ((int) explosion.getHitBox().x + Config.LevelEnv.EXPLOSION_DRAW_OFFSET_X - xLevelOffset),
                    ((int) explosion.getHitBox().y - Config.LevelEnv.EXPLOSION_DRAW_OFFSET_Y),
                    Config.LevelEnv.EXPLOSION_WIDTH,
                    Config.LevelEnv.EXPLOSION_HEIGHT,
                    null);
        }
    }

    private void renderScriptMessage(Graphics g, int xLevelOffset) {

        for (Message message : playing.getLevelManager().getCurrentLevel().getMessages()) {
            if (playing.isScriptMessageShown()) {
                // (Config.SCRIPT_MESSAGE_WIDTH / 2.5) - to center x according to player pos
                int x = (int) ((int) message.getHitBox().x - xLevelOffset - (Config.SCRIPT_MESSAGE_WIDTH / 2.5));
                int y = (int) ((int) message.getHitBox().y - Config.SCRIPT_MESSAGE_HEIGHT / 2.5);
                g.drawImage(
                        messageAsset[message.getMessageType().getAnimationIndex()],
                        x,
                        y,
                        Config.SCRIPT_MESSAGE_WIDTH ,
                        Config.SCRIPT_MESSAGE_HEIGHT,
                        null);
            }
        }

    }

    private void renderShips(Graphics g, int xLevelOffset) {

        for (Ship ship : playing.getLevelManager().getCurrentLevel().getShips()) {
            int x = (int) (ship.getHitBox().x - ship.getHitBox().height - xLevelOffset - (GamePanel.getCurrentTileSize() / 2));
            int y = (int) (ship.getHitBox().y - ship.getHitBox().height);

            g.drawImage(
                    shipAssets[ship.getAnimationIndex()],
                    x,
                    y,
                    Config.LevelEnv.SHIP_WIDTH,
                    Config.LevelEnv.SHIP_HEIGHT,
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
            if (!projectile.isActive) {
                projectiles.remove(projectile);
                continue;
            }
            projectile.updatePosition();

            if (projectile.getHitBox().intersects(player.getHitBox())) {
                player.takeDamage(30);
                projectile.setActive(false);
            } else if (CollisionHelper.hasProjectileHitObstacle(projectile.getHitBox(), levelData)) {
                projectile.setActive(false);
            }
        }
    }

    private void updateKey() {
        Key key = playing.getLevelManager().getCurrentLevel().getKey();
        if (Objects.nonNull(key) && key.isActive) {
            key.update();
        }

        Key statusBarKey = playing.getPlayer().getStatusBar().getKey();
        if (Objects.nonNull(statusBarKey) && statusBarKey.isActive) {
            statusBarKey.update();
        }
    }

    private void updateChest() {
        Chest chest = playing.getLevelManager().getCurrentLevel().getChest();
        if (Objects.nonNull(chest)) {
            chest.update();
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

    private void checkChestTouched(Rectangle2D.Float playerHitBox) {
        Chest chest = playing.getLevelManager().getCurrentLevel().getChest();
        if (Objects.isNull(chest)) return;
        if (playerHitBox.intersects(chest.getHitBox())) {
            if (playing.getPlayer().isKeyCollected()) {
                chest.setShouldAnimate(true);

                if (chest.isAnimationCompleted()) {
                    playing.setCurrLevelCompleted(true);
                }
            } else {
                if (Objects.isNull(explosion)) {
                    explosion = new Explosion(chest.getHitBox().x, chest.getHitBox().y);
                    playing.getPlayer().getHeath().subtractHealth(Config.MAX_HEALTH);
                }
            }
        }
    }

    private void checkKeyCollected(Rectangle2D.Float playerHitBox) {
        Key key = playing.getLevelManager().getCurrentLevel().getKey();
        if (Objects.isNull(key)) return;
        if (playerHitBox.intersects(key.getHitBox())) {
            playing.setScriptMessageShown(true);
            placeKeyUnderStatusBar(playing, key);
        }
    }

    private void placeKeyUnderStatusBar(Playing playing, Key key) {

        Key statusKey = new Key((float) (STATUS_BAR_X + STATUS_BAR_WIDTH - (STATUS_BAR_WIDTH * 0.15)),
                STAMINA_BAR_Y_START - ((float) Config.LevelEnv.KEY_HEIGHT / 2));
        statusKey.setHoverActive(false);

        playing.getPlayer().getStatusBar().setKey(statusKey);

        key.setActive(false);
        playing.getPlayer().setKeyCollected(true);
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


        flagAssets = new BufferedImage[9];
        BufferedImage flagSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_FLAG);
        for (int i = 0; i < flagAssets.length; i++) {
            flagAssets[i] = flagSprite.getSubimage(i * Config.LevelEnv.FLAG_WIDTH_DEFAULT, 0, Config.LevelEnv.FLAG_WIDTH_DEFAULT, Config.LevelEnv.FLAG_HEIGHT_DEFAULT);
        }

        keyAssets = new BufferedImage[8];
        BufferedImage keySprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_KEY);
        for (int i = 0; i < keyAssets.length; i++) {
            keyAssets[i] = keySprite.getSubimage(i * Config.LevelEnv.KEY_WIDTH_DEFAULT, 0, Config.LevelEnv.KEY_WIDTH_DEFAULT, Config.LevelEnv.KEY_HEIGHT_DEFAULT);
        }

        chestAssets = new BufferedImage[10];
        BufferedImage chestSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_CHEST);
        for (int i = 0; i < chestAssets.length; i++) {
            chestAssets[i] = chestSprite.getSubimage(i * Config.LevelEnv.CHEST_WIDTH_DEFAULT, 0, Config.LevelEnv.CHEST_WIDTH_DEFAULT, Config.LevelEnv.CHEST_HEIGHT_DEFAULT);
        }

        explosionAssets = new BufferedImage[7];
        BufferedImage explosionSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_EXPLOSION);
        for (int i = 0; i < explosionAssets.length; i++) {
            explosionAssets[i] = explosionSprite.getSubimage(i * Config.LevelEnv.EXPLOSION_WIDTH_DEFAULT, 0, Config.LevelEnv.EXPLOSION_WIDTH_DEFAULT, Config.LevelEnv.EXPLOSION_HEIGHT_DEFAULT);
        }

        shipAssets = new BufferedImage[4];
        BufferedImage shipSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_SHIP);
        for (int i = 0; i < shipAssets.length; i++) {
            shipAssets[i] = shipSprite.getSubimage(i * Config.LevelEnv.SHIP_WIDTH_DEFAULT, 0, Config.LevelEnv.SHIP_WIDTH_DEFAULT, Config.LevelEnv.SHIP_HEIGHT_DEFAULT);
        }

        messageAsset = new BufferedImage[2];
        BufferedImage messageSprite = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_MESSAGE);
        for (int i = 0; i < messageAsset.length; i++) {
            messageAsset[i] = messageSprite.getSubimage(0, i * Config.SCRIPT_MESSAGE_DEFAULT_HEIGHT, Config.SCRIPT_MESSAGE_DEFAULT_WIDTH, Config.SCRIPT_MESSAGE_DEFAULT_HEIGHT);
        }
    }
}

