package oostap2.game.flappy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import oostap2.game.flappy.screens.Menu;

public class Main extends Game {

    private static final float MIN_VIEW_WIDTH = 600;
    private static final float MAX_VIEW_WIDTH = 600;
    private static final float MIN_VIEW_HEIGHT = 800;
    private static final float MAX_VIEW_HEIGHT = 1400;

    private SpriteBatch batch;
    private Resources resources;
    private ExtendViewport viewport;
    private OrthographicCamera camera;


    @Override
    public void create() {
        batch = new SpriteBatch();

        viewport = new ExtendViewport(MIN_VIEW_WIDTH, MIN_VIEW_HEIGHT, MAX_VIEW_WIDTH, MAX_VIEW_HEIGHT);
        camera = (OrthographicCamera) viewport.getCamera();

        resources = new Resources();
        resources.load();

        setScreen(new Menu(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        resources.dispose();
    }


    public SpriteBatch batch() {
        return batch;
    }

    public Resources resources() {
        return resources;
    }

    public ExtendViewport viewport() {
        return viewport;
    }

    public OrthographicCamera camera() {
        return camera;
    }
}
