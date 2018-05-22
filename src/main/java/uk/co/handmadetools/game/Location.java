package uk.co.handmadetools.game;

public class Location {

    public float x;
    public float y;
    public float z;

    public float getDist(Location location) {
        float xd = location.x - x;
        float yd = location.y - y;
        float zd = location.z - z;
        return (float) Math.sqrt(xd * xd + yd * yd + zd * zd);
    }

}
