package oostap2.game.flappy.screens;

import oostap2.game.flappy.Main;

class Settings extends BaseScreen {

    private final Menu menu;

    private Languages languages;
    private About about;


    Settings(Main main, Menu menu) {
        super(main);
        this.menu = menu;
    }

    @Override
    protected void init() {
        languages = new Languages(getMain(), this);
        about = new About(getMain(), this);

        ui.addLabel("settings");

        ui.addButton(0, "back", () -> setScreen(menu));
        ui.addButton(1, "language", () -> setScreen(languages));
        ui.addButton(2, "about", () -> setScreen(about));
    }
}
