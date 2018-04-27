package uk.co.handmadetools;

import uk.co.handmadetools.event.Event;
import uk.co.handmadetools.event.EventGenerator;
import uk.co.handmadetools.event.EventType;
import uk.co.handmadetools.event.State;
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

        EventGenerator eventGenerator = new EventGenerator(galaxy);

        State state = new State(galaxy);
        while (true) {
            Event event = eventGenerator.getNextEvent(state);

            State newState = state.update(event);

            // TODO: archive states for timeline skipping

            state = newState;

            if (event.type == EventType.GalaxyEnd) {
                System.out.println("That's all folks");
                break;
            }
        }
    }

}
