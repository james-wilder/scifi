package uk.co.handmadetools.universe;

public class Population {

    public Lifeform lifeform;
    public Star location;
    public Stat size;
    public float started;

    public boolean canColonize(float at) {
        return size.getAt(at) >= 10.0f;
    }
}
