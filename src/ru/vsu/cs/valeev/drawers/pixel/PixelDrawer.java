package ru.vsu.cs.valeev.drawers.pixel;

import java.awt.*;

public interface PixelDrawer {
    void colorPixel(int x, int y, Color c);
    void colorPixel(int x, int y);
}
