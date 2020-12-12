package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public class GraphicsArcDrawer implements ArcDrawer {
    Graphics g;

    public GraphicsArcDrawer(Graphics g) {
        this.g = g;
    }

    @Override
    public void drawArc(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c) {
        g.setColor(c);
        g.drawArc(centerX - width, centerY - height, 2 * width, 2 * height, startAngle, endAngle);
    }
}
