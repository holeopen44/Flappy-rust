package oostap2.game.flappy.screens;

import com.badlogic.gdx.Gdx;
import oostap2.game.flappy.Main;

class About extends BaseScreen {

    private final Settings settings;


    About(Main main, Settings settings) {
        super(main);
        this.settings = settings;
    }

    @Override
    protected void init() {
        ui.addLabel("about_text");

        ui.addButton(0, "back", () -> setScreen(settings));
        ui.addButton(1, "GitHub", () -> Gdx.net.openURI("https://github.com/oostap2/Flappy"));
    }
}
