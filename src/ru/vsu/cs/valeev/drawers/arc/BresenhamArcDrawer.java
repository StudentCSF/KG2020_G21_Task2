package ru.vsu.cs.valeev.drawers.arc;

import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import java.awt.*;

public class BresenhamArcDrawer implements ArcDrawer {
    private PixelDrawer pd;

    public BresenhamArcDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        int[] octas = new int[8];
        fillOctas(octas, startAngle, endAngle);

        double radStartAngle = (float) startAngle / 180 * Math.PI;
        double radEndAngle = (float) endAngle / 180 * Math.PI;
        int doubleASqr = 2 * xRadius * xRadius;
        int doubleBSqr = 2 * yRadius * yRadius;
        int x = xRadius;
        int y = 0;
        int xChg = yRadius * yRadius * (1 - 2 * xRadius);
        int yChg = xRadius * xRadius;
        int error = 0;
        int stopX = doubleBSqr * xRadius;
        int stopY = 0;

        while (stopX >= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, octas, 0);
            y++;
            stopY += doubleASqr;
            error += yChg;
            yChg += doubleASqr;
            if ((2 * error + xChg) > 0) {
                x--;
                stopX -= doubleBSqr;
                error += xChg;
                xChg += doubleBSqr;
            }
        }

        x = 0;
        y = yRadius;
        xChg = yRadius * yRadius;
        yChg = xRadius * xRadius * (1 - 2 * yRadius);
        error = 0;
        stopX = 0;
        stopY = doubleASqr * yRadius;

        while (stopX <= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, octas, 1);
            x++;
            stopX += doubleBSqr;
            error += xChg;
            xChg += doubleBSqr;
            if (2 * error + yChg > 0) {
                y--;
                stopY -= doubleASqr;
                error += yChg;
                yChg += doubleASqr;
            }
        }
    }

    private void fillOctas(int[] octas, int s, int e) {
        int p1 = 0, p2 = 45;
        int step = 45;
        for (int i = 0; i < octas.length; i++, p1 += step, p2 += step) {
            if (p1 - 1 >= s && p2 + 1 <= e) octas[i] = 2;
            else if (p2 <= s || p1 >= e) octas[i] = 0;
            else octas[i] = 1;
        }
    }

    private void colorPixels(final int cx, final int cy, final int dx, final int dy, final double sa, final double ea, Color c, int[] octas, int i) {
        double angle;

        int rx = cx + dx;
        int ry = cy + dy;
        if (octas[7 - i] == 2) pd.colorPixel(rx, ry, c);
        else if (octas[7 - i] == 1) {
            angle = Math.atan2(dx, dy) + 1.5 * Math.PI;
            if (angle >= sa && angle <= ea) pd.colorPixel(rx, ry, c);
        }

        rx = cx + dx;
        ry = cy - dy;
        if (octas[i] == 2) pd.colorPixel(rx, ry, c);
        else if (octas[i] == 1) {
            angle = -1 * Math.atan2(-dy, dx);
            if (angle >= sa && angle <= ea) {
                pd.colorPixel(rx, ry, c);
            }
        }

        rx = cx - dx;
        ry = cy - dy;
        if (octas[3 - i] == 2) pd.colorPixel(rx, ry, c);
        else if (octas[3 - i] == 1) {
            angle = -1 * Math.atan2(-dy, -dx);
            if (angle >= sa && angle <= ea) pd.colorPixel(rx, ry, c);
        }

        rx = cx - dx;
        ry = cy + dy;
        if (octas[4 + i] == 2) pd.colorPixel(rx, ry, c);
        else if (octas[4 + i] == 1) {
            angle = Math.atan2(dx, -dy) + 0.5 * Math.PI;
            if (angle >= sa && angle <= ea) pd.colorPixel(rx, ry, c);
        }
    }
}
