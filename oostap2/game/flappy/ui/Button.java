package oostap2.game.flappy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

class Button implements InterfaceElement {

    private static final float BOTTOM_MARGIN = 80;
    private static final float SIDES_MARGIN = 20;
    private static final float SPACING = 20;
    private static final float OUTLINE_SIZE = 2;
    private static final float HEIGHT = 60;
    private static final float TEXT_PADDING = HEIGHT / 3;

    private final Texture texture;
    private final BitmapFont font;
    private final Sound sound;
    private final Runnable action;
    private final String textKey;
    private final int index;

    private String text;
    private float x;
    private float y;
    private float width;
    private float height;


    Button(int index, String textKey, Texture texture, BitmapFont font, Sound sound, Runnable action) {
        this.index = index;
        this.textKey = textKey;
        this.texture = texture;
        this.font = font;
        this.sound = sound;
        this.action = action;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.setColor(0, 0, 0, 1);
        batch.draw(texture, x, y, width, height);

        batch.setColor(0.5f,0.5f,0.5f,1);
        batch.draw(texture ,x + OUTLINE_SIZE, y + OUTLINE_SIZE, width - OUTLINE_SIZE * 2, height - OUTLINE_SIZE * 2);

        font.draw(batch, text, x + TEXT_PADDING, y + height - TEXT_PADDING);
    }

    @Override
    public void resize(float width, float height) {
        float safeBottom = Gdx.graphics.getSafeInsetBottom() * height / Gdx.graphics.getHeight();

        x = SIDES_MARGIN;
        y = safeBottom + BOTTOM_MARGIN + SPACING + (HEIGHT + SPACING) * index;
        this.width = width - SIDES_MARGIN * 2;
        this.height = HEIGHT;
    }

    @Override
    public void applyText(String text) {
        this.text = text;
    }

    @Override
    public String getTextKey() {
        return textKey;
    }

    void touch(float touchX, float touchY) {
        if (isTouched(touchX, touchY)) {
            click();
        }
    }

    void click() {
        sound.play();
        action.run();
    }

    private boolean isTouched(float touchX, float touchY) {
        return touchX > x && touchX < x + width
            && touchY > y && touchY < y + height;
    }


    int index() {
        return index;
    }
}
