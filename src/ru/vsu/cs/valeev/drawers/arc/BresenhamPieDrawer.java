package ru.vsu.cs.valeev.drawers.arc;

import ru.vsu.cs.valeev.drawers.line.LineDrawer;
import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import java.awt.*;
import java.util.Arrays;

public class BresenhamPieDrawer implements ArcDrawer {
    private PixelDrawer pd;
    private LineDrawer ld;

    public BresenhamPieDrawer(PixelDrawer pd, LineDrawer ld) {
        this.pd = pd;
        this.ld = ld;
    }

    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        int[] alonePoint = null;
        if (startAngle - endAngle == 0) {
            alonePoint = specialCase(centerX, centerY, xRadius, yRadius, startAngle, c);
            if (alonePoint == null) return;
        }
        if (endAngle - startAngle > 360) endAngle = startAngle + 360;
        if (startAngle > 360) {
            int k = startAngle / 360;
            startAngle -= k * 360;
            endAngle -= k * 360;
        }
        if (endAngle < startAngle) {
            int tmp = startAngle;
            startAngle = endAngle;
            endAngle = tmp;
        }

        int[] minax = new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[][] pts = new int[][]{
                {-1, -1},
                {-1, -1}
        };
        if (startAngle % 90 == 0 && endAngle % 90 != 0 || endAngle % 90 == 0 && startAngle % 90 != 0)
            specialCase(pts, startAngle, endAngle, centerX, centerY, xRadius, yRadius);

        int[] octants = new int[8];
        if (alonePoint != null) octants[startAngle / 45] = 1;
        else setOctants(octants, startAngle, endAngle);

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
            colorPixels(centerX, centerY, x, y, startAngle, endAngle, c, pts, octants, 0, alonePoint, minax);
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
            colorPixels(centerX, centerY, x, y, startAngle, endAngle, c, pts, octants, 1, alonePoint, minax);
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
        if (pts[0][0] == -1 && pts[1][0] == -1) {
            if (alonePoint == null) {
                specialCase(centerX, centerY, xRadius, yRadius, startAngle, c);
                specialCase(centerX, centerY, xRadius, yRadius, endAngle, c);
            } else {
                ld.drawLine(centerX, centerY, alonePoint[0], alonePoint[1], c);
            }
            return;
        } else if (pts[0][0] == -1 && pts[1][0] != -1) {
            pts[0][0] = pts[1][0];
            pts[0][1] = pts[1][1];
        } else if (pts[1][0] == -1 && pts[0][0] != -1) {
            pts[1][0] = pts[0][0];
            pts[1][1] = pts[0][1];
        }
        if ((endAngle - endAngle / 360) == 180) {
            pts[1][0] = centerX - xRadius;
            pts[1][1] = centerY;
        }
        ld.drawLine(centerX, centerY, pts[0][0], pts[0][1], c);
        ld.drawLine(centerX, centerY, pts[1][0], pts[1][1], c);
    }

    private void colorPixels(final int cx, final int cy, final int dx, final int dy, final int sa, final int ea, Color c, int[][] pts, int[] octas, int i, int[] alonePoint, int[] minax) {
        int angle;
        int angle2;

        int rx = cx + dx;
        int ry = cy + dy;
        if (octas[7 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 7 - i);
        else if (octas[7 - i] > 0) {
            angle = toGradus(Math.atan2(dx, dy)) + 270;
            angle2 = angle + 360;
            if (alonePoint != null) {
                if (angle - sa == 0) {
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 7 - i);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 7 - i);
        }
        rx = cx + dx;
        ry = cy - dy;
        if (octas[i] == -1) colorPixel(rx, ry, c, -1, pts, minax, i);
        else if (octas[i] > 0) {
            angle = toGradus(-1 * Math.atan2(-dy, dx));
            angle2 = angle + 360;
            if (alonePoint != null) {
                if (angle - sa == 0) {
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, i);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, i);
        }

        rx = cx - dx;
        ry = cy - dy;
        if (octas[3 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 3 - i);
        else if (octas[3 - i] > 0) {
            angle = toGradus(-1 * Math.atan2(-dy, -dx));
            angle2 = angle + 360;
            if (angle < 0 || angle % Math.PI == 0) {
                while (angle < sa) angle += 2 * Math.PI;
            }
            if (alonePoint != null) {
                if (angle - sa == 0) {
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 3 - i);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 3 - i);
        }

        rx = cx - dx;
        ry = cy + dy;
        if (octas[4 + i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 4 + i);
        else if (octas[4 + i] > 0) {
            angle = toGradus(Math.atan2(dx, -dy)) + 90;
            angle2 = angle + 360;
            if (alonePoint != null) {
                if (angle - sa == 0) {
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 4 + i);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 4 + i);
        }
    }

    private void setOctants(int[] octas, int s, int e) {
        int i = 0;
        int p1 = 0, p2 = 45;
        while (p1 < e) {
            int index = i % 8;
            if (p2 <= s) {
            } else if (p1 > s && p2 < e) octas[index] = -1;
            else octas[index] = i / 8 + 1;
            i++;
            p1 += 45;
            p2 += 45;
        }
    }

    private void colorPixel(int x, int y, Color c, int angle, int[][] pts, int[] minax, int i) {
        pd.colorPixel(x, y, c);
        if (angle != -1) {
            if (angle <= minax[0] && i % 2 == 1 || angle < minax[0] && i % 2 == 0) {
                pts[0][0] = x;
                pts[0][1] = y;
                minax[0] = angle;
            }
            if (angle > minax[1] && i % 2 == 1 || i % 2 == 0 && angle >= minax[1]) {
                pts[1][0] = x;
                pts[1][1] = y;
                minax[1] = angle;
            }
        }
    }

    private static final double GRADUS = 180 / Math.PI;

    private int toGradus(double angle) {
        return (int) Math.round(angle * GRADUS);
    }

    private int[] specialCase(int x, int y, int xr, int yr, int angle, Color c) {
        while (angle > 360) angle -= 360;
        if (angle % 360 == 0) {
            ld.drawLine(x, y, x + xr, y, c);
            return null;
        } else if (angle % 270 == 0) {
            ld.drawLine(x, y, x, y + yr, c);
            return null;
        } else if (angle % 180 == 0) {
            ld.drawLine(x, y, x - xr, y, c);
            return null;
        } else if (angle % 90 == 0) {
            ld.drawLine(x, y, x, y - yr, c);
            return null;
        }
        return new int[2];
    }

    private void specialCase(int[][] pts, int s, int e, int cx, int cy, int xr, int yr) {
        if (s % 90 == 0) {
            int angle = s;
            while (angle > 360) angle -= 360;
            if (angle % 360 == 0) {
                pts[0][0] = cx + xr;
                pts[0][1] = cy;
            } else if (angle % 270 == 0) {
                pts[0][0] = cx;
                pts[0][1] = cy + yr;
            } else if (angle % 180 == 0) {
                pts[0][0] = cx - xr;
                pts[0][1] = cy;
            } else {
                pts[0][0] = cx;
                pts[0][1] = cy - yr;
            }
        } else {
            int angle = e;
            while (angle > 360) angle -= 360;
            if (angle % 360 == 0) {
                pts[1][0] = cx + xr;
                pts[1][1] = cy;
            } else if (angle % 270 == 0) {
                pts[1][0] = cx;
                pts[1][1] = cy + yr;
            } else if (angle % 180 == 0) {
                pts[1][0] = cx - xr;
                pts[1][1] = cy;
            } else {
                pts[1][0] = cx;
                pts[1][1] = cy - yr;
            }
        }
    }
}
