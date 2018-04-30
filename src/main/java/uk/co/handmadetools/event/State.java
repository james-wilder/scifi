package uk.co.handmadetools.event;

import uk.co.handmadetools.universe.Galaxy;
import uk.co.handmadetools.universe.Lifeform;
import uk.co.handmadetools.universe.Population;
import uk.co.handmadetools.universe.Star;
import uk.co.handmadetools.util.FormatAt;
import uk.co.handmadetools.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class State {

    public float at = 0;
    public Galaxy galaxy;
    public List<Event> events = new ArrayList<>();
    public List<Lifeform> lifeforms = new ArrayList<>();
    public List<Population> populations = new ArrayList<>();

    public State(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    public State update(Event event) {
        State newState = new State(galaxy);
        newState.events.addAll(events);
        newState.events.add(event);
        newState.at = event.at;
        newState.lifeforms.addAll(lifeforms);
        newState.populations.addAll(populations);

        // TODO: lifeform evolves
        if (event.type == EventType.LifeformStart) {
            Lifeform lifeform = new Lifeform();
            Pair<Star, Float> lifeformStart = new Pair<>(event.star, event.at);
            lifeform.name = galaxy.getName(lifeformStart);
            lifeform.color = galaxy.pickColor(lifeform.name);
            newState.lifeforms.add(lifeform);

            Population population = new Population();
            population.lifeform = lifeform;
            population.location = event.star;
            population.size = 1;
            newState.populations.add(population);

            System.out.println(FormatAt.format(event.at) + " New lifeform " + lifeform.name + " evolves at " + event.star.name + " (" + newState.lifeforms.size() + " lifeforms)");
        }

        // TODO: create new state

        return newState;
    }

    public List<Population> getPopulations(Star star) {
        return populations.stream()
                .filter(population -> population.location == star)
                .collect(Collectors.toList());
    }
}
