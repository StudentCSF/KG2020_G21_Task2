package ru.vsu.cs.valeev.pixel_lines;

import java.awt.*;

public class GraphicsLineDrawer implements LineDrawer {
    private Graphics g;

    public GraphicsLineDrawer(Graphics g) {
        this.g = g;
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

}

