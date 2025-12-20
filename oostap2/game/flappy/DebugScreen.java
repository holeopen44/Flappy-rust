package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class DebugScreen implements Screen {

    Main game;

    Button resetBtn;
    Button backBtn;
    Button fpsBtn;

    DebugScreen(Main game) {
        this.game = game;

        fpsBtn = new Button(10, 230, "show fps");
        resetBtn = new Button(10, 120, "reset progress");
        backBtn = new Button(10, 10, "back");
    }

    @Override
    public void show() {
        resetBtn.reset();
        backBtn.reset();
        fpsBtn.reset();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        FontManager.gameFont.draw(game.batch, "Debug screen!", 10, game.h -50);

        resetBtn.draw(game.batch, delta);
        backBtn.draw(game.batch, delta);
        fpsBtn.draw(game.batch, delta);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (backBtn.isTouching(tx, ty)) {
                ScreenManager.setMenu(game);
            }

            if (resetBtn.isTouching(tx, ty)) {
                Main.reset(game);
            }

            if (fpsBtn.isTouching(tx, ty)) {
                Main.change_show_fps();
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
