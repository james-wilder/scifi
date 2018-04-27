package uk.co.handmadetools.event;

import uk.co.handmadetools.universe.Galaxy;

import java.util.ArrayList;
import java.util.List;

public class State {

    public float at = 0;
    public Galaxy galaxy;
    public List<Event> events = new ArrayList<>();

    public State(Galaxy galaxy) {
        this.galaxy = galaxy;
    }


    public State update(Event event) {
        State newState = new State(galaxy);
        newState.events.addAll(events);
        newState.events.add(event);

        // TODO: lifeform evolves

        // TODO: create new state

        return newState;
    }
}
