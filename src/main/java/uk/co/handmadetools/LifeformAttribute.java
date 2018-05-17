package uk.co.handmadetools;

public enum LifeformAttribute {

    PACIFIST ("Pacifist"),
    WARLIKE ("Warlike");

    private String name;

    LifeformAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
