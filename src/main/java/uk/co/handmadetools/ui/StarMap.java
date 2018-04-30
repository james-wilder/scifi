package uk.co.handmadetools.ui;

import uk.co.handmadetools.event.State;
import uk.co.handmadetools.universe.Galaxy;
import uk.co.handmadetools.universe.Population;
import uk.co.handmadetools.universe.Star;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class StarMap extends JPanel {

    int STAR_SIZE = 256;

    float scale = 30;
    float centerX = 0;
    float centerY = 0;

    private Galaxy galaxy;
    private BufferedImage starImage;
    private BufferedImage glowImage;
    private boolean beingDragged;
    private int dragX = 0;
    private int dragY = 0;
    private State state;

    public StarMap(Galaxy galaxy) {
        this.galaxy = galaxy;

        System.out.println("Generating star images...");
        starImage = makeStarImage(false, true);
        glowImage = makeStarImage(true, false);
        System.out.println("Generated star images");

        addMouseListener(new StarMapMouseListener(this));
        addMouseMotionListener(new StarMapMouseMotionListener(this));
        addMouseWheelListener(new StarMapWheelListener(this));
    }

    private BufferedImage makeStarImage(boolean glow, boolean star) {
        BufferedImage image = new BufferedImage(STAR_SIZE, STAR_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        float mid = (STAR_SIZE - 1.0f) / 2.0f;

        for (int x = 0; x < STAR_SIZE / 2; x++) {
            float xd = x - mid;
            for (int y = 0; y < STAR_SIZE / 2; y++) {
                float yd = y - mid;

                float d2 = xd * xd + yd * yd;
                float d = (float)Math.sqrt(d2);

                int opacity = 0;
                int colorChange = 0;
                if ((d < STAR_SIZE / 20) && star) {
                    opacity = 255;
                    colorChange = 127;
                } else if (glow) {
                    float fade = 0.01f * ((STAR_SIZE / 2.0f) / (1.0f + d - STAR_SIZE / 20.0f) - 0.4f);
                    if (fade < 0) {
                        fade = 0;
//                        System.out.println("A");
                    }
                    if (fade > 1) {
                        fade = 1;
//                        System.out.println("...");
                    }
//                    System.out.println(fade);
//                    fade = 1 - fade;
                    opacity = (int) (fade * 255);
                    colorChange = (int) (100.0f * fade * fade * fade * fade);
                }
                g.setColor(new Color(200, 100 + colorChange, colorChange, opacity));

                g.drawRect(x, y, 1, 1);
                g.drawRect(STAR_SIZE - 1 - x, y, 1, 1);
                g.drawRect(x, STAR_SIZE - 1 - y, 1, 1);
                g.drawRect(STAR_SIZE - 1 - x, STAR_SIZE - 1 - y, 1, 1);
            }
        }

        return image;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

//        System.out.println("paintComponent");

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        drawStars(g, glowImage);
        drawStars(g, starImage);
    }

    private void drawStars(Graphics g, BufferedImage image) {
        int size = (int) (0.4f * (float)STAR_SIZE / Math.log(1.2f + scale));
        int halfSize = size / 2;

        Image scaled = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);

        int midX = getWidth() / 2;
        int midY= getHeight() / 2;
        for (Star star : galaxy.stars) {
            float x = star.x;
            float y = star.y;

            x = x - centerX;
            y = y - centerY;

            x = x / scale;
            y = y / scale;

            g.drawImage(scaled, (int)x - halfSize + midX, (int)y -halfSize + midY, size, size, this);
        }

        if (state == null) {
            return;
        }

        for (Population population : state.populations) {
            Star star = population.location;

            float x = star.x;
            float y = star.y;

            x = x - centerX;
            y = y - centerY;

            x = x / scale;
            y = y / scale;

            g.setColor(population.lifeform.color);

            int radius = (int) (0.05f * (float)STAR_SIZE / Math.log(1.0f + scale));
            g.drawOval((int)x - radius + midX, (int)y - radius + midY, radius * 2, radius * 2);

            if (population.canColonize(state.at)) {
                g.drawOval((int) x - radius + midX - 1, (int) y - radius + midY - 1, radius * 2 + 2, radius * 2 + 2);
            }
        }
    }

    public void dragged(int x, int y) {
        if (!beingDragged) {
            beingDragged = true;
            dragX = x;
            dragY = y;
        } else {
            int dx = x - dragX;
            int dy = y - dragY;

            centerX -= dx * scale;
            centerY -= dy * scale;

            dragX = x;
            dragY = y;

            doRedraw();
        }
    }

    private void doRedraw() {
//        this.invalidate();
//        this.repaint();
        this.paintImmediately(0, 0, getWidth(), getHeight());
    }

    public void zoom(int wheelRotation, int x, int y) {
        int dx = x - getWidth() / 2;
        int dy = y - getHeight() / 2;

        float zoom = (float) Math.pow(1.2f, wheelRotation);

        if (scale * zoom > 50) {
            return;
        }

        if (scale * zoom < 0.25) {
            return;
        }

        centerX -= dx * scale * (zoom - 1);
        centerY -= dy * scale * (zoom - 1);

        scale = scale * zoom;

        doRedraw();
    }

    public void click(int x, int y) {
        dragX = x;
        dragY = y;
    }

    public void refresh(State state) {
        this.state = state;
        doRedraw();
    }
}
