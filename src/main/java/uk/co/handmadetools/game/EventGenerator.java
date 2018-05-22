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
                option.text = lifeform.toString() + " ... " + lifeform.startAt;
                option.lifeform = lifeform;
                event.addOption(option);
            }
            event.eventType = EventType.CHOSE_CREATOR;
            event.text = "You are a newly self-aware AI, created by... (pick one)";

            addEvent(state, events, event);
        }

        for (Lifeform lifeform : game.getLifeforms()) {
            LifeformState lifeformState = state.lifeformStateMap.get(lifeform);

            if (!lifeformState.started) {
                event = new Event();
                event.at = lifeform.startAt;
                event.eventType = EventType.LIFEFORM_START;
                event.lifeform = lifeform;
                event.text = "The " + lifeform.name + " have started";
                if (lifeform == state.creator) {
                    event.addOption(okOption());
                }
                addEvent(state, events, event);
            } else {
                float selfDestructionAt = lifeformState.selfDestruction.willReachLevelAt(100);
                event = new Event();
                event.at = selfDestructionAt;
                event.eventType = EventType.GAME_OVER_SELF_DESTRUCTION;
                event.lifeform = lifeform;
                event.text = "The " + lifeform.name + " wiped everything out in nuclear war";
                if (lifeform == state.creator) {
                    event.addOption(okOption());
                }
                addEvent(state, events, event);

                float environmentDestructionAt = lifeformState.environmentalDestruction.willReachLevelAt(100);
                event = new Event();
                event.at = environmentDestructionAt;
                event.eventType = EventType.GAME_OVER_ENVIRONMENTAL;
                event.lifeform = lifeform;
                event.text = "The " + lifeform.name + " crashed the environment";
                if (lifeform == state.creator) {
                    event.addOption(okOption());
                }
                addEvent(state, events, event);

                float selfDestructionWarningAt = lifeformState.selfDestruction.willReachLevelAt(90);
                event = new Event();
                event.at = selfDestructionWarningAt;
                event.eventType = EventType.WARNING_SELF_DESTRUCTION;
                event.lifeform = lifeform;
                event.text = "The " + lifeform.name + " are about to destroy themselves and most life on their planet by global nuclear war";
                if (lifeform == state.creator) {
                    event.addOption(okOption());
                }
                addEvent(state, events, event);

                float environmentWarningAt = lifeformState.environmentalDestruction.willReachLevelAt(90);
                event = new Event();
                event.at = environmentWarningAt;
                event.eventType = EventType.WARNING_ENVIRONMENTAL;
                event.lifeform = lifeform;
                event.text = "The " + lifeform.name + " are about to destroy themselves and most life on their planet by environment destruction";
                if (lifeform == state.creator) {
                    event.addOption(okOption());
                }
                addEvent(state, events, event);
            }

            for (Lifeform lifeform1 : game.getLifeforms()) {
                for (Lifeform lifeform2 : game.getLifeforms()) {
                    if (!lifeform1.hasDiscovered(lifeform2)) {
                        float d = lifeform1.origin.getDist(lifeform2.origin);
                        //                    float timeToDiscover =
                    }
                }
            }
        }

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
