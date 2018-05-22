package uk.co.handmadetools.game;

import java.util.HashMap;
import java.util.Map;

public class State {

    public Lifeform creator;
    public boolean gameOver = false;
    public float at;
    public Map<Lifeform, LifeformState> lifeformStateMap = new HashMap<>();

    public State update(Event event, Option option) {
        State state = new State();

        state.creator = creator;
        state.gameOver = gameOver;
        state.at = event.at;

        for (Map.Entry<Lifeform, LifeformState> lifeformState : lifeformStateMap.entrySet()) {
            LifeformState oldState = lifeformStateMap.get(lifeformState.getKey());
            LifeformState newState = oldState.update(event, lifeformState.getKey());
            state.lifeformStateMap.put(lifeformState.getKey(), newState);
        }

        if (event.eventType == EventType.CHOSE_CREATOR) {
            state.creator = option.lifeform;
        }

        if ((event.eventType == EventType.GAME_OVER_SELF_DESTRUCTION) && (event.lifeform == creator)) {
            state.gameOver = true;
        }

        if (event.eventType == EventType.GAME_OVER_GALAXY_ENDS) {
            state.gameOver = true;
        }

        return state;
    }

}
