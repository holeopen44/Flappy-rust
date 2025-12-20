package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FontManager {

    public static BitmapFont buttonFont;
    public static BitmapFont gameFont;

    public static void InitFonts() {
        Texture texture = new Texture(Gdx.files.internal("font/font.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        gameFont = new BitmapFont(Gdx.files.internal("font/font.fnt"), new com.badlogic.gdx.graphics.g2d.TextureRegion(texture), false);

        buttonFont = new BitmapFont(Gdx.files.internal("font/font.fnt"), new com.badlogic.gdx.graphics.g2d.TextureRegion(texture), false);

        gameFont.getData().setScale(0.8f);
        buttonFont.getData().setScale(1.2f);
    }

    public static void dispose() {
        buttonFont.dispose();
        gameFont.dispose();
    }
}
