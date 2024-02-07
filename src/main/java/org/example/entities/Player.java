package org.example.entities;

import org.example.Config;
import org.example.gameState.Playing;
import org.example.types.AtlasType;
import org.example.utils.Helper;
import org.example.utils.PlayerConstants;
import org.example.utils.ResourceLoader;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.example.Config.ENTITY_ANIMATION_SPEED;
import static org.example.Game.SCALE;
import static org.example.gameState.Playing.CHARACTER_SPRITE_HEIGHT;
import static org.example.gameState.Playing.CHARACTER_SPRITE_WIDTH;
import static org.example.types.EntityType.PLAYER;
import static org.example.utils.CollisionHelper.*;
import static org.example.utils.PlayerConstants.*;

public class Player extends Entity {
    private final Playing playing;
    private final Actions actions;
    private final StatusBar statusBar;
    private final Stamina stamina;
    private boolean isAttackPerformed = false;
    private BufferedImage[][] animations;
    private int[][] currentLevelData;

    // for handling rendering player sprite when going left
    private int xFlip = 0;
    private int widthFlip = 1;
    private boolean isActive = true;
    private int powerAttackTick;

    PlayerConstants currentAnimation = SPRITE_JUMPING;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height, Helper.generateGravitySettingForEntity(PLAYER), Config.Player.WALK_SPEED);
        this.playing = playing;
        actions = new Actions();
        stamina = new Stamina();
        statusBar = new StatusBar(getHeath(), stamina);
        loadAnimations();
        initHitBox(Config.Player.HIT_BOX_WIDTH, Config.Player.HIT_BOX_HEIGHT);
        initAttackRange();
    }

    public void setSpawnPosition(Point spawnPoint) {
        this.y = spawnPoint.y;
        this.x = spawnPoint.x;
        hitBox.x = x;
        hitBox.y = y;
    }

    public void setCurrentLevelData(int[][] currentLevelData) {
        this.currentLevelData = currentLevelData;
    }

    public void setInAirIfPlayerNotOnFloor(Player player, int[][] currentLevelData) {
        if (!isOnTheFloor(player.getHitBox(), currentLevelData)) {
            getDirections().setInAir(true);
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Actions getActions() {
        return actions;
    }

    public void update() {
        statusBar.update();
        checkPlayerAlive();

        if (isActive) {
            updateCharacterPosition();

            checkPotionCollected();
            checkSpikeTrapTouched();
            checkDrowned();

            setCharacterAnimation();
            checkEnemyStumped();

            if (actions.isAttacking() || actions.isPowerAttacking()) {
                attack();
            }

            updateAnimationTick();
            updateAttackRange();
        }
    }

    public void render(Graphics g, int xLevelOffset) {
        statusBar.render(g);

        g.drawImage(animations[currentAnimation.getSpriteIndex()][animationIndex],
                (int) (hitBox.x - Config.Player.DRAW_OFFSET_X) - xLevelOffset + xFlip,
                (int) (hitBox.y - Config.Player.DRAW_OFFSET_Y),
                width * widthFlip,
                height,
                null);
//        drawHitBox(g, xLevelOffset);
//        drawAttackRangeBox(g, xLevelOffset);
    }

    private void checkPlayerAlive() {
        if (getHeath().isDead()) {
            // player just died
            if (isActive()) {
                setActive(false);
                resetAnimations();
                currentAnimation = SPRITE_DEAD;
                playing.getGame().getAudioPlayer().playEffect(Config.Audio.DIE_EFFECT_INDEX);
            } else if (animationIndex == SPRITE_DEAD.getFrameCount() - 1 &&
                    animationTick >= ENTITY_ANIMATION_SPEED - 1) {
                // setting GAME_OVER only at the last frame of a dying animation
                playing.setGameOver();
            } else {
                updateAnimationTick();
            }
        }
    }

    public void updateAttackRange() {
        if (getDirections().isMovingToRightAndLeft()) {
            if (widthFlip == 1) {
                attackRange.x = hitBox.x + hitBox.width + 10 * SCALE;
            } else {
                attackRange.x = hitBox.x - hitBox.width - 10 * SCALE;
            }
        } else if (getDirections().isMovingRight() || isPowerAttackingFacingRight()) {
            attackRange.x = hitBox.x + hitBox.width + 10 * SCALE;
        } else if (getDirections().isMovingLeft() || isPowerAttackingFacingLeft()) {
            attackRange.x = hitBox.x - hitBox.width - 10 * SCALE;
        }
        attackRange.y = hitBox.y + 10 * SCALE;
    }

    public Stamina getStamina() {
        return stamina;
    }

    public void initAttackRange() {
        float attackX;
        if (widthFlip == 1) {
            attackX = hitBox.x + hitBox.width + 10 * SCALE;
        } else {
            attackX = hitBox.x - hitBox.width - 10 * SCALE;
        }
        attackRange = new Rectangle2D.Float(attackX, y, 20 * SCALE, 20 * SCALE);
    }

    public void reset() {
        isActive = true;
        resetGravitySettings();
        getDirections().reset();
        resetInAir();
        actions.resetAll();
        getHeath().reset();
        stamina.reset();
        resetAnimations();
        currentAnimation = SPRITE_IDLE;

        // reset player position
        hitBox.x = x;
        hitBox.y = y;

        setInAirIfPlayerNotOnFloor(this, currentLevelData);
        initAttackRange();
    }

    public void doPowerAttack() {
        if (actions.isPowerAttacking()) return;

        if (stamina.getCurrentValue() < Config.Player.POWER_ATTACK_STAMINA_COST) return;

        stamina.subtractStamina(Config.Player.POWER_ATTACK_STAMINA_COST);
        actions.setAttacking(false);
        actions.setPowerAttacking(true);
    }

    public void takeDamage(int amount) {
        getHeath().subtractHealth(amount);
        actions.setTakingDamage(true);
    }

    private void updateCharacterPosition() {
        getDirections().setMoving(false);

        if (getDirections().isJumping()) {
            handleJump();
        }

        if (getDirections().isNoDirectionSet() && !actions.isPowerAttacking()) return;

        float xDestination = hitBox.x;

        if (getDirections().isMovingLeft()) {
            xDestination -= getWalkSpeed();
            xFlip = width;
            widthFlip = -1;
        }

        if (getDirections().isMovingRight()) {
            xDestination += getWalkSpeed();
            xFlip = 0;
            widthFlip = 1;
        }

        if (actions.isPowerAttacking()) {

            // if player hit powerAttack but did not set any direction, we need to set the direction automatically
            if (!getDirections().isMovingLeft() && !getDirections().isMovingRight()) {
                if (widthFlip == -1) {
                    xDestination -= getWalkSpeed();
                } else {
                    xDestination += getWalkSpeed();
                }
            }

            if (widthFlip == -1) {
                xDestination -= getWalkSpeed() * 2;
            } else {
                xDestination += getWalkSpeed() * 2;
            }
        }

        if (!getDirections().isInAir() && !isOnTheFloor(hitBox, currentLevelData)) {
            getDirections().setInAir(true);
        }

        if (getDirections().isInAir() && !actions.isPowerAttacking()) {
            float yDestination = hitBox.y + getGravitySettings().getAirSpeed();

            if (canMoveHere(hitBox.x, yDestination, hitBox.width, hitBox.height, currentLevelData)) {
                hitBox.y = yDestination;
                getGravitySettings().setAirSpeed(getGravitySettings().getAirSpeed() + getGravitySettings().getGravityForce());
                updatePlayerXPos(xDestination);
            } else {
             /*
             if we assume that updating position will lead to collision, the pos update will not be made,
             meaning that there still will be some space between the player and the obstacle. Thus, we need to
             move the player as close to the obstacle as possible
            */
                hitBox.y = getClosestToObstacleYPos(hitBox, yDestination);

                if (getGravitySettings().getAirSpeed() > 0) { // falling on the floor
                    resetInAir();
                } else { // colliding with the roof, need to bounce
                    getGravitySettings().setAirSpeed(getGravitySettings().getPostCollisionFallSpeed());
                }
                updatePlayerXPos(xDestination);
            }

        } else {
            updatePlayerXPos(xDestination);
        }

        getDirections().setMoving(true);
    }

    private void checkPotionCollected() { // todo do not drink potion if heath is full
        playing.checkPotionCollected(hitBox);
    }

    private void checkSpikeTrapTouched() {
        playing.checkSpikeTrapTouched(hitBox);
    }

    private void checkDrowned() {
        playing.checkDrowned(hitBox);
    }

    private void checkEnemyStumped() {
        if (currentAnimation == SPRITE_FALLING) {
            playing.checkEnemyStumped(this);
        }
    }

    private void handleJump() {
        if (getDirections().isInAir()) return;

        playing.getGame().getAudioPlayer().playEffect(Config.Audio.JUMP_EFFECT_INDEX);
        getDirections().setInAir(true);
        getGravitySettings().setAirSpeed(getGravitySettings().getJumpSpeed());
    }

    private void resetInAir() {
        getDirections().setInAir(false);
        getGravitySettings().setAirSpeed(0);
    }

    private void updatePlayerXPos(float xDestination) {
        if (canMoveHere(xDestination, hitBox.y, hitBox.width, hitBox.height, currentLevelData)) {
            hitBox.x = xDestination;
        } else {
            /*
             if we assume that updating position will lead to collision, the pos update will not be made,
             meaning that there still will be some space between the player and the obstacle. Thus, we need to
             move the player as close to the obstacle as possible
            */
            hitBox.x = getClosestToObstacleXPos(hitBox, xDestination);
            if (actions.isPowerAttacking()) {
                actions.setPowerAttacking(false);
                powerAttackTick = 0;
            }
        }
    }

    private void setCharacterAnimation() {

        var previousAnimation = currentAnimation;

        if (getDirections().isMoving() && !getDirections().isMovingToRightAndLeft()) {
            currentAnimation = SPRITE_RUNNING;
        } else {
            currentAnimation = SPRITE_IDLE;
        }

        if (getDirections().isInAir()) {
            if (getDirections().isJumping()) {
                currentAnimation = SPRITE_JUMPING;
            } else {
                currentAnimation = SPRITE_FALLING;
            }
        }

        if (actions.isTakingDamage()) {
            currentAnimation = SPRITE_HIT;
            if (previousAnimation != SPRITE_HIT) {
                resetAnimationTick();
            }
            return;
        }

        if (actions.isPowerAttacking()) {
            currentAnimation = SPRITE_ATTACK;
            resetAnimationTick();
            animationIndex = 1;

            powerAttackTick++;
            if (powerAttackTick >= 35) {
                powerAttackTick = 0;
                actions.setPowerAttacking(false);
            }
            return;
        }

        if (actions.isAttacking()) {
            currentAnimation = SPRITE_ATTACK;

            // the sprite's attacking animation takes too long, thus we shorten it by one sprite
            if (previousAnimation != SPRITE_ATTACK) {
//                previousAnimation = currentAnimation;
                resetAnimationTick();
                animationIndex = 1;
            }
            return;
        }

        // this is necessary to avoid starting new animation from the index of a previous animation (leads to flickering and bugs)
        if (previousAnimation != currentAnimation) {
            resetAnimationTick();
        }
    }

    private void attack() {
        /*
        the attack is registered only at specific animation index (when it looks like an enemy lands a strike)
        and only once per attack animation
        */
        if (animationIndex != 1 || isAttackPerformed) return;

        isAttackPerformed = !actions.isPowerAttacking();

        playing.checkEnemyHit(attackRange);
        playing.checkLevelObjectDestroyed(attackRange);

        int attackEffectIndex = playing.getGame().getAudioPlayer().getAttackEffectRandomIndex();
        playing.getGame().getAudioPlayer().playEffect(attackEffectIndex);
    }

    private void resetAnimationTick() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updateAnimationTick() {
        animationTick++;
        if (animationTick >= ENTITY_ANIMATION_SPEED) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= currentAnimation.getFrameCount()) {
                animationIndex = 0;
                resetAnimations();
            }
        }
    }

    private void resetAnimations() {
        actions.setAttacking(false);
        isAttackPerformed = false;
        actions.setTakingDamage(false);
        animationTick = 0;
        animationIndex = 0;
    }

    private void loadAnimations() {

        var characterAtlas = ResourceLoader.getSpriteAtlas(AtlasType.ATLAS_PLAYER);

        animations = new BufferedImage[7][8];

        for (int spriteRow = 0; spriteRow < animations.length; spriteRow++) {

            for (int spriteColumn = 0; spriteColumn < animations[spriteRow].length; spriteColumn++) {
                animations[spriteRow][spriteColumn] = characterAtlas.getSubimage(spriteColumn * CHARACTER_SPRITE_WIDTH,
                        spriteRow * CHARACTER_SPRITE_HEIGHT,
                        CHARACTER_SPRITE_WIDTH,
                        CHARACTER_SPRITE_HEIGHT);
            }
        }
    }

    private boolean isPowerAttackingFacingRight() {
        return actions.isPowerAttacking() && widthFlip == 1;
    }

    private boolean isPowerAttackingFacingLeft() {
        return actions.isPowerAttacking() && widthFlip == -1;
    }
}
