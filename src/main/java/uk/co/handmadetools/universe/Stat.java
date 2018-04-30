package uk.co.handmadetools.universe;

public class Stat {
    public float startAt;
    public float startLevel;
    public float rateOfIncrease;

    public float getAt(float at) {
        return startLevel + (at - startAt) * rateOfIncrease;
    }

    public float reachLevelAt(float level) {
        return startAt + (level - startLevel) / rateOfIncrease;
    }

}
