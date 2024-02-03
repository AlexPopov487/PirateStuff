package org.example.levelObjects;

import org.example.Config;
import org.example.Game;

import java.awt.geom.Rectangle2D;

public class HoverEffect {
    private float hoverOffset;
    private int hoverDirection = 1;

    public void update(int hoverOffsetMax, float hoverInc, Rectangle2D.Float entityHitBox, float yPivot) {
        hoverOffset += (hoverInc * Game.SCALE * hoverDirection);

        if (hoverOffset >= hoverOffsetMax) {
            hoverDirection = -1;
        } else if (hoverOffset < 0) {
            hoverDirection = 1;
        }

        entityHitBox.y = yPivot + hoverOffset;
    }
}
