package ru.vsu.cs.valeev.pixel_lines;

import java.awt.*;

public interface PixelDrawer {
    void colorPixel(int x, int y, Color c);
    void colorPixel(int x, int y);
}
