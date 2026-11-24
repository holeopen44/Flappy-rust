package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Resources {

    private final AssetManager assets;
    private final LanguagesManager languages;
    private final Preferences saves;

    private Texture fontTexture;
    private BitmapFont font;


    Resources() {
        saves = Gdx.app.getPreferences("data");
        languages = new LanguagesManager();
        assets = new AssetManager();
    }

    void load() {
        assets.load("textures/pixel.png", Texture.class);
        assets.load("textures/circle.png", Texture.class);

        assets.load("sounds/click.wav", Sound.class);
        assets.load("sounds/hit.wav", Sound.class);
        assets.load("sounds/explosion.ogg", Sound.class);

        assets.finishLoading();

        loadFont();
    }

    private void loadFont() {
        fontTexture = new Texture(Gdx.files.internal("font/font.png"), true);
        fontTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        font = new BitmapFont(Gdx.files.internal("font/font.fnt"), new TextureRegion(fontTexture), false);
    }

    void dispose() {
        assets.dispose();
        font.dispose();
        fontTexture.dispose();
    }


    public void setLanguage(String lang) {
        languages.setLanguage(lang);
    }

    public String getString(String key) {
        return languages.getString(key);
    }

    public Array<LanguagesManager.LanguageItem> getAvailableLanguages() {
        return languages.getAvailableLanguages();
    }


    public BitmapFont font() {
        return font;
    }

    public Preferences saves() {
        return saves;
    }


    public Texture pixel() {
        return assets.get("textures/pixel.png", Texture.class);
    }

    public Texture circle() {
        return assets.get("textures/circle.png", Texture.class);
    }


    public Sound click() {
        return assets.get("sounds/click.wav", Sound.class);
    }

    public Sound hit() {
        return assets.get("sounds/hit.wav", Sound.class);
    }

    public Sound explosion() {
        return assets.get("sounds/explosion.ogg", Sound.class);
    }
}
