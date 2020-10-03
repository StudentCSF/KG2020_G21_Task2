package ru.vsu.cs.valeev.pixel_lines;

import java.awt.*;

public class GraphicsPixelDrawer implements  PixelDrawer {
    private Graphics g;

    public GraphicsPixelDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void drawPixel(int x, int y, Color c) {
        this.g.setColor(c);
        g.fillRect(x, y, 1, 1);
    }

    public Graphics getG() {
        return g;
    }
}
