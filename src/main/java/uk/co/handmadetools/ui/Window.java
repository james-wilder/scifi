package uk.co.handmadetools.ui;

import uk.co.handmadetools.universe.Galaxy;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Window extends JFrame {

    private Galaxy galaxy;

    public Window(Galaxy galaxy) {
        this.galaxy = galaxy;

        add(new StarMap(galaxy));
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
