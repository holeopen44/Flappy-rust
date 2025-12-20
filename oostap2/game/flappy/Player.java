package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    public static final float diam = 50, radius = diam *.5f;
    private final float drawSize, halfSize;

    public boolean is_alive;
    private float x, y;
    private float v_x, v_y;

    private static final float gravity = 900;
    private static final float jump_acceleration = 400;
    private static final float horizontal_acceleration = 500;
    private static final float friction = 750;

    Player() {
        float realDiam = 90;
        float texSize = 128;
        float scale = diam / realDiam;
        drawSize = texSize * scale;
        halfSize = drawSize *.5f;
    }

    public void draw(SpriteBatch batch) {
        if (is_alive) {
            if (Main.gameMode == GameMode.circle) {
                batch.draw(TextureManager.c_white, x - halfSize, y - halfSize, drawSize, drawSize);
                return;
            }
            batch.draw(TextureManager.white, x - radius, y - radius, diam, diam);
            return;
        }
        if (Main.gameMode == GameMode.circle) {
            batch.draw(TextureManager.c_red, x - halfSize, y - halfSize, drawSize, drawSize);
            return;
        }
        batch.draw(TextureManager.red, x - radius, y - radius, diam, diam);
    }

    public void reset(Main game) {
        x = game.halfW;
        y = -radius;
        v_x = 0;
        v_y = jump_acceleration *2;
        is_alive = true;
    }

    public void update(float delta) {
        x += v_x * delta;
        y += v_y * delta;

        v_y -= gravity * delta;
        if (v_x > 0) {
            v_x -= friction * delta;
            if (v_x < 0) v_x = 0;
        } else if (v_x < 0) {
            v_x += friction * delta;
            if (v_x > 0) v_x = 0;
        }

        if (x < radius) {
            x = radius;
            v_x = Math.abs(v_x);
            SoundManager.playHit();
        }
        if (x + radius > Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() -radius;
            v_x = -Math.abs(v_x);
            SoundManager.playHit();
        }

        if (y - radius < 0 && v_y <= 0) {
            kill();
            y = radius;
        }
    }

    public void goLeft() {
        v_x = -horizontal_acceleration;
        goUp();
    }

    public void goRight() {
        v_x = horizontal_acceleration;
        goUp();
    }

    private void goUp() {
        if (y > Gdx.graphics.getHeight() - diam) return;
        v_y = jump_acceleration;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void kill() {
        is_alive = false;
        SoundManager.playExplosion();
    }
}
