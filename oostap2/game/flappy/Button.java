package oostap2.game.flappy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button {

    private int x, y;
    static final int xSize = 400;
    static final int ySize = 100;
    private String text;
    float clickCount;

    public Button(int x, int y, String text) {
        clickCount = 0;
        this.x = x;
        this.y = y;
        this.text = " " +text;
    }

    public void setText(String text) {
        this.text = " " +text;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void draw(SpriteBatch batch, float delta) {
        batch.draw(TextureManager.black, x -1, y -1, xSize +2, ySize +2);
        if (clickCount <= 0) {
            batch.draw(TextureManager.gray, x, y, xSize, ySize);
        } else {
            batch.draw(TextureManager.dark_gray, x, y, xSize, ySize);
            clickCount -= delta;
        }
        FontManager.buttonFont.draw(batch, text, x, y + ySize * .6f);
    }

    public boolean isTouching(float tx, float ty) {
        if
        (
            tx > x &&
            tx < x + xSize &&
            ty > y &&
            ty < y + ySize
        )
        {
            clickCount = .1f;
            SoundManager.playClick();
            return true;
        }
            return false;
    }

    public void reset() {
        clickCount = 0;
    }
}
