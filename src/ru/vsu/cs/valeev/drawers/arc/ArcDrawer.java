package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public interface ArcDrawer {
    void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c);
    default void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle) {
        draw(centerX, centerY, xRadius, yRadius, startAngle, endAngle, Color.BLACK);
    }
}
