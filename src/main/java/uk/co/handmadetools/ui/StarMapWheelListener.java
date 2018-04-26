package uk.co.handmadetools.ui;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class StarMapWheelListener implements MouseWheelListener {

    private StarMap starMap;

    public StarMapWheelListener(StarMap starMap) {
        this.starMap = starMap;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        starMap.zoom(e.getWheelRotation(), e.getX(), e.getY());
    }

}
