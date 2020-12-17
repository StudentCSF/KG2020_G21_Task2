package ru.vsu.cs.valeev.test;

import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.ArcDrawer;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PieDrawer;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.PixelDrawer;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.graphics_impl.PrimitivesFactoryWithDefaultGraphicsImplementation;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.graphics_impl.arc.ArcDrawerFactoryByPixelDrawer;
import ru.vsu.cs.kg2020.nuzhnykh_a_v.task2.graphics_impl.pie.PieDrawerFactoryByPixelDrawer;
import ru.vsu.cs.valeev.drawers.arc.BresenhamArcDrawer;
import ru.vsu.cs.valeev.drawers.arc.BresenhamPieDrawer;
import ru.vsu.cs.valeev.drawers.line.BresenhamLineDrawer;

public class MyFactoryImplementation extends PrimitivesFactoryWithDefaultGraphicsImplementation {

    @Override
    protected PieDrawerFactoryByPixelDrawer getCustomPieDrawerFactory() {
        return new PieDrawerFactoryByPixelDrawer() {
            @Override
            public PieDrawer createInstance(PixelDrawer pd) {
                MyPixelDrawerImpl mpdi = new MyPixelDrawerImpl(pd);
                BresenhamPieDrawer bpd = new BresenhamPieDrawer(mpdi, new BresenhamLineDrawer(mpdi));
                return new PieDrawerImpl(bpd);
            }
        };
    }

    @Override
    protected ArcDrawerFactoryByPixelDrawer getCustomArcDrawerFactory() {
        return new ArcDrawerFactoryByPixelDrawer() {
            @Override
            public ArcDrawer createInstance(PixelDrawer pd) {
                MyPixelDrawerImpl mpdi = new MyPixelDrawerImpl(pd);
                BresenhamArcDrawer bad = new BresenhamArcDrawer(mpdi);
                return new OpenArcDrawerImpl(bad);
            }
        };
    }
}
