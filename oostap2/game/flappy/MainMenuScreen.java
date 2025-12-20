package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class MainMenuScreen implements Screen {

    final Main game;
    Button playBtn;
    Button settingsBtn;
    Button exitBtn;

    public MainMenuScreen(final Main game) {
        this.game = game;
        playBtn = new Button(game.w -10 -Button.xSize, 230, LanguageManager.getPlay());
        settingsBtn = new Button(game.w -10 -Button.xSize, 120, LanguageManager.getSettings());
        exitBtn = new Button(game.w -10 -Button.xSize, 10, LanguageManager.getExit());
    }

    @Override
    public void show() {
        playBtn.reset();
        settingsBtn.reset();
        exitBtn.reset();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        playBtn.draw(game.batch, delta);
        exitBtn.draw(game.batch, delta);
        settingsBtn.draw(game.batch, delta);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (playBtn.isTouching(tx, ty)) {
                ScreenManager.setMode(game);
            }

            if (settingsBtn.isTouching(tx, ty)) {
                ScreenManager.setSettings(game);
            }

            if (exitBtn.isTouching(tx, ty)) {
                game.startExiting();
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        int ButtonX = width -10 -Button.xSize;
        playBtn.setX(ButtonX);
        settingsBtn.setX(ButtonX);
        exitBtn.setX(ButtonX);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}
}

