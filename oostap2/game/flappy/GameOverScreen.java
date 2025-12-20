package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class GameOverScreen implements Screen {
    final Main game;

    Button goodBtn;
    Button againBtn;

    String text;

    public GameOverScreen(final Main game) {
        this.game = game;
        againBtn = new Button(game.w -10 -Button.xSize, 120, LanguageManager.getAgain());
        goodBtn = new Button(game.w -10 -Button.xSize, 10, LanguageManager.getGood());
    }


    @Override
    public void show() {
        againBtn.reset();
        goodBtn.reset();
        if (game.score > game.record) {
            game.saveBestScore();
            text = LanguageManager.getNewRecord();
            SoundManager.playCoin();
        } else {
            text = LanguageManager.getScore() + (int) game.score;
        }
    }

    @Override
    public void render(float delta) {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        goodBtn.draw(game.batch, delta);
        againBtn.draw(game.batch, delta);
        FontManager.gameFont.draw(game.batch, text, 10, game.h -50);
        FontManager.gameFont.draw(game.batch, LanguageManager.getHighScore() + game.record, 10, game.h -100);

        game.batch.end();

        if (Gdx.input.justTouched()) {

            float tx = Gdx.input.getX();
            float ty = game.h - Gdx.input.getY();

            if (againBtn.isTouching(tx, ty)) {
                ScreenManager.setGame(game);
            }

            if (goodBtn.isTouching(tx, ty)) {
                ScreenManager.setMenu(game);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        int ButtonX = width -10 -Button.xSize;
        goodBtn.setX(ButtonX);
        againBtn.setX(ButtonX);
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
