package uk.co.handmadetools.game;

public class LifeformState {

    public Stat selfDestruction;
    public Stat environmentalDestruction;
    public boolean extinct;
    public boolean started;

    public LifeformState update(Event event, Lifeform lifeform) {
        LifeformState state = new LifeformState();

        if (event.at == lifeform.startAt) {
            state.selfDestruction = new Stat(event.at, 0, 1);
            state.environmentalDestruction = new Stat(event.at, 0, 1);

            if (lifeform.attributes.contains(LifeformAttribute.PACIFIST)) {
                state.selfDestruction = new Stat(event.at, 0, 0.5f);
            }
            if (lifeform.attributes.contains(LifeformAttribute.WARLIKE)) {
                state.selfDestruction = new Stat(event.at, 0, 2.0f);
            }
        } else {
            state.selfDestruction = new Stat(selfDestruction);
            state.environmentalDestruction = new Stat(environmentalDestruction);
        }

        state.extinct = extinct;
        state.started = event.at >= lifeform.startAt;

        if (state.started) {
            state.selfDestruction = state.selfDestruction.getValueAt(event.at);
            state.environmentalDestruction = state.environmentalDestruction.getValueAt(event.at);

            if (event.eventType == EventType.GAME_OVER_ENVIRONMENTAL) {
                state.extinct = true;
            }

            if (event.eventType == EventType.GAME_OVER_SELF_DESTRUCTION) {
                state.extinct = true;
            }

            if (event.eventType == EventType.GAME_OVER_GALAXY_ENDS) {
                state.extinct = true;
            }
        }

        return state;
    }

}
