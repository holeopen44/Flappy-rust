package oostap2.game.flappy.screens;

import com.badlogic.gdx.utils.Array;
import oostap2.game.flappy.LanguagesManager;
import oostap2.game.flappy.Main;

class Languages extends BaseScreen {

    private final Settings settings;


    Languages(Main main, Settings settings) {
        super(main);
        this.settings = settings;
    }

    @Override
    protected void init() {
        Array<LanguagesManager.LanguageItem> languageList = resources.getAvailableLanguages();

        for (int i = 0; i < languageList.size; i++) {
            LanguagesManager.LanguageItem languageItem = languageList.get(i);

            ui.addButton(i + 1, languageItem.fullName(), () -> {
                resources.saves().putString("language", languageItem.name()).flush();

                resources.setLanguage(languageItem.name());
                setScreen(settings);
            });
        }

        ui.addButton(0, "back", () -> setScreen(settings));
        ui.addLabel("languages");
    }
}
