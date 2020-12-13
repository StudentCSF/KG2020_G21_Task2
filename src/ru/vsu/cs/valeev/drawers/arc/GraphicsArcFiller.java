package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public class GraphicsArcFiller implements ArcDrawer {
    Graphics g;

    public GraphicsArcFiller(Graphics g) {
        this.g = g;
    }

    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        g.setColor(c);
        g.fillArc(centerX - xRadius, centerY - yRadius, 2 * xRadius, 2 * yRadius, startAngle, endAngle);
    }
}
