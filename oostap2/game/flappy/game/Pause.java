package oostap2.game.flappy.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

class Pause {

    private static final float RESUME_TIME = 3;
    private static final float COUNT_SPEED = 2;

    private final BitmapFont font;

    private float time;
    private float worldWidth;
    private float y;
    private boolean isPaused;


    Pause(BitmapFont font) {
        this.font = font;
        time = 0;
        isPaused = false;
    }

    void draw(SpriteBatch batch) {
        if (time <= 0 || isPaused) return;

        String text = "" + (int) Math.ceil(time);
        font.draw(batch, text, 0, y, worldWidth, Align.center, false);
    }

    boolean countdown(float delta) {
        if (isPaused) {
            return true;
        }

        if (time <= 0) {
            return false;
        }

        time -= delta * COUNT_SPEED;

        return true;
    }

    void resize(float width, float height) {
        worldWidth = width;
        y = height / 2;
    }

    void pause() {
        time = RESUME_TIME;
        isPaused = true;
    }

    void resume() {
        isPaused = false;
    }
}
