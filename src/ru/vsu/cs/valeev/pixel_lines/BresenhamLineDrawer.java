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

        int dx = x2 - x1;
        int dy = y2 - y1;

        int xs = x1 < x2 ? 1 : -1;
        int ys = y1 < y2 ? 1 : -1;

        int e = 2 * dx - dy;
        int i = dx;

        pd.drawPixel(x, y, Color.BLACK);

        while (i-- > 0) {
           if (e >= 0) {
               x++;
               y++;
               e += 2 * (dx + dy);
           } else {
               x++;
               e += 2 * dy;
           }
           pd.drawPixel(x, y, Color.BLACK);
        }
    }
}
