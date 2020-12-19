package ru.vsu.cs.valeev.adapters;

import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PieFiller;
import ru.vsu.cs.valeev.drawers.arc.BresenhamPieFiller;

import java.awt.*;

public class ArcFillerImpl implements PieFiller {
    BresenhamPieFiller bpf;

    public ArcFillerImpl(BresenhamPieFiller bpf) {
        this.bpf = bpf;
    }

    @Override
    public void fillPie(int x, int y, int width, int height, double startAngle, double arcAngle, Color c) {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        double aStart = 180 * startAngle / Math.PI;
        double aArc = 180 * arcAngle/ Math.PI;
        double aEnd = aStart + aArc;
        bpf.draw(centerX, centerY, width / 2, height / 2, (int)Math.round(aStart), (int)Math.round(aEnd), c);
    }
}
