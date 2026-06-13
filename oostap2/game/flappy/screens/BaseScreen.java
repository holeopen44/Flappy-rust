package oostap2.game.flappy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import oostap2.game.flappy.Main;
import oostap2.game.flappy.Resources;
import oostap2.game.flappy.ui.UserInterface;

abstract class BaseScreen implements Screen {

    private final Main main;
    private final Vector2 touchPos;
    private final Appear appear;
    protected final Resources resources;
    protected final UserInterface ui;

    private float width;
    private float height;
    private boolean initialised;


    BaseScreen(Main main) {
        this.main = main;
        resources = main.resources();
        touchPos = new Vector2();
        ui = new UserInterface(resources);
        appear = new Appear();
        initialised = false;
    }


    protected void init() {}

    protected void draw(SpriteBatch batch) {}

    protected void move(float delta) {}

    protected void touch(float x, float y) {}


    @Override
    public void render(float delta) {
        SpriteBatch batch = main.batch();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        main.viewport().apply();
        batch.setProjectionMatrix(main.camera().combined);

        batch.begin();

        drawBackground(batch);
        draw(batch);
        ui.draw(batch);
        appear.draw(batch);

        batch.end();

        ui.handleKeyboard();

        if (Gdx.input.justTouched()) {
            Vector2 pos = getMousePos();
            handleTouch(pos.x, pos.y);
        }

        appear.move(delta);
        move(delta);
    }

    @Override
    public void resize(int width, int height) {
        ExtendViewport viewport = main.viewport();

        viewport.update(width, height, true);
        this.width = viewport.getWorldWidth();
        this.height = viewport.getWorldHeight();
        ui.resize(this.width, this.height);
    }

    @Override public void show() {
        Gdx.gl.glClearColor(0,0,0, 1);
        initialise();
        ui.applyLocalization();
        appear.start();
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void dispose() {}


    private void drawBackground(SpriteBatch batch) {
        batch.setColor(0.529f,0.808f,0.922f, 1);
        batch.draw(resources.pixel(), 0, 0, width, height);
    }

    private void initialise() {
        if (initialised) return;

        init();
        initialised = true;
    }

    private void handleTouch(float x, float y) {
        if (appear.isActive()) return;

        ui.handleTouch(x, y);
        touch(x, y);
    }

    private Vector2 getMousePos() {
        touchPos.x = Gdx.input.getX();
        touchPos.y = Gdx.input.getY();
        main.viewport().unproject(touchPos);

        return touchPos;
    }

    public void setScreen(Screen screen) {
        main.setScreen(screen);
    }

    public Main getMain() {
        return main;
    }

    public Resources resources() {
        return resources;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }


    private class Appear {

        private static final float SPEED = 4;
        private float alpha;


        void start() {
            alpha = 1.0f;
        }

        void move(float delta) {
            if (alpha > 0) {
                alpha -= delta * SPEED;
                return;
            }
            alpha = 0;
        }

        void draw(SpriteBatch batch) {
            if (!isActive()) return;

            batch.setColor(0, 0, 0, alpha);
            batch.draw(resources.pixel(), 0, 0, width, height);
        }

        boolean isActive() {
            return alpha > 0;
        }
    }
}
