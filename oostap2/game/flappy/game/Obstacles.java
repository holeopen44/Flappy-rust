package oostap2.game.flappy.game;

import java.util.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class Obstacles {

    private static final float SIZE = 60;
    private static final float RADIUS = SIZE / 2;
    private static final float SPEED = 120;
    private static final float SPACING = 100;
    private static final float SAFE_DISTANCE = 250;
    private static final int NUMBER = 15;
    private static final int SAFE_NUMBER = 4;

    private final Texture texture;
    private final Player player;
    private final Random rng;
    private final Array<Obstacle> obstacles;

    private Obstacle lowest;
    private float worldWidth;
    private float worldHeight;
    private int safeCount;


    Obstacles(Texture texture, Player player, Random rng) {
        this.texture = texture;
        this.player = player;
        this.rng = rng;
        obstacles = new Array<>();

        for (int i = 0; i < NUMBER; i++) {
            obstacles.add(new Obstacle());
        }
    }

    void reset() {
        Obstacle first = obstacles.get(0);
        first.reset();
        lowest = first;

        for (int i = 1; i < obstacles.size; i++) {
            obstacles.get(i).wrap();
        }

        safeCount = SAFE_NUMBER;
    }

    void draw(SpriteBatch batch) {
        batch.setColor(1, 0.5f, 0.5f, 1);

        for (int i = 0; i < obstacles.size; i++) {
            Obstacle obstacle = obstacles.get(i);
            batch.draw(texture, obstacle.x - RADIUS, obstacle.y - RADIUS, SIZE, SIZE);
        }
    }

    void move(float delta) {
        for (int i = 0; i < obstacles.size; i++) {
            obstacles.get(i).move(delta);
        }

        for (int i = 0; i < obstacles.size; i++) {
            Obstacle obstacle = obstacles.get(i);

            if (obstacle.checkWrap()) {
                obstacle.wrap();
            }

            if (obstacle.checkPlayerCollision()) {
                player.kill();
            }
        }
    }

    void resize(float width, float height) {
        worldWidth = width;
        worldHeight = height;
    }


    private class Obstacle {

        private float x;
        private float y;


        void move(float delta) {
            y += delta * SPEED;
        }

        void reset() {
            x = rng.nextInt((int) worldWidth);
            y = -SIZE - player.size();
        }

        void wrap() {
            if (safeCount <= 0) {
                safeCount = SAFE_NUMBER;
                safeWrap();
            } else {
                randomWrap();

                if (Math.abs(x - lowest.x) < SAFE_DISTANCE) {
                    safeCount--;
                } else {
                    safeCount = SAFE_NUMBER;
                }
            }

            lowest = this;
        }

        private void randomWrap() {
            y = lowest.y - SPACING;
            x = rng.nextInt((int) worldWidth);
        }

        private void safeWrap() {
            y = lowest.y - SPACING;

            if (lowest.x < worldWidth / 2) {
                x = lowest.x + SAFE_DISTANCE;
                return;
            }
            x = lowest.x - SAFE_DISTANCE;
        }

        boolean checkWrap() {
            return y > worldHeight + SIZE;
        }

        boolean checkPlayerCollision() {
            float dx = player.x() - x;
            float dy = player.y() - y;
            float minDist = player.radius() + RADIUS;

            return dx * dx + dy * dy < minDist * minDist;
        }
    }
}
