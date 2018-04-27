package uk.co.handmadetools.event;

import uk.co.handmadetools.universe.Galaxy;

public class EventGenerator {

    private Galaxy galaxy;

    public EventGenerator(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    public Event getNextEvent(State state) {
        Event event = new Event();
        event.type = EventType.GalaxyEnd;
        event.at = 100000000000.0f;
        return event;
    }
}
