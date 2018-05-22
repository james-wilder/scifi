package uk.co.handmadetools.game;

import uk.co.handmadetools.names.NameGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    public float GALAXY_SIZE = 1000;
    public float MIN_DIST = 10;

    private Random random = new Random();

    private List<Lifeform> lifeforms = new ArrayList<>();
    private NameGenerator nameGenerator = new NameGenerator();

    public Game() {
        createLifeforms();
    }

    private void createLifeforms() {
        for (int i = 0; i < 10; i++) {
            Lifeform lifeform = new Lifeform();
            lifeform.startAt = random.nextFloat() * 100;
            lifeform.name = nameGenerator.newName();
            if (random.nextFloat() < 0.1) {
                lifeform.attributes.add(LifeformAttribute.PACIFIST);
            } else if (random.nextFloat() < 0.1) {
                lifeform.attributes.add(LifeformAttribute.WARLIKE);
            }
            Location location = new Location();
            float minD = 0;
            while (minD < MIN_DIST) {
                location.x = GALAXY_SIZE * random.nextFloat();
                location.y = GALAXY_SIZE * random.nextFloat();
                location.z = GALAXY_SIZE * random.nextFloat();

                minD = minLifeformDist(location);
            }
            lifeform.origin = location;
            lifeforms.add(lifeform);
        }

        lifeforms.sort((o1, o2) -> Float.compare(o1.startAt, o2.startAt));
    }

    private float minLifeformDist(Location location) {
        float minD = Float.MAX_VALUE;
        for (Lifeform lifeform : lifeforms) {
            float d = lifeform.origin.getDist(location);
            if (d < minD) {
                minD = d;
            }
        }
        return minD;
    }

    public List<Lifeform> getLifeforms() {
        return lifeforms;
    }

}
