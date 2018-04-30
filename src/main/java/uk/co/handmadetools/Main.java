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
        Galaxy galaxy = new Galaxy(nameGenerator);
        Window window = new Window(galaxy);

        EventGenerator eventGenerator = new EventGenerator(galaxy);

        System.out.println("Generating events...");
        State state = new State(galaxy);
        while (true) {
            Event event = eventGenerator.getNextEvent(state);

            State newState = state.update(event);

            // TODO: archive states for timeline skipping

            state = newState;

            window.refresh(state);

            if (event.type == EventType.GalaxyEnd) {
                System.out.println("That's all folks");
                break;
            }
        }
    }

}
