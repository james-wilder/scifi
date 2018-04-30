package uk.co.handmadetools.ui;

import uk.co.handmadetools.event.State;
import uk.co.handmadetools.universe.Galaxy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window extends JFrame {

    private Galaxy galaxy;
    private StarMap starMap;

    public Window(Galaxy galaxy) {
        this.galaxy = galaxy;

        starMap = new StarMap(galaxy);
        add(starMap);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void refresh(State state) {
        starMap.refresh(state);
    }
}
