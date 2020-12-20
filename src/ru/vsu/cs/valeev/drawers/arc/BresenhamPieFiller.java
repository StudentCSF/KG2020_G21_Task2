package ru.vsu.cs.valeev.drawers.arc;

import ru.vsu.cs.valeev.drawers.line.LineDrawer;
import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BresenhamPieFiller implements ArcDrawer {
    PixelDrawer pd;
    LineDrawer ld;

    public BresenhamPieFiller(PixelDrawer pd, LineDrawer ld) {
        this.pd = pd;
        this.ld = ld;
    }

    private static class PixelType implements Comparable<PixelType> {
        private int value;
        private boolean isArcPixel;

        public PixelType(int value, boolean isArcPixel) {
            this.value = value;
            this.isArcPixel = isArcPixel;
        }

        public int getValue() {
            return value;
        }

        public boolean isArcPixel() {
            return isArcPixel;
        }

        @Override
        public int compareTo(PixelType o) {
            return Integer.compare(this.getValue(), o.getValue());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PixelType pixelType = (PixelType) o;
            return value == pixelType.value && isArcPixel == pixelType.isArcPixel;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, isArcPixel);
        }
    }

    private void findPixels(final int x1, final int y1, final int x2, final int y2, Color color, Map<Integer, List<PixelType>> map) {
        int x = x1;
        int y = y1;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        int xs = x1 < x2 ? 1 : -1;
        int ys = y1 < y2 ? 1 : -1;

        int swap = 0;

        int c;
        if ((c = dx) < dy) {
            swap++;
            c = dy;
            dy = dx;
            dx = c;
        }

        int i1 = 2 * dy;
        int s = i1 - dx;
        int i2 = 2 * dx;

        pd.colorPixel(x, y, color);

        if (!map.containsKey(x)) {
            map.put(x, new ArrayList<>(Arrays.asList(new PixelType(y, false))));
        } else {
            for (PixelType pt : map.get(x)) {
                if (pt.getValue() == y) pt.isArcPixel = false;
            }
            map.get(x).add(new PixelType(y, false));
        }

        while (--c >= 0) {
            if (s >= 0) {
                if (swap != 0) {
                    x += xs;
                } else {
                    y += ys;
                }
                s -= i2;
            }
            if (swap != 0) {
                y += ys;
            } else {
                x += xs;
            }
            s += i1;
            pd.colorPixel(x, y, color);
            if (!map.containsKey(x)) {
                map.put(x, new ArrayList<>(Arrays.asList(new PixelType(y, false))));
            } else {
                for (PixelType pt : map.get(x)) {
                    if (pt.getValue() == y) pt.isArcPixel = false;
                }
                map.get(x).add(new PixelType(y, false));
            }
        }
    }

    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        if (startAngle - endAngle == 0) {
            return;
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
        setOctants(octants, startAngle, endAngle);

        int doubleASqr = 2 * xRadius * xRadius;
        int doubleBSqr = 2 * yRadius * yRadius;
        int x = xRadius;
        int y = 0;
        int xChg = yRadius * yRadius * (1 - 2 * xRadius);
        int yChg = xRadius * xRadius;
        int error = 0;
        int stopX = doubleBSqr * xRadius;
        int stopY = 0;

        Map<Integer, List<PixelType>> map = new TreeMap<>();
        map.put(centerX, new ArrayList<>(Arrays.asList(new PixelType(centerY, false))));

        while (stopX >= stopY) {
            colorPixels(centerX, centerY, x, y, startAngle, endAngle, c, pts, octants, 0, minax, map);
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
            colorPixels(centerX, centerY, x, y, startAngle, endAngle, c, pts, octants, 1, minax, map);
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
            specialCase(centerX, centerY, xRadius, yRadius, startAngle, c);
            specialCase(centerX, centerY, xRadius, yRadius, endAngle, c);
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
        if (endAngle - startAngle < 360) {
            specialCase(pts, startAngle, endAngle, centerX, centerY, xRadius, yRadius);
            findPixels(centerX, centerY, pts[0][0], pts[0][1], c, map);
            findPixels(centerX, centerY, pts[1][0], pts[1][1], c, map);
        }

        fill(map, c);

    }

    private void fill(Map<Integer, List<PixelType>> map, Color c) {
        for (Map.Entry<Integer, List<PixelType>> kv : map.entrySet()) {
            Collections.sort(kv.getValue());
            if (kv.getValue().size() == 2 && !kv.getValue().get(0).isArcPixel() && !kv.getValue().get(1).isArcPixel())
                ld.drawLine(kv.getKey(), kv.getValue().get(0).getValue(), kv.getKey(), kv.getValue().get(1).getValue(), c);
            else {
                for (int i = 0; i < kv.getValue().size() - 1; i++) {
                    if (kv.getValue().get(i).isArcPixel() || kv.getValue().get(i + 1).isArcPixel()) {
                        ld.drawLine(kv.getKey(), kv.getValue().get(i).getValue(), kv.getKey(), kv.getValue().get(i + 1).getValue(), c);
                    }
                }
            }
        }
    }

    private void colorPixels(final int cx, final int cy, final int dx, final int dy, final int sa, final int ea, Color c, int[][] pts, int[] octas, int i, int[] minax, Map<Integer, List<PixelType>> map) {
        int angle;
        int angle2;

        int rx = cx + dx;
        int ry = cy + dy;
        if (octas[7 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 7 - i, map);
        else if (octas[7 - i] > 0) {
            angle = toGradus(Math.atan2(dx, dy)) + 270;
            angle2 = angle + 360;
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 7 - i, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 7 - i, map);
        }
        rx = cx + dx;
        ry = cy - dy;
        if (octas[i] == -1) colorPixel(rx, ry, c, -1, pts, minax, i, map);
        else if (octas[i] > 0) {
            angle = toGradus(-1 * Math.atan2(-dy, dx));
            angle2 = angle + 360;
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, i, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, i, map);
        }

        rx = cx - dx;
        ry = cy - dy;
        if (octas[3 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 3 - i, map);
        else if (octas[3 - i] > 0) {
            angle = toGradus(-1 * Math.atan2(-dy, -dx));
            angle2 = angle + 360;
            if (angle < 0 || angle % Math.PI == 0) {
                while (angle < sa) angle += 2 * Math.PI;
            }
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 3 - i, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 3 - i, map);
        }

        rx = cx - dx;
        ry = cy + dy;
        if (octas[4 + i] == -1) colorPixel(rx, ry, c, -1, pts, minax, 4 + i, map);
        else if (octas[4 + i] > 0) {
            angle = toGradus(Math.atan2(dx, -dy)) + 90;
            angle2 = angle + 360;
            if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, 4 + i, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, 4 + i, map);
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

    private void colorPixel(int x, int y, Color c, int angle, int[][] pts, int[] minax, int i, Map<Integer, List<PixelType>> map) {
        pd.colorPixel(x, y, c);

        if (!map.containsKey(x)) {
            map.put(x, new ArrayList<>(Arrays.asList(new PixelType(y, true))));
        } else {
            map.get(x).add(new PixelType(y, true));
        }

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
        return (int) (angle * GRADUS);
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
        } else if (e % 90 == 0) {
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
