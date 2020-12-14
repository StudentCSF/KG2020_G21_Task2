package ru.vsu.cs.valeev.test;

import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PixelDrawer;

import java.awt.*;

public class MyPixelDrawerImpl implements ru.vsu.cs.valeev.drawers.pixel.PixelDrawer {
    private PixelDrawer pdInstance;

    public MyPixelDrawerImpl(PixelDrawer pdInstance) {
        this.pdInstance = pdInstance;
    }

    @Override
    public void colorPixel(int x, int y, Color c) {
        pdInstance.setPixel(x, y, c);
    }
}
