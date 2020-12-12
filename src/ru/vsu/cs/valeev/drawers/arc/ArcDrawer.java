package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public interface ArcDrawer {
    void drawArc(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c);
    default void drawArc(int centerX, int centerY, int width, int height, int startAngle, int endAngle) {
        drawArc(centerX, centerY, width,height, startAngle, endAngle, Color.BLACK);
    }
}
