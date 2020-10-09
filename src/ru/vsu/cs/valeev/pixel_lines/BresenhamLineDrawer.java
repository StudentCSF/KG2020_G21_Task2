package ru.vsu.cs.valeev.pixel_lines;

import java.awt.*;

public class BresenhamLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public BresenhamLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        int x = x1;
        int y = y1;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int xs = x1 < x2 ? 1 : -1;
        int ys = y1 < y2 ? 1 : -1;

        int swap = 0;

        int c;
        if ((c = dx) < dy) {
            swap++;
            c = dy;
            dy = dx;
            dx = c;
        }

        int i1 = 2 * dy;
        int s = i1 - dx;
        int i2 = 2 * dx;

        pd.drawPixel(x, y);

        while (--c >= 0) {
            if (s >= 0) {
                if (swap != 0) {
                    x += xs;
                } else {
                    y += ys;
                }
                s -= i2;
            }
            if (swap != 0) {
                y += ys;
            } else {
                x += xs;
            }
            s += i1;
            pd.drawPixel(x, y);
        }
    }
}
