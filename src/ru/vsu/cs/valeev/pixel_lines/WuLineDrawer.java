package ru.vsu.cs.valeev.pixel_lines;

import java.awt.*;

public class WuLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public WuLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        if (x2 < x1) {
            x1 += x2;
            x2 = x1 - x2;
            x1 -= x2;
            y1 += y2;
            y2 = y1 - y2;
            y1 -= y2;
        }
        int dx = x2 - x1;
        int dy = y2 - y1;

        if (dx == 0) {
            if (dy < 0) {
                y1 += y2;
                y2 = y1 - y2;
                y1 -= y2;
            }
            for (int i = y1; i < y2; i++) {
                pd.drawPixel(x1, i, Color.RED);
            }
            return;
        } else if (dy == 0) {
            if (dx < 0) {
                x1 += x2;
                x2 = x1 - x2;
                x1 -= x2;
            }
            for (int j = x1; j < x2; j++) {
                pd.drawPixel(j, y1, Color.BLUE);
            }
            return;
        }

        float gradient = 0;
        if (dx > dy) {
            gradient = (float) dy / dx;
            float intery = y1 + gradient;
            pd.drawPixel(x1, y1, Color.BLACK);
            for (int x = x1; x < x2; ++x) {
                pd.drawPixel(x, (int) intery, new Color(0, 0, 0, (int) (255 - fractionalPart(intery) * 255)));
                pd.drawPixel(x, (int) intery + 1, new Color(0, 0, 0, (int) (fractionalPart(intery) * 255)));
                intery += gradient;
            }
            pd.drawPixel(x2, y2, Color.BLACK);
        } else {
            gradient = (float) dx / dy;
            float interx = x1 + gradient;
            pd.drawPixel(x1, y1, Color.BLACK);
            for (int y = y1; y < y2; ++y) {
                pd.drawPixel((int) interx, y, new Color(0, 0, 0, (int) (255 - fractionalPart(interx) * 255)));
                pd.drawPixel((int) interx + 1, y, new Color(0, 0, 0, (int) (fractionalPart(interx) * 255)));
                interx += gradient;
            }
            pd.drawPixel(x2, y2, Color.BLACK);
        }
    }

    private float fractionalPart(float x) {
        int tmp = (int) x;
        return x - tmp;
    }
}
