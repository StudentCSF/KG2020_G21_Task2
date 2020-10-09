package ru.vsu.cs.valeev;

import ru.vsu.cs.valeev.pixel_lines.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements MouseMotionListener {
    private Point2D position = new Point(0, 0);

    public DrawPanel() {
        this.addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics gr = bi.createGraphics();
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.setColor(Color.BLACK);

        PixelDrawer pd = new GraphicsPixelDrawer(gr);

        //LineDrawer ld = new DDALineDrawer(pd);
        //LineDrawer ld = new BresenhamLineDrawer(pd);
        LineDrawer ld = new WuLineDrawer(pd);
        drawAll(ld);
        g.drawImage(bi, 0, 0, null);
        gr.dispose();
    }

    public static void drawSnowflake(LineDrawer ld, int x, int y, int r, int n) {
        double da = 2 * Math.PI / n;

        double a, dx, dy;
        for (int i = 0; i < n; i++) {
            a = da * i;
            dx = Math.cos(a) * r;
            dy = Math.sin(a) * r;
            ld.drawLine(x, y, x + (int) dx, y + (int) dy);
        }
    }

    private void drawAll(LineDrawer ld) {
        drawSnowflake(ld, 100, 100, 50, 12);
        ld.drawLine(getWidth() / 2, getHeight() / 2, (int) this.position.getX(), (int) this.position.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.position = new Point(e.getX(), e.getY());
        repaint();
    }
}
