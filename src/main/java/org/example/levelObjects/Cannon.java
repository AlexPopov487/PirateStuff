package org.example.levelObjects;

import org.example.Config;
import org.example.Game;
import org.example.GamePanel;
import org.example.entities.Player;
import org.example.gameState.Drawable;
import org.example.types.LevelObjectType;
import org.example.utils.CollisionHelper;

import java.awt.*;

public class Cannon extends LevelObject implements Drawable {
    protected static final float ATTACK_RANGE = GamePanel.getCurrentTileSize();
    protected static final float VISUAL_RANGE = GamePanel.getCurrentTileSize() * 5;


    private final int currentTileY;
    public Cannon(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, false);
        currentTileY = (int) (y / GamePanel.getCurrentTileSize());
        initHitBox(Config.LevelEnv.CANNON_HIT_BOX_WIDTH, Config.LevelEnv.CANNON_HIT_BOX_HEIGHT);
        // Align the cannon img to the center of the tile
        hitBox.x -= 4 * Game.SCALE;
        hitBox.y += 6 * Game.SCALE;
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        if (shouldAnimate) {
            updateAnimationTick();
        }
    }

    public int getCurrentTileY() {
        return currentTileY;
    }

    public boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitBox().y / GamePanel.getCurrentTileSize());

        if (getCurrentTileY() != playerTileY) return false;

        if (!isPlayerInVisualRange(player)) return false;

        if (!isPlayerInFrontOfCannon(player)) return false;

        return CollisionHelper.isDistanceClearFromObstacle(levelData, hitBox, player.getHitBox(), getCurrentTileY(), false);
    }

    private boolean isPlayerInVisualRange(Player player) {
        // returns a distance between 2 points (always positive)
        float distanceToPlayer = Math.abs(player.getHitBox().x - getHitBox().x);
        return distanceToPlayer <= VISUAL_RANGE;
    }

    private boolean isPlayerInFrontOfCannon(Player player) {
        if (LevelObjectType.CANNON_LEFT.equals(getObjectType())) {
            return getHitBox().x > player.getHitBox().x;
        } else if(LevelObjectType.CANNON_RIGHT.equals(getObjectType())) {
            return getHitBox().x < player.getHitBox().x;
        }
        return false;
    }

}
