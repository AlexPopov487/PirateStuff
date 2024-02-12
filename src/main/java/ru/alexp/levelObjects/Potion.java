package ru.alexp.levelObjects;

import ru.alexp.Config;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

public class Potion extends LevelObject  implements Drawable {

    private final HoverEffect hoverEffect;
    public Potion(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, true);
        initHitBox(Config.LevelEnv.POTION_HIT_BOX_WIDTH, Config.LevelEnv.POTION_HIT_BOX_HEIGHT);
        hoverEffect = new HoverEffect();
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateAnimationTick(null);
        hoverEffect.update(Config.LevelEnv.POTION_HOVER_OFFSET_MAX, Config.LevelEnv.POTION_HOVER_INC_STEP, getHitBox() , y);
    }
}
