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
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        // для запоминания концевых точек арки, с которыми будем соединять центр
        int[][] pts = new int[][]{
                {-1, -1},
                {-1, -1}
        };
        // для хранения текущих мин. и макс. углов соответствующих концевых точек
        double[] minax = new double[]{Double.MAX_VALUE, Double.MIN_VALUE};


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

        // закрашиваем точки в "горизонтально ориетированном прямоугольнике - 0, 3, 4, 7 восьмеринки"
        while (stopX >= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax, octas, 0);
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

        // закрашиваем точки в "вертикально ориентированном прямоугольнике - 2, 3, 5, 6 восьмеринки"
        while (stopX <= stopY) {
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax, octas, 1);
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

    // восьмеринки, которые полностью в дипазоне помечаются двойкой, вне дипазона 0,
    // спорные - 1
    // примечание:  пришлось сделать спорными восьмеринки, где диапазон по типу 0-45 (0 восьмеринка спорная), 30-90 (1 восьмеринка спорная)
    private void fillOctas(int[] octas, int s, int e) {
        int p1 = 0, p2 = 45;
        int step = 45;
        for (int i = 0; i < octas.length; i++, p1 += step, p2 += step) {
            if (p1 - 1 >= s && p2 + 1 <= e) octas[i] = 2;
            else if (p2 <= s || p1 >= e) octas[i] = 0;
            else octas[i] = 1;
        }
    }

    private void colorPixels(final int cx, final int cy, final int dx, final int dy, final double sa, final double ea, Color c, int[][] pts, double[] minax, int[] octas, int i) {
        double angle;

        int rx = cx + dx;
        int ry = cy + dy;
        if (octas[7 - i] == 2) colorPixel(rx, ry, c, -1, pts, minax);
        else if (octas[7 - i] == 1) {
            angle = Math.atan2(dx, dy) + 1.5 * Math.PI;
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax);
        }

        rx = cx + dx;
        ry = cy - dy;
        if (octas[i] == 2) colorPixel(rx, ry, c, -1, pts, minax);
        else if (octas[i] == 1) {
            angle = -1 * Math.atan2(-dy, dx);
            if (angle >= sa && angle <= ea) {
                colorPixel(rx, ry, c, angle, pts, minax);
            }
        }

        rx = cx - dx;
        ry = cy - dy;
        if (octas[3 - i] == 2) colorPixel(rx, ry, c, -1, pts, minax);
        else if (octas[3 - i] == 1) {
            angle = -1 * Math.atan2(-dy, -dx);
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax);
        }

        rx = cx - dx;
        ry = cy + dy;
        if (octas[4 + i] == 2) colorPixel(rx, ry, c, -1, pts, minax);
        else if (octas[4 + i] == 1) {
            angle = Math.atan2(dx, -dy) + 0.5 * Math.PI;
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax);
        }
    }

    private void colorPixel(int x, int y, Color c, double angle, int[][] pts, double[] minax) {
        pd.colorPixel(x, y, c);
        if (angle != -1) {
            if (angle < minax[0]) {
                pts[0][0] = x;
                pts[0][1] = y;
                minax[0] = angle;
            }
            if (angle > minax[1]) {
                pts[1][0] = x;
                pts[1][1] = y;
                minax[1] = angle;
            }
        }
    }
}
