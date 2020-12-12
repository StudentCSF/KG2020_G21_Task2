package ru.vsu.cs.valeev.drawers.arc;

import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import javax.swing.plaf.ColorUIResource;
import java.awt.*;

public class BresenhamArcDrawer implements ArcDrawer {
    PixelDrawer pd;

    public BresenhamArcDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawArc(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c) {
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
            draw(centerX, centerY, x, y, radStartAngle, radEndAngle, c);
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
            draw(centerX, centerY, x, y, radStartAngle, radEndAngle, c);
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

    private void draw(final int cx, final int cy, final int x, final int y, final double startAngle, final double endAngle, Color c) {
        int rx = cx + x;
        int ry = cy + y;
        double angle = Math.atan2(x, y) + 1.5 * Math.PI;
        if (angle >= startAngle && angle <= endAngle)
            pd.colorPixel(rx, ry, Color.RED);
        rx = cx + x;
        ry = cy - y;
        angle = Math.atan2(-y, x);
        if (-angle >= startAngle && -angle <= endAngle)
            pd.colorPixel(rx, ry, Color.BLUE);
        rx = cx - x;
        ry = cy - y;
        angle = Math.atan2(-y, -x);
        if (-angle >= startAngle && -angle <= endAngle)
            pd.colorPixel(rx, ry, Color.RED);
        rx = cx - x;
        ry = cy + y;
        angle = Math.atan2(x, -y) + 0.5 * Math.PI;
        if (angle >= startAngle && angle <= endAngle)
            pd.colorPixel(rx, ry, Color.BLUE);
    }
}
