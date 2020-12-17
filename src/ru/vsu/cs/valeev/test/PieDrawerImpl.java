package ru.vsu.cs.valeev.test;

import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PieDrawer;
import ru.vsu.cs.valeev.drawers.arc.BresenhamPieDrawer;

import java.awt.*;

public class PieDrawerImpl implements PieDrawer {
    private BresenhamPieDrawer bpd;

    public PieDrawerImpl(BresenhamPieDrawer bpd) {
        this.bpd = bpd;
    }

    @Override
    public void drawPie(int x, int y, int width, int height, double startAngle, double arcAngle, Color c) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        double aStart = 180 * startAngle / Math.PI;
        double aArc = 180 * arcAngle/ Math.PI;
        double aEnd = aStart + aArc;
        bpd.draw(centerX, centerY, width / 2, height / 2, (int)aStart, (int)aEnd, c);
    }
}
