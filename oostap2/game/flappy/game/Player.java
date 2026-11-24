package oostap2.game.flappy.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private static final float SIZE = 50;
    private static final float RADIUS = SIZE / 2;
    private static final float GRAVITY = 900;
    private static final float FRICTION = 750;
    private static final float JUMP_UP = 400;
    private static final float JUMP_SIDE = 500;
    private static final float JUMP_START = 900;

    private final Texture texture;
    private final Sound hit;
    private final Sound explosion;

    private float x;
    private float y;
    private float vx;
    private float vy;
    private float worldWidth;
    private float worldHeight;
    private boolean alive;


    Player(Texture texture, Sound hit, Sound explosion) {
        this.texture = texture;
        this.hit = hit;
        this.explosion = explosion;
    }

    void reset() {
        x = worldWidth / 2;
        y = -RADIUS;
        vx = 0;
        vy = JUMP_START;
        alive = true;
    }

    public void resize(float width, float height) {
        worldWidth = width;
        worldHeight = height;
    }

    void draw(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 1);
        batch.draw(texture, x - RADIUS, y - RADIUS, SIZE, SIZE);
    }

    void move(float delta) {
        x += vx * delta;
        y += vy * delta;

        vy -= GRAVITY * delta;

        if (vx > 0) {
            vx -= FRICTION * delta;
            if (vx < 0) vx = 0;
        }

        if (vx < 0) {
            vx += FRICTION * delta;
            if (vx > 0) vx = 0;
        }

        reflect();

        if (y > RADIUS || vy > 0) return;
        kill();
    }

    void jumpRight() {
        jumpUp();
        vx = JUMP_SIDE;
    }

    void jumpLeft() {
        jumpUp();
        vx = -JUMP_SIDE;
    }

    private void jumpUp() {
        if (y + RADIUS > worldHeight) return;

        vy = JUMP_UP;
    }

    void kill() {
        if (!alive) return;

        alive = false;
        explosion.play();
    }

    private void reflect() {
        if (x < RADIUS) {
            x = RADIUS;
            vx = Math.abs(vx);
            hit.play();
        }

        if (x + RADIUS > worldWidth) {
            x = worldWidth - RADIUS;
            vx = -Math.abs(vx);
            hit.play();
        }
    }


    float x() {
        return x;
    }

    float y() {
        return y;
    }

    float radius() {
        return RADIUS;
    }

    float size() {
        return SIZE;
    }

    boolean isAlive() {
        return alive;
    }
}
