package ru.vsu.cs.valeev.drawers.arc;

import java.awt.*;

public class GraphicsArcFiller implements ArcDrawer {
    Graphics g;

    public GraphicsArcFiller(Graphics g) {
        this.g = g;
    }

    @Override
    public void draw(int centerX, int centerY, int width, int height, int startAngle, int endAngle, Color c) {
        g.setColor(c);
        g.fillArc(centerX - width, centerY - height, 2 * width, 2 * height, startAngle, endAngle);
    }
}
