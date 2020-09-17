package ru.vsu.cs.valeev;

import java.awt.*;

public abstract class BaseLineDrawer implements LineDrawer {
    protected Graphics g;

    public BaseLineDrawer(Graphics g) {
        this.g = g;
    }

    public abstract void drawLine(int x1, int y1, int x2, int y2);

    final public void drawPixel(int x, int y) {
        this.g.drawLine(x, y, x, y);
    }
}
