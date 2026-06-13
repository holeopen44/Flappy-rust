package oostap2.game.flappy.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

class Label implements InterfaceElement {

    private static final float SIDES_MARGIN = 15;
    private static final float TOP_MARGIN = 10;

    private final BitmapFont font;
    private final GlyphLayout layout;
    private final String textKey;

    private String text;
    private float y;
    private float maxWidth;


    Label(String textKey, BitmapFont font) {
        this.textKey = textKey;
        this.font = font;
        this.layout = new GlyphLayout();
    }

    @Override
    public void draw(SpriteBatch batch) {
        font.draw(batch, layout, SIDES_MARGIN, y);
    }

    @Override
    public void resize(float width, float height) {
        float safeTop = Gdx.graphics.getSafeInsetTop() * height / Gdx.graphics.getHeight();

        y = height - safeTop - TOP_MARGIN;
        maxWidth = width - SIDES_MARGIN * 2;

        rebuildLayout();
    }

    @Override
    public void applyText(String text) {
        this.text = text;
        rebuildLayout();
    }

    @Override
    public String getTextKey() {
        return textKey;
    }

    private void rebuildLayout() {
        layout.setText(font, text, Color.WHITE, maxWidth, Align.center, true);
    }
}
