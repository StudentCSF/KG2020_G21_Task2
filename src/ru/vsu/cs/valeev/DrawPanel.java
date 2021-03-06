package ru.vsu.cs.valeev;

import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PieDrawer;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.testing.TestArcs;
import ru.vsu.cs.valeev.drawers.arc.*;
import ru.vsu.cs.valeev.drawers.line.BresenhamLineDrawer;
import ru.vsu.cs.valeev.drawers.line.DDALineDrawer;
import ru.vsu.cs.valeev.drawers.line.GraphicsLineDrawer;
import ru.vsu.cs.valeev.drawers.line.LineDrawer;
import ru.vsu.cs.valeev.drawers.pixel.GraphicsPixelDrawer;
import ru.vsu.cs.valeev.drawers.pixel.PixelDrawer;
import ru.vsu.cs.valeev.test.MyFactoryImplementation;
import ru.vsu.cs.valeev.test.PieDrawerImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DrawPanel extends JPanel implements MouseMotionListener {
    private Point2D position = new Point(0, 0);

    public DrawPanel() {
        this.addMouseMotionListener(this);
        try {
            TestArcs.startTest(new MyFactoryImplementation(), TestArcs.IMG_DIFF, TestArcs.TEST_ARC, true, "D:\\Учёба\\КГ\\KG2020_G21_Task2\\results");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g) {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = bi.createGraphics();
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.setColor(Color.BLACK);

        PixelDrawer pd = new GraphicsPixelDrawer(gr);

//        LineDrawer ld = new GraphicsLineDrawer(gr);
        LineDrawer ld = new DDALineDrawer(pd);
//        LineDrawer ld = new BresenhamLineDrawer(pd);
//        LineDrawer ld = new MyBresenhamLineDrawer(pd);
//        LineDrawer ld = new WuLineDrawer(pd);
//        drawAll(ld);
//        ArcDrawer adf = new GraphicsArcFiller(gr);
        ArcDrawer add = new GraphicsArcDrawer(gr);
//        add.draw(500, 500, 100, 100, 130, 457);
//        gr.drawArc(300, 300, 100, 100, 130, 377);
        ArcDrawer adb = new BresenhamArcDrawer(pd);
        ArcDrawer adp = new BresenhamPieDrawer(pd, ld);
//        adb.draw(300, 300, 100, 100, 180, 190);
//        adb.draw(300, 300, 300, 100, 0, 360, Color.GREEN);
//        adb.draw(300, 300, 200, 50, 0, 36, Color.RED);
//        adp.draw(300, 300, 200, 50, 10, 11, Color.RED);
//        adp.draw(300, 300, 200, 50, 36, 72, Color.RED);
//        adp.draw(300, 300, 200, 50, 72, 108, Color.RED);
//        adp.draw(300, 300, 200, 50, 108, 144, Color.RED);
//        adp.draw(300, 300, 200, 50, 144, 180, Color.RED);
//        adp.draw(300, 300, 200, 50, 180, 216, Color.RED);
//        adp.draw(300, 300, 200, 50, 216, 252, Color.RED);
//        adp.draw(300, 300, 200, 50, 252, 288, Color.RED);
//        adp.draw(300, 300, 200, 50, 288, 324, Color.RED);
//        adp.draw(300, 300, 200, 50, 324, 360, Color.RED);
//        adp.draw(300, 300, 200, 50, 162, 190, Color.GREEN);
//        adb.draw(300, 300, 200, 50, 72, 108, Color.RED);
//        adb.draw(300, 300, 200, 50, 108, 144, Color.RED);
//        adb.draw(300, 300, 200, 50, 144, 180, Color.RED);
//        adp.draw(300, 300, 200, 50, 0, 180, Color.GREEN);
//        new PieDrawerImpl((BresenhamPieDrawer) adp).drawPie(400, 200, 200, 200, 10 * Math.PI / 180, Math.PI / 180, Color.BLACK);

//        testQuarters(adb);
//        testAlmostCircles(adp);
//        testMiniPies(adp);
//        Arc2D arc2D = new Arc2D.Double(200, 200, 200, 200, 60, 70, Arc2D.PIE);
//        gr.draw(arc2D);


//        adp.draw(300, 300, 100, 100, 315, 360);
//        add.draw(300, 300, 100, 100, -180, 0);
//        gr.drawArc(200, 200, 100, 100, 0, 180);
//        ld.drawLine(300, 300, 400, 300);
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

    private static void testMiniPies(ArcDrawer ad) {
        int angle = 3;
        Random rnd = new Random();
        int a = 100, b = 100;
        int start = rnd.nextInt(360 - angle);
        ad.draw(200, 200, a, b, start, start + angle);
        start = rnd.nextInt(360 - angle);
        ad.draw(200, 400, a, b, start, start + angle);
        start = rnd.nextInt(360 - angle);
        ad.draw(400, 200, a, b, start, start + angle);
        start = rnd.nextInt(360 - angle);
        ad.draw(400, 400, a, b, start, start + angle);
    }

    private static void testQuarters(ArcDrawer ad) {
        Random rnd = new Random();
        int a = 100, b = 100;
        int start = rnd.nextInt(271);
        ad.draw(300, 300, 100, 100, start, start + 90);
        ad.draw(510, 490, a, b, 0, 90);
        ad.draw(490, 490, a, b, 90, 180);
        ad.draw(490, 510, a, b, 180, 270);
        ad.draw(510, 510, a, b, 270, 360);
    }

    private static void testAlmostCircles(ArcDrawer ad) {
       Random rnd = new Random();
       int bound = 2;
       int start = rnd.nextInt(bound);
       int angle = 360 - bound + 1;
       int a = 100, b = 100;
       ad.draw(500, 500, a, b, start, start + angle);
    }

    private void drawAll(LineDrawer ld) {
        drawSnowflake(ld, 1000, 500, 20, 40);
        ld.drawLine(getWidth() / 3, getHeight() / 3, (int) this.position.getX(), (int) this.position.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
//        this.position = new Point(e.getX(), e.getY());
//        repaint();
    }
}
