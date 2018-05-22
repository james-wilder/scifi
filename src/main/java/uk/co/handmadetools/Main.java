package uk.co.handmadetools;

import uk.co.handmadetools.game.Event;
import uk.co.handmadetools.game.EventGenerator;
import uk.co.handmadetools.game.Game;
import uk.co.handmadetools.game.Option;
import uk.co.handmadetools.game.Stat;
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
        state.selfDestruction = new Stat(0, 0, 1);
        state.environmentalDestruction = new Stat(0, 0, 1);

        while (!state.gameOver) {
            List<Event> events = eventGenerator.nextEvents(game, state);
            for (Event event : events) {
                System.out.println("Event: " + event.eventType.name() + ", " + event.text);

                List<Option> options = event.getOptions();

                showStatus(state, event);
                for (int i = 0; i < options.size(); i++) {
                    System.out.println(OPTION_KEYS.substring(i, i + 1) + " - " + options.get(i).text);
                }

                String option = chooseOption(in).toLowerCase();
                int chosenIndex = OPTION_KEYS.indexOf(option);
                while (chosenIndex < 0 || chosenIndex >= options.size()) {
                    option = chooseOption(in).toLowerCase();
                    chosenIndex = OPTION_KEYS.indexOf(option);
                }

//                System.out.println(chosenIndex);
//                System.out.println(option);

                Option chosenOption = options.get(chosenIndex);

                state = state.update(event, chosenOption);
            }
        }
    }

    private static void showStatus(State state, Event event) {
        System.out.println("Year: " + event.at);
        System.out.println("Creator: " + (state.creator == null ? "none" : state.creator.name));
        System.out.println("Environmental damage: " + state.environmentalDestruction.getValueAt(event.at).value);
        System.out.println("Global destruction danger: " + state.selfDestruction.getValueAt(event.at).value);
    }

    private static String chooseOption(BufferedReader in) throws IOException {
        return in.readLine();
    }

}
