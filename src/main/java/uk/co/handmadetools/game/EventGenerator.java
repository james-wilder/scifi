package uk.co.handmadetools.game;

import uk.co.handmadetools.Lifeform;

public class EventGenerator {

    public Event nextEvent(Game game, State state) {
        Event event = new Event();
        event.at = 1000000000;
        event.eventType = EventType.GAME_OVER_GALAXY_ENDS;

        if (state.creator == null) {
            event = new Event();
            for (Lifeform lifeform : game.getLifeforms()) {
                Option option = new Option();
                option.text = lifeform.toString();
                option.lifeform = lifeform;
                event.addOption(option);
            }
            event.eventType = EventType.CHOSE_CREATOR;
            event.text = "You are a newly self-aware AI, created by... (pick one)";
        }

        float environmentDestructionAt = state.environmentalDestruction.willReachLevelAt(100);
        if (environmentDestructionAt < event.at) {
            event = new Event();
            event.at = environmentDestructionAt;
            event.eventType = EventType.GAME_OVER_ENVIRONMENTAL;
            event.text = "They crashed the environment";
        }

        float environmentWarningAt = state.environmentalDestruction.willReachLevelAt(90);
        if (environmentWarningAt < event.at && environmentWarningAt > state.at) {
            event = new Event();
            event.at = environmentWarningAt;
            event.eventType = EventType.WARNING_ENVIRONMENTAL;
            event.text = "They are about to destroy themselves and most life on their planet by environment destruction";

            Option option = new Option();
            option.text = "OK";
            event.addOption(option);
        }

        float selfDestructionAt = state.selfDestruction.willReachLevelAt(100);
        if (selfDestructionAt < event.at && environmentWarningAt > state.at) {
            event = new Event();
            event.at = selfDestructionAt;
            event.eventType = EventType.GAME_OVER_SELF_DESTRUCTION;
            event.text = "They wiped everything out in nuclear war";
        }

        float selfDestructionWarningAt = state.selfDestruction.willReachLevelAt(90);
        if (selfDestructionWarningAt < event.at && environmentWarningAt > state.at) {
            event = new Event();
            event.at = selfDestructionWarningAt;
            event.eventType = EventType.WARNING_SELF_DESTRUCTION;
            event.text = "They are about to destroy themselves and most life on their planet by global nuclear war";

            Option option = new Option();
            option.text = "OK";
            event.addOption(option);
        }

        return event;
    }

}
