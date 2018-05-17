package uk.co.handmadetools.game;

import uk.co.handmadetools.Lifeform;
import uk.co.handmadetools.LifeformAttribute;

public class State {

    public Lifeform creator;
    public boolean gameOver = false;
    public Stat selfDestruction;
    public Stat environmentalDestruction;
    public float at;

    public State update(Event event, Option option) {
        State state = new State();

        state.creator = creator;
        state.gameOver = gameOver;
        state.at = event.at;

        state.selfDestruction = selfDestruction.getValueAt(event.at);
        state.environmentalDestruction = environmentalDestruction.getValueAt(event.at);

        if (event.eventType == EventType.CHOSE_CREATOR) {
            state.creator = option.lifeform;

            if (state.creator.attributes.contains(LifeformAttribute.WARLIKE)) {
                state.selfDestruction.changeRate *= 2.0;
            }

            if (state.creator.attributes.contains(LifeformAttribute.PACIFIST)) {
                state.selfDestruction.changeRate *= 0.5;
            }
        }

        if (event.eventType == EventType.GAME_OVER_ENVIRONMENTAL) {
            state.gameOver = true;
        }

        if (event.eventType == EventType.GAME_OVER_SELF_DESTRUCTION) {
            state.gameOver = true;
        }

        if (event.eventType == EventType.GAME_OVER_GALAXY_ENDS) {
            state.gameOver = true;
        }

        return state;
    }

}
