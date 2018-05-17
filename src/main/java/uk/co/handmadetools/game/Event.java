package uk.co.handmadetools.game;

import java.util.ArrayList;
import java.util.List;

public class Event {

    public String text;
    public EventType eventType;
    public float at;
    private List<Option> options = new ArrayList<>();

    public List<Option> getOptions() {
        return options;
    }

    public void addOption(Option option) {
        options.add(option);
    }
}
