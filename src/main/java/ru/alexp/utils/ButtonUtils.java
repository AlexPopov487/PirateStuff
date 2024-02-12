package ru.alexp.utils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ButtonUtils {

    public static boolean isHoveredOverButton(MouseEvent e, Rectangle hitBox) {
        return hitBox.contains(e.getX(), e.getY());
    }
}
