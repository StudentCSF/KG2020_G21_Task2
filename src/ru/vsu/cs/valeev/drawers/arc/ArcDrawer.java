package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public interface ArcDrawer {
    void draw(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c);
    default void draw(int centerX, int centerY, int width, int height, int startAngle, int endAngle) {
        draw(centerX, centerY, width,height, startAngle, endAngle, Color.BLACK);
    }
}
