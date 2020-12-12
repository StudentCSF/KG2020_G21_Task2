package ru.vsu.cs.valeev.drawers.arc;

import ru.vsu.cs.valeev.drawers.line.LineDrawer;
import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import java.awt.*;

public class BresenhamPieDrawer implements ArcDrawer {
    private PixelDrawer pd;
    private LineDrawer ld;

    public BresenhamPieDrawer(PixelDrawer pd, LineDrawer ld) {
        this.pd = pd;
        this.ld = ld;
    }

    @Override
    public void draw(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c) {
        int[][] pts = new int[][] {
                {-1, -1},
                {-1, -1}
        };
        double[] minax = new double[]{Double.MAX_VALUE, Double.MIN_VALUE};

        double radStartAngle = (float) startAngle / 180 * Math.PI;
        double radEndAngle = (float) endAngle / 180 * Math.PI;
        int doubleASqr = 2 * width * width;
        int doubleBSqr = 2 * height * height;
        int x = width;
        int y = 0;
        int xChg = height * height * (1 - 2 * width);
        int yChg = width * width;
        int error = 0;
        int stopX = doubleBSqr * width;
        int stopY = 0;

        while (stopX >= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax);
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
        y = height;
        xChg = height * height;
        yChg = width * width * (1 - 2 * height);
        error = 0;
        stopX = 0;
        stopY = doubleASqr * height;

        while (stopX <= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax);
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
        ld.drawLine(centerX, centerY, pts[0][0], pts[0][1]);
        ld.drawLine(centerX, centerY, pts[1][0], pts[1][1]);
    }

    protected void colorPixels(final int cx, final int cy, final int dx, final int dy, final double sa, final double ea, Color c, int[][] pts, double[] minax) {
        double eps = 1e-12;

        int rx = cx + dx;
        int ry = cy + dy;
        double angle = Math.atan2(dx, dy) + 1.5 * Math.PI;
        if (angle >= sa && angle <= ea) {
            pd.colorPixel(rx, ry, c);
            if (angle < minax[0]) {
                pts[0][0] = rx;
                pts[0][1] = ry;
                minax[0] = angle;
            }
            if (angle > minax[1]) {
                pts[1][0] = rx;
                pts[1][1] = ry;
                minax[1] = angle;
            }
        }

        rx = cx + dx;
        ry = cy - dy;
        angle = -1 * Math.atan2(-dy, dx);
        if (angle >= sa && angle <= ea) {
            pd.colorPixel(rx, ry, c);
            if (angle < minax[0]) {
                pts[0][0] = rx;
                pts[0][1] = ry;
                minax[0] = angle;
            }
            if (angle > minax[1]) {
                pts[1][0] = rx;
                pts[1][1] = ry;
                minax[1] = angle;
            }
        }

        rx = cx - dx;
        ry = cy - dy;
        angle = -1 * Math.atan2(-dy, -dx);
        if (angle >= sa && angle <= ea) {
            pd.colorPixel(rx, ry, c);
            if (angle < minax[0]) {
                pts[0][0] = rx;
                pts[0][1] = ry;
                minax[0] = angle;
            }
            if (angle > minax[1]) {
                pts[1][0] = rx;
                pts[1][1] = ry;
                minax[1] = angle;
            }
        }

        rx = cx - dx;
        ry = cy + dy;
        angle = Math.atan2(dx, -dy) + 0.5 * Math.PI;
        if (angle >= sa && angle <= ea) {
            pd.colorPixel(rx, ry, c);
            if (angle < minax[0]) {
                pts[0][0] = rx;
                pts[0][1] = ry;
                minax[0] = angle;
            }
            if (angle > minax[1]) {
                pts[1][0] = rx;
                pts[1][1] = ry;
                minax[1] = angle;
            }
        }
    }
}