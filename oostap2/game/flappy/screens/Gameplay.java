package oostap2.game.flappy.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import oostap2.game.flappy.Main;
import oostap2.game.flappy.game.Game;

class Gameplay extends BaseScreen {

    private final Menu menu;

    private Game game;
    private boolean hasToReset;


    Gameplay(Main main, Menu menu) {
        super(main);
        this.menu = menu;
    }

    @Override
    protected void init() {
        game = new Game(resources());
    }

    @Override
    public void show() {
        super.show();
        hasToReset = true;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        game.resize(width(), height());

        if (!hasToReset) return;

        game.reset();
        hasToReset = false;
    }

    @Override
    public void pause() {
        game.pause();
    }

    @Override
    public void resume() {
        game.resume();
    }

    @Override
    protected void move(float delta) {
        game.move(delta);

        if (game.isRunning()) return;

        setScreen(menu);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        game.draw(batch);
    }

    @Override
    protected void touch(float x, float y) {
        game.touch(x);
    }
}
