package oostap2.game.flappy.screens;

import com.badlogic.gdx.Gdx;
import oostap2.game.flappy.Main;

public class Menu extends BaseScreen {

    private Gameplay gameplay;
    private Settings settings;
    private Rankings rankings;


    public Menu(Main main) {
        super(main);
    }

    @Override
    protected void init() {
        settings = new Settings(getMain(), this);
        rankings = new Rankings(getMain(), this);
        gameplay = new Gameplay(getMain(), this);

        String savedLanguage = resources.saves().getString("language");
        resources.setLanguage(savedLanguage);

        ui.addButton(0, "exit", () -> Gdx.app.exit());
        ui.addButton(1, "settings", () -> setScreen(settings));
        ui.addButton(2, "rankings", () -> setScreen(rankings));
        ui.addButton(3, "play", () -> setScreen(gameplay));
    }
}
