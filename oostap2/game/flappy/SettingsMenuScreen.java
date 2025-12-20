package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class SettingsMenuScreen implements Screen {
    final Main game;

    Button aboutBtn;
    Button changeLangBtn;
    Button backBtn;
    Button soundBtn;
    int secretCount;

    public SettingsMenuScreen(final Main game) {
        this.game = game;
        aboutBtn = new Button(10, 340, LanguageManager.getAbout());
        soundBtn = new Button(10, 230, "");
        changeLangBtn = new Button(10, 120, LanguageManager.getChangeLanguage());
        backBtn = new Button(10, 10, LanguageManager.getBack());
        if (SoundManager.enabled) soundBtn.setText(LanguageManager.getSoundDisable());
        else soundBtn.setText(LanguageManager.getSoundEnable());
    }

    @Override
    public void show() {
        secretCount = 0;
        aboutBtn.reset();
        backBtn.reset();
        changeLangBtn.reset();
        soundBtn.reset();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        aboutBtn.draw(game.batch, delta);
        changeLangBtn.draw(game.batch, delta);
        backBtn.draw(game.batch, delta);
        soundBtn.draw(game.batch, delta);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (tx > game.w -100 && ty > game.h -100) secretCount += 120;

            if (secretCount > 300) {
                ScreenManager.setDebug(game);
            }

            if (backBtn.isTouching(tx, ty)) {
                ScreenManager.setMenu(game);
            }

            if (aboutBtn.isTouching(tx, ty)) {
                ScreenManager.setAbout(game);
            }

            if (changeLangBtn.isTouching(tx, ty)) {
                changeLang();
            }

            if (soundBtn.isTouching(tx, ty)) {
                changeSound();
            }
        }
        if (secretCount > 0) {
            secretCount -= 1;
        }
    }

    private void changeLang() {
        LanguageManager.changeLanguage();
        ScreenManager.resetAllStrings();
    }

    private void changeSound() {
        if (SoundManager.enabled) {
            SoundManager.turn_off();
            soundBtn.setText(LanguageManager.getSoundEnable());
        }
        else {
            SoundManager.turn_on();
            soundBtn.setText(LanguageManager.getSoundDisable());
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}
}
