package ru.vsu.cs.valeev.pixel_lines;

import ru.vsu.cs.valeev.BaseLineDrawer;

import java.awt.*;

public class BresenhamLineDrawer extends BaseLineDrawer {

    public BresenhamLineDrawer(Graphics g) {
        super(g);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int xs = x1 < x2 ? 1 : -1;
        int ys = y1 < y2 ? 1 : -1;

        int e = dx - dy;
        int e2;

        while (true) {
            drawPixel(x1, y1);

            if (x1 == x2 || y1 == y2) break;

            e2 = 2 * e;

            if (e2 > -dy) {
                e -= dy;
                x1 += xs;
            }
            if (e2 < dx) {
                e += dx;
                y1 += ys;
            }
        }
    }
}
