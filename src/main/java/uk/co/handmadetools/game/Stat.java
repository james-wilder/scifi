package uk.co.handmadetools.game;

public class Stat {

    public float value;
    public float at;
    public float changeRate;

    public Stat(float at, float value, float changeRate) {
        this.at = at;
        this.value = value;
        this.changeRate = changeRate;
    }

    public Stat(Stat other) {
        if (other == null) {
            return;
        }
        this.value = other.value;
        this.at = other.at;
        this.changeRate = other.changeRate;
    }

    public Stat getValueAt(float t) {
        Stat stat = new Stat(t, value + (t - at) * changeRate, changeRate);
        return stat;
    }

    public float willReachLevelAt(float level) {
        if (level <= value) {
            return 0;
        }
        return at + (level - value) / changeRate;
    }

}
