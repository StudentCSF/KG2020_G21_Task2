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

    private void drawLine(final int x1, final int y1, final int x2, final int y2, Color color, Map<Integer, int[]> map) {
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

        pd.colorPixel(x, y);
        if (!map.containsKey(x)) {
            map.put(x, new int[]{-y, -1, -1, -1});
        } else if (map.get(x)[1] == -1) {
            map.get(x)[1] = -y;
        } else if (map.get(x)[2] == -1) {
            map.get(x)[2] = -y;
        } else if (map.get(x)[3] == -1) {
            map.get(x)[3] = -y;
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
            pd.colorPixel(x, y);
            if (!map.containsKey(x)) {
//                System.out.println(1);
                map.put(x, new int[]{-y, -1, -1, -1});
            } else if (map.get(x)[1] == -1) {
//                System.out.println(2);
                map.get(x)[1] = -y;
            } else if (map.get(x)[2] == -1) {
                map.get(x)[2] = -y;
            } else if (map.get(x)[3] == -1) {
                map.get(x)[3] = -y;
            }
        }
    }

    private int[] specialCase(int x, int y, int xr, int yr, int angle, Color c) {
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


    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        Map<Integer, int[]> map = new HashMap<>();
        int[] alonePoint = null;
        double[] arcTgAlonePoint = new double[]{Double.MAX_VALUE};
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

        int[][] pts = new int[][]{
                {-1, -1},
                {-1, -1}
        };
        double[] minax = new double[]{Double.MAX_VALUE, Double.MIN_VALUE};

        int[] octants = new int[8];
        setOctants(octants, startAngle, endAngle);

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
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax, octants, 0, alonePoint, arcTgAlonePoint, map);
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
            colorPixels(centerX, centerY, x, y, radStartAngle, radEndAngle, c, pts, minax, octants, 1, alonePoint, arcTgAlonePoint, map);
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
        if (pts[0][0] == -1 && pts[1][0] == -1) return;
        else if (pts[0][0] == -1 && pts[1][0] != -1) {
            pts[0][0] = pts[1][0];
            pts[0][1] = pts[1][1];
        } else if (pts[1][0] == -1 && pts[0][0] != -1) {
            pts[1][0] = pts[0][0];
            pts[1][1] = pts[0][1];
        }
        if (alonePoint == null) {
            drawLine(centerX, centerY, pts[0][0], pts[0][1], c, map);
            drawLine(centerX, centerY, pts[1][0], pts[1][1], c, map);
            if (startAngle <= 90 && endAngle >= 90) {
                ld.drawLine(centerX, centerY, centerX, centerY - yRadius, c);
            }
            if (startAngle <= 270 && endAngle >= 270) {
                ld.drawLine(centerX, centerY, centerX, centerY + yRadius, c);
            }
            /*for (Map.Entry<Integer, int[]> kv : map.entrySet()) {
                ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[1], c);
                if (kv.getValue()[2] != -1 && kv.getValue()[3] == -1) {
                    ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[2], c);
                    ld.drawLine(kv.getKey(), kv.getValue()[1], kv.getKey(), kv.getValue()[2], c);
                }
//                else if (kv.getValue()[2] != -1 && kv.getValue()[3] == -1) ld.drawLine(kv.getKey(), kv.getValue()[0]);
            }*/
           /* for (Map.Entry<Integer, int[]> kv : map.entrySet()) {
                int y1 = kv.getValue()[0];
                int y2 = kv.getValue()[1];
                int y3 = kv.getValue()[2];
                int y4 = kv.getValue()[3];
                if (y3 == -1 && y4 == -1) {
                    ld.drawLine(kv.getKey(), Math.abs(y1), kv.getKey(), Math.abs(y2), c);
                } else if (y2 < -1 && y4 == -1) {
                    ld.drawLine(kv.getKey(), Math.abs(y1), kv.getKey(), Math.abs(y2));
                    ld.drawLine(kv.getKey(), Math.abs(y3), kv.getKey(), Math.abs(y2));
                } else if (y3 < -1 && y4 == -1) {
                    ld.drawLine(kv.getKey(), Math.abs(y1), kv.getKey(), Math.abs(y3));
                    ld.drawLine(kv.getKey(), Math.abs(y2), kv.getKey(), Math.abs(y3));
                } else if (y4 < -1) {
                    kv.getValue()[3] = Math.abs(y4);
                    Arrays.sort(kv.getValue());
//                    if (Math.abs(kv.getValue()[3] - kv.getValue()[0]) < 3) {
//                        kv.getValue()[3] = centerY;
//                        ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[3], c);
//                    } else {
                        ld.drawLine(kv.getKey(), Math.abs(kv.getValue()[3]), kv.getKey(), Math.abs(kv.getValue()[2]), c);
                        ld.drawLine(kv.getKey(), Math.abs(kv.getValue()[0]), kv.getKey(), Math.abs(kv.getValue()[1]), c);
//                    }
//                    ld.drawLine(kv.getKey(), Math.abs(y1), kv.getKey(), Math.abs(y2), c);
//                    ld.drawLine(kv.getKey(), Math.abs(y3), kv.getKey(), Math.abs(y4), c);
                } else {
                    System.out.println(kv.getKey() + "  " + Arrays.toString(kv.getValue()));
                    Arrays.sort(kv.getValue());
                    if (Math.abs(kv.getValue()[3] - kv.getValue()[0]) < 4) {
                        kv.getValue()[3] = centerY;
//                        ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[3], c);
                    } else {
//                        kv.getValue()[3] = centerY;
//                        Arrays.sort(kv.getValue());
//                        if (Math.atan2(kv.getValue()[3] - kv.getValue()[0], kv.getKey() - centerY) < endAngle)
//                        ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[3], c);
                        ld.drawLine(kv.getKey(), kv.getValue()[3], kv.getKey(), kv.getValue()[2], c);
                        ld.drawLine(kv.getKey(), kv.getValue()[0], kv.getKey(), kv.getValue()[1], c);
                    }
                }
            }*/
            for (Map.Entry<Integer, int[]> kv : map.entrySet()) {
                List<Integer> ygr = new ArrayList<>();
                for (int v : kv.getValue()) {
                    if (v != -1) ygr.add(Math.abs(v));
                }
                if (ygr.size() == 2) {
                    if (Math.abs(centerX - kv.getKey()) < xRadius) {
                        ld.drawLine(kv.getKey(), ygr.get(0), kv.getKey(), ygr.get(1), c);
                    } else System.out.println(ygr.toString());
                } else if (ygr.size() == 4) {
                    if (Math.abs(centerX - kv.getKey()) < xRadius * 0.75) {
                        Collections.sort(ygr);
                        ld.drawLine(kv.getKey(), ygr.get(0), kv.getKey(), ygr.get(1), c);
                        ld.drawLine(kv.getKey(), ygr.get(2), kv.getKey(), ygr.get(3), c);
                    }
                }
            }
        } else ld.drawLine(centerX, centerY, alonePoint[0], alonePoint[1], c);
    }

    private void setOctants(int[] octas, int s, int e) {
        int i = 0;
        int p1 = 0, p2 = 45;
        while (p1 < e) {
            int index = i % 8;
            if (p2 <= s) {
            } else if (p1 - 1 >= s && p2 + 1 <= e) octas[index] = -1;
            else octas[index] = i / 8 + 1;
            i++;
            p1 += 45;
            p2 += 45;
        }
    }

    private void colorPixels(final int cx, final int cy, final int dx, final int dy, final double sa, final double ea, Color c, int[][] pts, double[] minax, int[] octas, int i, int[] alonePoint, double[] arctg, Map<Integer, int[]> map) {
        double angle;
        double angle2;

        int rx = cx + dx;
        int ry = cy + dy;
        if (octas[7 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, map);
        else if (octas[7 - i] > 0) {
            angle = Math.atan2(dx, dy) + 1.5 * Math.PI;
            angle2 = angle + 2 * Math.PI - Math.PI / 180;
            if (alonePoint != null) {
                double diff = angle - sa;
                if (Math.abs(arctg[0]) > Math.abs(diff)) {
                    arctg[0] = diff;
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, map);
        }
        rx = cx + dx;
        ry = cy - dy;
        if (octas[i] == -1) colorPixel(rx, ry, c, -1, pts, minax, map);
        else if (octas[i] > 0) {
            angle = -1 * Math.atan2(-dy, dx);
            angle2 = angle + 2 * Math.PI - Math.PI / 180;
            if (alonePoint != null) {
                double diff = angle - sa;
                if (Math.abs(arctg[0]) > Math.abs(diff)) {
                    arctg[0] = diff;
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, map);
        }

        rx = cx - dx;
        ry = cy - dy;
        if (octas[3 - i] == -1) colorPixel(rx, ry, c, -1, pts, minax, map);
        else if (octas[3 - i] > 0) {
            angle = -1 * Math.atan2(-dy, -dx);
            angle2 = angle + 2 * Math.PI - Math.PI / 180;
            if (angle < 0 || angle % Math.PI == 0) {
                while (angle < sa) angle += 2 * Math.PI;
            }
            if (alonePoint != null) {
                double diff = angle - sa;
                if (Math.abs(arctg[0]) > Math.abs(diff)) {
                    arctg[0] = diff;
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, map);
        }

        rx = cx - dx;
        ry = cy + dy;
        if (octas[4 + i] == -1) colorPixel(rx, ry, c, -1, pts, minax, map);
        else if (octas[4 + i] > 0) {
            angle = Math.atan2(dx, -dy) + 0.5 * Math.PI;
            angle2 = angle + 2 * Math.PI - Math.PI / 180;
            if (alonePoint != null) {
                double diff = angle - sa;
                if (Math.abs(arctg[0]) > Math.abs(diff)) {
                    arctg[0] = diff;
                    alonePoint[0] = rx;
                    alonePoint[1] = ry;
                }
            } else if (angle >= sa && angle <= ea) colorPixel(rx, ry, c, angle, pts, minax, map);
            else if (angle2 >= sa && angle2 <= ea) colorPixel(rx, ry, c, angle2, pts, minax, map);
        }
    }

    private void colorPixel(int x, int y, Color c, double angle, int[][] pts, double[] minax, Map<Integer, int[]> map) {
        pd.colorPixel(x, y, c);
        if (!map.containsKey(x)) {
            map.put(x, new int[]{y, -1, -1, -1});
        } else if (map.get(x)[1] == -1) {
            map.get(x)[1] = y;
        } else if (map.get(x)[2] == -1) {
//            System.out.println(3);
            map.get(x)[2] = y;
        } else if (map.get(x)[3] == -1) {
//            System.out.println(4);
            map.get(x)[3] = y;
        }
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
