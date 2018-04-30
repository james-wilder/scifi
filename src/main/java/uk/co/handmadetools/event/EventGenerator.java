package uk.co.handmadetools.event;

import uk.co.handmadetools.universe.Galaxy;
import uk.co.handmadetools.universe.Population;
import uk.co.handmadetools.universe.Star;

import java.util.List;

public class EventGenerator {

    private Galaxy galaxy;

    public EventGenerator(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    public Event getNextEvent(State state) {
        Event event = new Event();
        event.type = EventType.GalaxyEnd;
        event.at = 100000000000.0f;

        for (Star star : galaxy.stars) {
            float starEvolvesLifeAt = galaxy.lifeformEvolvesAt.get(star);
            if ((starEvolvesLifeAt >= state.at) && (starEvolvesLifeAt < event.at)) {
                List<Population> populations = state.getPopulations(star);
                if (populations.size() > 0) {
                    continue;
                }

                Event evolveEvent = new Event();
                evolveEvent.type = EventType.LifeformStart;
                evolveEvent.at = starEvolvesLifeAt;
                evolveEvent.star = star;
                event = evolveEvent;
            }
        }

        for (Star star : galaxy.stars) {
            List<Population> populations = state.getPopulations(star);
            if (populations.size() > 0) {
                continue;
            }

            for (Population population : state.populations) {
                float reachOneBillionAt = population.size.reachLevelAt(10.0f);

                float d = galaxy.getDistance(population.location, star)
                    + 0.01f * galaxy.getDistance(population.lifeform.startLocation, star);

                float techLevelRequired = 10.0f + d * 100.0f;

                float willColonizeAt = Math.max(population.lifeform.tech.reachLevelAt(techLevelRequired), reachOneBillionAt)
                        + d * 100.0f;

                if (willColonizeAt < event.at) {
                    Event evolveEvent = new Event();
                    evolveEvent.type = EventType.Colonization;
                    evolveEvent.at = willColonizeAt;
                    evolveEvent.population = population;
                    evolveEvent.star = star;
                    event = evolveEvent;
                }
            }
        }

        return event;
    }
}
