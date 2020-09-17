package ru.vsu.cs.valeev.pixel_lines;

import ru.vsu.cs.valeev.BaseLineDrawer;

import java.awt.*;

public class DDALineDrawer extends BaseLineDrawer {

    public DDALineDrawer(Graphics g) {
        super(g);
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

            drawPixel(x, y);
        }
    }
}
