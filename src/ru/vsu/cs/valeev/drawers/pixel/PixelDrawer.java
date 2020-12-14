package ru.vsu.cs.valeev.drawers.pixel;

import java.awt.*;

public interface PixelDrawer {
    void colorPixel(int x, int y, Color c);

    default void colorPixel(int x, int y) {
        colorPixel(x, y, Color.BLACK);
    }
}
