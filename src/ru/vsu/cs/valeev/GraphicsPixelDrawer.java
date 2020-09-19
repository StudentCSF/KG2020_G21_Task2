package ru.vsu.cs.valeev;

import java.awt.*;

public class GraphicsPixelDrawer implements PixelDrawer {
    private Graphics g;

    public GraphicsPixelDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void drawPixel(int x, int y, Color c) {
        g.setColor(c);
        this.g.fillRect(x, y, 1, 1);
    }

    @Override
    public void drawPixel(int x, int y) {
        this.g.fillRect(x, y, 1, 1);
    }
}