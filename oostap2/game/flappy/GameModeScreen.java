package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameModeScreen implements Screen {

    final Main game;
    Button circleBtn;
    Button squareBtn;
    Button backBtn;

    public GameModeScreen(final Main game) {
        this.game = game;
        circleBtn = new Button(game.w -10 -Button.xSize, 230, LanguageManager.getCircle());
        squareBtn = new Button(game.w -10 -Button.xSize, 120, LanguageManager.getSquare());
        backBtn = new Button(game.w -10 -Button.xSize, 10, LanguageManager.getBack());
    }

    @Override
    public void show() {
        circleBtn.reset();
        squareBtn.reset();
        backBtn.reset();
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        FontManager.gameFont.draw(game.batch, LanguageManager.getGameMode(), 10, game.h -50);

        circleBtn.draw(game.batch, delta);
        backBtn.draw(game.batch, delta);
        squareBtn.draw(game.batch, delta);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (circleBtn.isTouching(tx, ty)) {
                Main.gameMode = GameMode.circle;
                ScreenManager.setGame(game);
            }

            if (squareBtn.isTouching(tx, ty)) {
                Main.gameMode = GameMode.square;
                ScreenManager.setGame(game);
            }

            if (backBtn.isTouching(tx, ty)) {
                ScreenManager.setMenu(game);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        int ButtonX = width -10 -Button.xSize;
        circleBtn.setX(ButtonX);
        squareBtn.setX(ButtonX);
        backBtn.setX(ButtonX);
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
