package oostap2.game.flappy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Score {

    private static final float TOP_MARGIN = 10;
    private static final float INCREASE_SPEED = 10;

    private final BitmapFont font;

    private float amount;
    private float worldWidth;
    private float y;


    Score(BitmapFont font) {
        this.font = font;
        reset();
    }

    void reset() {
        amount = 0;
    }

    void move(float delta) {
        amount += delta * INCREASE_SPEED;
    }

    void draw(SpriteBatch batch) {
        String text = "" + (int) amount;
        font.draw(batch, text, 0, y, worldWidth, Align.center, false);
    }

    void resize(float width, float height) {
        float safeTop = Gdx.graphics.getSafeInsetTop() * height / Gdx.graphics.getHeight();

        worldWidth = width;
        y = height - safeTop - TOP_MARGIN;
    }


    int get() {
        return (int) amount;
    }
}
