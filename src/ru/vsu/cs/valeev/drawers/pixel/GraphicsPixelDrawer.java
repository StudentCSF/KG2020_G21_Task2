package ru.vsu.cs.valeev.drawers.pixel;

import java.awt.*;

public class GraphicsPixelDrawer implements PixelDrawer{
    private Graphics g;

    public GraphicsPixelDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void colorPixel(int x, int y, Color c) {
        g.setColor(c);
        this.g.fillRect(x, y, 1, 1);
    }

    @Override
    public void colorPixel(int x, int y) {
        g.setColor(Color.BLACK);
        this.g.fillRect(x, y, 1, 1);
    }
}
