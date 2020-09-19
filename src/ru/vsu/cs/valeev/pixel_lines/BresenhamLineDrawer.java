package ru.vsu.cs.valeev.pixel_lines;

import ru.vsu.cs.valeev.LineDrawer;
import ru.vsu.cs.valeev.PixelDrawer;

import java.awt.*;

public class BresenhamLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public BresenhamLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        int x = x1;
        int y = y1;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        double sx = Math.signum(x2 - x1);
        double sy = Math.signum(y2 - y1);

        int e = 2 * dy - dx;

        boolean flag;
        if (flag = dy > dx) {
            int tmp = dx;
            dx = dy;
            dy = tmp;
        }

        for (int i = 1; i < dx; i++) {
            this.pd.drawPixel(x, y, Color.RED);
            
            while (e >= 0) {
                if (flag) {
                    x += sx;
                } else {
                    y += sy;
                }
                e -= 2 * dx;
            }
            if (flag) {
                y += sy;
            } else {
                x += sx;
            }
            e += 2 * dy;
        }
    }
}
