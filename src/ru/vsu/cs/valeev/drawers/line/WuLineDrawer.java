package ru.vsu.cs.valeev.drawers.line;

import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import java.awt.*;

public class WuLineDrawer implements LineDrawer {
    private PixelDrawer pixelDrawer;

    public WuLineDrawer(PixelDrawer pd) {
        this.pixelDrawer = pd;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        boolean isLineVerticallyOriented = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        if (isLineVerticallyOriented) {
            int temp = y1;
            y1 = x1;
            x1 = temp;
            temp = y2;
            y2 = x2;
            x2 = temp;
        }
        if (x1 > x2) {
            int temp = y2;
            y2 = y1;
            y1 = temp;
            temp = x2;
            x2 = x1;
            x1 = temp;
        }
        if (isLineVerticallyOriented) {
            pixelDrawer.colorPixel(y1, x1, color);
            pixelDrawer.colorPixel(y2, x2, color);
        } else {
            pixelDrawer.colorPixel(x1, y1, color);
            pixelDrawer.colorPixel(x2, y2, color);
        }
        double dx = x2 - x1;
        double dy = y2 - y1;
        double k = dy / dx;
        double y = y1 + k;
        for (int x = x1 + 1; x < x2; x++) {
            Color tmp1 = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int) (255 * (1 - y + (int) y))
            );
            Color tmp2 = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int) (255 * (y - (int) y))
            );
            if (isLineVerticallyOriented) {
                pixelDrawer.colorPixel(
                        (int) y,
                        x, tmp1);
                pixelDrawer.colorPixel(
                        (int) y + 1,
                        x, tmp2);
            } else {
                pixelDrawer.colorPixel(
                        x,
                        (int) y, tmp1);
                pixelDrawer.colorPixel(
                        x,
                        (int) y + 1, tmp2);
            }
            y += k;
        }
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        drawLine(x1, y1, x2, y2, Color.BLACK);
    }
}
