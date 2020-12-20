package ru.vsu.cs.valeev.drawers.line;

import java.awt.*;

public interface LineDrawer {
    void drawLine(int x1, int y1, int x2, int y2, Color color);
    default void drawLine(int x1, int y1, int x2, int y2) {
        drawLine(x1, y1, x2, y2, Color.BLACK);
    }
}
