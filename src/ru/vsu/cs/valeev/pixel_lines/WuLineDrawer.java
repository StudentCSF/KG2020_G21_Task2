package ru.vsu.cs.valeev.pixel_lines;

import ru.vsu.cs.valeev.LineDrawer;

import java.awt.*;

public class WuLineDrawer implements LineDrawer {
    private Graphics g;

    public WuLineDrawer(Graphics g) {
        this.g = g;
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

        if (dx == 0 || dy == 0) {
            this.g.setColor(Color.BLACK);
            this.g.drawLine(x1, y1, x2, y2);
            return;
        }
        float gradient = 0;
        if (dx > dy) {
            gradient = (float) dy / dx;
            float intery = y1 + gradient;
            this.g.setColor(Color.BLACK);
            this.g.drawLine(x1, y1, x1, y1);
            for (int x = x1; x < x2; ++x) {
                this.g.setColor(new Color(0, 0, 0, (int) (255 - fractionalPart(intery) * 255))); //Меняем прозрачность
                this.g.drawLine(x, (int) intery, x, (int) intery);
                this.g.setColor(new Color(0, 0, 0, (int) (fractionalPart(intery) * 255)));
                this.g.drawLine(x, (int) intery + 1, x, (int) intery + 1);
                intery += gradient;
            }
            this.g.setColor(Color.BLACK);
            this.g.drawLine(x2, y2, x2, y2);
        } else {
            gradient = (float) dx / dy;
            float interx = x1 + gradient;
            this.g.setColor(Color.BLACK);
            this.g.drawLine(x1, y1, x1, y1);
            for (int y = y1; y < y2; ++y) {
                this.g.setColor(new Color(0, 0, 0, (int) (255 - fractionalPart(interx) * 255)));
                this.g.drawLine((int) interx, y, (int) interx, y);
                this.g.setColor(new Color(0, 0, 0, (int) (fractionalPart(interx) * 255)));
                this.g.drawLine((int) interx + 1, y, (int) interx + 1, y);
                interx += gradient;
            }
            this.g.setColor(Color.BLACK);
            this.g.drawLine(x2, y2, x2, y2);
        }
    }

    private float fractionalPart(float x) {
        int tmp = (int) x;
        return x - tmp;
    }
}
