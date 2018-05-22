package uk.co.handmadetools;

import uk.co.handmadetools.game.Event;
import uk.co.handmadetools.game.EventGenerator;
import uk.co.handmadetools.game.Game;
import uk.co.handmadetools.game.Lifeform;
import uk.co.handmadetools.game.LifeformState;
import uk.co.handmadetools.game.Option;
import uk.co.handmadetools.game.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static final String OPTION_KEYS = "1234567890abcdefghijklmnopqrstuvwxyz";

    public static void main (String args[]) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        Game game = new Game();

        EventGenerator eventGenerator = new EventGenerator();

        State state = new State();
        for (Lifeform lifeform : game.getLifeforms()) {
            LifeformState lifeformState = new LifeformState();
            lifeformState.extinct = false;
            lifeformState.started = false;
            state.lifeformStateMap.put(lifeform, lifeformState);
        }

        while (!state.gameOver) {
            List<Event> events = eventGenerator.nextEvents(game, state);
            for (Event event : events) {
                System.out.println("Event (year " + event.at + "): " + event.eventType.name() + ", " + event.text);

                List<Option> options = event.getOptions();

                showStatus(state, event);
                for (int i = 0; i < options.size(); i++) {
                    System.out.println(OPTION_KEYS.substring(i, i + 1) + " - " + options.get(i).text);
                }

                if (!options.isEmpty()) {
                    String option = chooseOption(in).toLowerCase();
                    int chosenIndex = OPTION_KEYS.indexOf(option);
                    while (chosenIndex < 0 || chosenIndex >= options.size()) {
                        option = chooseOption(in).toLowerCase();
                        chosenIndex = OPTION_KEYS.indexOf(option);
                    }

                    Option chosenOption = options.get(chosenIndex);

                    state = state.update(event, chosenOption);
                } else {
                    state = state.update(event, null);
                }
            }
        }
    }

    private static void showStatus(State state, Event event) {
        if (state.creator == null) {
            System.out.println("Creator not chosen");
        } else if (event.lifeform == state.creator) {
            LifeformState lifeformState = state.lifeformStateMap.get(state.creator);

            System.out.println("Year: " + event.at);
            System.out.println("Creator: " + state.creator.name);
            System.out.println("Environmental damage: " + lifeformState.environmentalDestruction.getValueAt(event.at).value);
            System.out.println("Global destruction danger: " + lifeformState.selfDestruction.getValueAt(event.at).value);
        }

        if (event.lifeform != null) {
//            System.out.println(event.lifeform.name + " ... " + event.eventType.name());
        }
    }

    private static String chooseOption(BufferedReader in) throws IOException {
        return in.readLine();
    }

}
