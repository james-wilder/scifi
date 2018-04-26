package uk.co.handmadetools.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StarMapMouseListener implements MouseListener {
    private StarMap starMap;

    public StarMapMouseListener(StarMap starMap) {
        this.starMap = starMap;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        starMap.click(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
