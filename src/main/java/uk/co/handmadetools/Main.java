package uk.co.handmadetools;

import uk.co.handmadetools.names.NameGenerator;
import uk.co.handmadetools.ui.Window;
import uk.co.handmadetools.universe.Galaxy;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello worlds");

        NameGenerator nameGenerator = new NameGenerator();
        for (int i = 0; i < 20; i++) {
            System.out.println(nameGenerator.newName());
        }

        Galaxy galaxy = new Galaxy();
        Window window = new Window(galaxy);
    }

}
