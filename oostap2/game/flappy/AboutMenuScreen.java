package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class AboutMenuScreen implements Screen {

    final Main game;

    Button backBtn;
    Button githubBtn;

    public AboutMenuScreen(final Main game) {
        this.game = game;
        backBtn = new Button(10, 10, LanguageManager.getBack());
        githubBtn = new Button(10, 120, "GitHub");
    }

    @Override
    public void show() {
        backBtn.reset();
        githubBtn.reset();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        backBtn.draw(game.batch, delta);
        githubBtn.draw(game.batch, delta);
        FontManager.gameFont.draw(game.batch, LanguageManager.getAboutGame(), 10, game.h -50);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (backBtn.isTouching(tx, ty)) {
                ScreenManager.setSettings(game);
            }

            if (githubBtn.isTouching(tx, ty)) {
                Gdx.net.openURI("https://github.com/oostap2/Flappy");
            }
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {}
}

