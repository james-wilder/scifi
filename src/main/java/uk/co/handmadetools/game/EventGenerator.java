package uk.co.handmadetools.game;

import java.util.ArrayList;
import java.util.List;

public class EventGenerator {

    public List<Event> nextEvents(Game game, State state) {
        List<Event> events = new ArrayList<>();
        Option option;

        Event event = new Event();
        event.at = 1000000000;
        event.eventType = EventType.GAME_OVER_GALAXY_ENDS;

        addEvent(state, events, event);

        if (state.creator == null) {
            event = new Event();
            for (Lifeform lifeform : game.getLifeforms()) {
                option = new Option();
                option.text = lifeform.toString();
                option.lifeform = lifeform;
                event.addOption(option);
            }
            event.eventType = EventType.CHOSE_CREATOR;
            event.text = "You are a newly self-aware AI, created by... (pick one)";

            addEvent(state, events, event);
        }

        float environmentDestructionAt = state.environmentalDestruction.willReachLevelAt(100);
        event = new Event();
        event.at = environmentDestructionAt;
        event.eventType = EventType.GAME_OVER_ENVIRONMENTAL;
        event.text = "They crashed the environment";
        event.addOption(okOption());
        addEvent(state, events, event);

        float selfDestructionAt = state.selfDestruction.willReachLevelAt(100);
        event = new Event();
        event.at = selfDestructionAt;
        event.eventType = EventType.GAME_OVER_SELF_DESTRUCTION;
        event.text = "They wiped everything out in nuclear war";
        event.addOption(okOption());
        addEvent(state, events, event);

        float environmentWarningAt = state.environmentalDestruction.willReachLevelAt(90);
        event = new Event();
        event.at = environmentWarningAt;
        event.eventType = EventType.WARNING_ENVIRONMENTAL;
        event.text = "They are about to destroy themselves and most life on their planet by environment destruction";
        event.addOption(okOption());
        addEvent(state, events, event);

        float selfDestructionWarningAt = state.selfDestruction.willReachLevelAt(90);
        event = new Event();
        event.at = selfDestructionWarningAt;
        event.eventType = EventType.WARNING_SELF_DESTRUCTION;
        event.text = "They are about to destroy themselves and most life on their planet by global nuclear war";
        event.addOption(okOption());
        addEvent(state, events, event);

        return events;
    }

    private Option okOption() {
        Option option = new Option();
        option.text = "OK";
        return option;
    }

    private void addEvent(State state, List<Event> events, Event event) {
        if (event.at < state.at) {
            return;
        }

        if (events.size() == 0) {
            events.add(event);
        } else if (event.at < events.get(0).at) {
            events.clear();
            events.add(event);
        } else if (event.at == events.get(0).at) {
            events.add(event);
        } // else event.at > events.get(0).at
    }

}
