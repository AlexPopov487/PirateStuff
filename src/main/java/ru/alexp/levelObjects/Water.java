package ru.alexp.levelObjects;

import ru.alexp.GamePanel;
import ru.alexp.gameState.Drawable;
import ru.alexp.types.LevelObjectType;

import java.awt.*;

public class Water extends LevelObject implements Drawable {

    public Water(float x, float y, LevelObjectType levelObjectType) {
        super(x, y, levelObjectType, isShouldAnimate(levelObjectType));
        initHitBox(GamePanel.getCurrentTileSize(), GamePanel.getCurrentTileSize()); // todo make initbox obligatory!!
    }

    private static boolean isShouldAnimate(LevelObjectType levelObjectType) {
        return LevelObjectType.WATER_WAVE.equals(levelObjectType);
    }

    @Override
    public void render(Graphics graphics) {

    }

    @Override
    public void update() {
        updateWaterAnimation();
    }

    private void updateWaterAnimation() {
        animationTick++;
        if (animationTick >= 40) {
            animationTick = 0;
            animationIndex++;

            if (animationIndex >= 4)
                animationIndex = 0;
        }
    }
}
