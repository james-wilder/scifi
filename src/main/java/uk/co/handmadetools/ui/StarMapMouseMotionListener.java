package uk.co.handmadetools.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class StarMapMouseMotionListener implements MouseMotionListener {

    private StarMap starMap;

    public StarMapMouseMotionListener(StarMap starMap) {
        this.starMap = starMap;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        System.out.println("Drag");
//        System.out.println(e.getX() + " " + e.getY());

        starMap.dragged(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
