package ru.vsu.cs.valeev;

import ru.vsu.cs.valeev.pixel_lines.BresenhamLineDrawer;
import ru.vsu.cs.valeev.pixel_lines.DDALineDrawer;
import ru.vsu.cs.valeev.pixel_lines.WuLineDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements MouseMotionListener {
    private Point2D position = new Point(0 , 0);

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
        LineDrawer ld = new DDALineDrawer(gr);
        //LineDrawer ld = new BresenhamLineDrawer(gr);
        //LineDrawer ld = new WuLineDrawer(gr);
        //c помощью свич кейс создавать разные лд
        drawAll(ld);
        g.drawImage(bi, 0, 0, null);
        gr.dispose();
    }

    public static void drawSnowflake(LineDrawer ld, int x, int y, int r, int n) {
        double da = 2 * Math.PI / n;
        for (int i = 0; i < n; i++) {
            double a = da * i;
            double dx = Math.cos(a) * r;
            double dy = Math.sin(a) * r;
            ld.drawLine(x, y, x + (int) dx, y + (int) dy);
        }
    }

    private void drawAll(LineDrawer ld) {
        drawSnowflake(ld, 100, 100, 50, 12);
        ld.drawLine(getWidth() / 2, getHeight() / 2, (int)this.position.getX(), (int)this.position.getY());
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
