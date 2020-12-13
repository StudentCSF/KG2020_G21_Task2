package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public class GraphicsArcDrawer implements ArcDrawer {
    Graphics g;

    public GraphicsArcDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void draw(int centerX, int centerY, int xRadius, int yRadius, int startAngle, int endAngle, Color c) {
        g.setColor(c);
        g.drawArc(centerX - xRadius, centerY - yRadius, 2 * xRadius, 2 * yRadius, startAngle, endAngle);
    }
}
