package uk.co.handmadetools.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lifeform {

    public String name;
    public List<LifeformAttribute> attributes = new ArrayList<>();
    public Location origin;

    public String toString() {
        return name + "(" + attributes.stream().map(Enum::toString).collect(Collectors.joining(", ")) + ")";
    }

}
