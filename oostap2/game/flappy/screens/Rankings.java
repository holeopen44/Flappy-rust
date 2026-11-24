package oostap2.game.flappy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import oostap2.game.flappy.Main;

class Rankings extends BaseScreen {

    private final Menu menu;

    private String text;
    private float worldWidth;
    private float y;


    Rankings(Main main, Menu menu) {
        super(main);
        this.menu = menu;
    }

    @Override
    protected void init() {
        ui.addLabel("rankings");
        ui.addButton(0, "back", () -> setScreen(menu));
    }

    @Override
    public void show() {
        super.show();

        text = "\n\n";
        Preferences saves = resources.saves();

        int highScore = saves.getInteger("high_score", 0);
        int lastGame = saves.getInteger("last_score", 0);

        if (highScore == 0 && lastGame == 0) {
            text += resources.getString("empty");
            return;
        }

        text += resources.getString("high_score") + ": " + highScore + "\n"
            + resources.getString("last_score") + ": " + lastGame;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        resources.font().draw(batch, text, 0, y, worldWidth, Align.center, false);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        float safeTop = Gdx.graphics.getSafeInsetTop() * (float) height / Gdx.graphics.getHeight();

        worldWidth = width();
        y = height() - safeTop;
    }
}
