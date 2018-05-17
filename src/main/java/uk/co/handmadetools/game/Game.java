package uk.co.handmadetools.game;

import uk.co.handmadetools.Lifeform;
import uk.co.handmadetools.LifeformAttribute;
import uk.co.handmadetools.names.NameGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private Random random = new Random();

    private List<Lifeform> lifeforms = new ArrayList<>();
    private NameGenerator nameGenerator = new NameGenerator();

    public Game() {
        createLifeforms();
    }

    private void createLifeforms() {
        for (int i = 0; i < 10; i++) {
            Lifeform lifeform = new Lifeform();
            lifeform.name = nameGenerator.newName();
            if (random.nextFloat() < 0.1) {
                lifeform.attributes.add(LifeformAttribute.PACIFIST);
            } else if (random.nextFloat() < 0.1) {
                lifeform.attributes.add(LifeformAttribute.WARLIKE);
            }
            lifeforms.add(lifeform);
        }
    }

    public List<Lifeform> getLifeforms() {
        return lifeforms;
    }

}
