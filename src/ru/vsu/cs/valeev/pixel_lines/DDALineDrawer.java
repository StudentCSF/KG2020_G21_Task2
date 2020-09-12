package ru.vsu.cs.valeev.pixel_lines;

import ru.vsu.cs.valeev.LineDrawer;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private Graphics g;

    public DDALineDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        int l = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        int xs = (x2 - x1) / l;
        int ys = (y2 - y1) / l;

        int x = x1;
        int y = y1;
        for (int i = 0; i < l; i++) {
            x += xs;
            y += ys;

            this.g.drawLine(x, y, x, y );
        }
    }
}
