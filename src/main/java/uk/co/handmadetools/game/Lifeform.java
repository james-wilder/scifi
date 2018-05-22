package uk.co.handmadetools.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Lifeform {

    public String name;
    public List<LifeformAttribute> attributes = new ArrayList<>();
    public Location origin;
    public Map<Lifeform, Diplomacy> diplomacyMap = new HashMap<>();
    public float startAt;

    public String toString() {
        return name + "(" + attributes.stream().map(Enum::toString).collect(Collectors.joining(", ")) + ")";
    }

    public boolean hasDiscovered(Lifeform lifeform) {
        return diplomacyMap.containsKey(lifeform);
    }

}
