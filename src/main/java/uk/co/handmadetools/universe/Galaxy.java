package uk.co.handmadetools.universe;

import uk.co.handmadetools.names.NameGenerator;
import uk.co.handmadetools.util.Pair;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Galaxy {

    private Random random = new Random();

    public static final int STAR_COUNT = 2500;
    public static final int ARM_COUNT = 2;
    public static final float GALAXY_RADIUS = 10000;
    public static final float MIN_DISTANCE = 50.0f;
    public static final float MIN_DISTANCE_CHANGE = 0.0f;
    public static final float MAX_DISTANCE = 50000.0f;

    private NameGenerator nameGenerator;

    public List<Star> stars = new ArrayList<>();
    public Map<Star, Float> lifeformEvolvesAt = new HashMap<>();
    public Map<Pair<Star, Float>, String> lifeformNames = new HashMap<>();
    public Map<String, Color> lifeformColors = new HashMap<>();

    public Galaxy(NameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;

        float d = 0;
        while (stars.size() < STAR_COUNT) {
            int arm = random.nextInt(ARM_COUNT + ARM_COUNT);

            d += (0.5f + 0.5f * d) * random.nextFloat() / 1000;
            if (d > 1) {
                d = random.nextFloat() / 1000;
            }

            float a = ((float)arm / (float)ARM_COUNT) * 2.0f * (float)Math.PI + d * 10.0f;

            float x = (float)Math.sin(a) * d * GALAXY_RADIUS;
            float y = (float)Math.cos(a) * d * GALAXY_RADIUS;

            x += (float)random.nextGaussian() * (1 - d) * GALAXY_RADIUS / 10.0f;
            y += (float)random.nextGaussian() * (1 - d) * GALAXY_RADIUS / 10.0f;

            x += (float)random.nextGaussian() * d * GALAXY_RADIUS / 20.0f;
            y += (float)random.nextGaussian() * d * GALAXY_RADIUS / 20.0f;

            x += (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * GALAXY_RADIUS / 5000.0f;
            y += (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * (float)random.nextGaussian() * GALAXY_RADIUS / 5000.0f;

            if (x < -GALAXY_RADIUS * 1.2f) {
                continue;
            }
            if (x > GALAXY_RADIUS * 1.2f) {
                continue;
            }
            if (y < -GALAXY_RADIUS * 1.2f) {
                continue;
            }
            if (y > GALAXY_RADIUS * 1.2f) {
                continue;
            }

            float minD = Float.MAX_VALUE;
            for (Star star : stars) {
                float dist = getDistance(star.x, star.y, x, y);
                if (dist < minD) {
                    minD = dist;
                }
            }
            if (minD < MIN_DISTANCE + d * MIN_DISTANCE_CHANGE) {
                continue;
            }
            if (stars.size() > 0 && minD > MAX_DISTANCE) {
                continue;
            }

            Star star = new Star();
            star.x = x;
            star.y = y;
            star.name = "star_" + stars.size();

            float lifeWillEvolveAt = 0;
            while (lifeWillEvolveAt <= 0) {
                lifeWillEvolveAt = (float) (random.nextGaussian() * 1000000000.0f + 10000000000.0f);
            }
            lifeformEvolvesAt.put(star, lifeWillEvolveAt);

            stars.add(star);
        }
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float xd = x1 - x2;
        float yd = y1 - y2;
        return (float) Math.sqrt(xd * xd + yd * yd);
    }

    public String getName(Pair<Star, Float> lifeformStart) {
        if (lifeformNames.containsKey(lifeformStart)) {
            return lifeformNames.get(lifeformStart);
        }
        String name = nameGenerator.newName();
        lifeformNames.put(lifeformStart, name);
        return name;
    }

    public Color pickColor(String name) {
        if (lifeformColors.containsKey(name)) {
            return lifeformColors.get(name);
        }
        // TODO: make colors fairly unique
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        while ((r < 100) && (g < 100) && (b < 100)) {
            r = random.nextInt(255);
            g = random.nextInt(255);
            b = random.nextInt(255);
        }
        Color color = new Color(r, g, b);
        lifeformColors.put(name, color);
        return color;
    }

    public float getDistance(Star star, Star star2) {
        float dx = star.x - star2.x;
        float dy = star.y - star2.y;
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
}
