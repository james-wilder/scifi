package uk.co.handmadetools.universe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Galaxy {

    private Random random = new Random();

    public static final int STAR_COUNT = 2500;
    public static final int ARM_COUNT = 2;
    public static final float GALAXY_RADIUS = 10000;
    public static final float MIN_DISTANCE = 50.0f;
    public static final float MIN_DISTANCE_CHANGE = 0.0f;
    public static final float MAX_DISTANCE = 50000.0f;

    public List<Star> stars = new ArrayList<>();

    public Galaxy() {
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
            star.lifeWillEvolveAt = 0;
            while (star.lifeWillEvolveAt <= 0) {
                star.lifeWillEvolveAt = (float) (random.nextGaussian() * 1000000000.0f + 10000000000.0f);
            }

            stars.add(star);
        }
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        float xd = x1 - x2;
        float yd = y1 - y2;
        return (float) Math.sqrt(xd * xd + yd * yd);
    }

}
