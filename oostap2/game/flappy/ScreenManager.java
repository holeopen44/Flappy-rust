package oostap2.game.flappy;

public class ScreenManager {

    private static MainMenuScreen menuScreen;
    private static GameScreen gameScreen;
    private static SettingsMenuScreen settingsScreen;
    private static AboutMenuScreen aboutScreen;
    private static GameOverScreen overScreen;
    private static DebugScreen debugScreen;
    private static GameModeScreen modeScreen;

    public static void InitScreens(Main game){
        menuScreen = new MainMenuScreen(game);
        gameScreen = new GameScreen(game);
        settingsScreen = new SettingsMenuScreen(game);
        aboutScreen = new AboutMenuScreen(game);
        overScreen = new GameOverScreen(game);
        debugScreen = new DebugScreen(game);
        modeScreen = new GameModeScreen(game);
    }

    public static void setMenu(Main game) {
        game.setScreen(menuScreen);
    }

    public static void setGame(Main game) {
        game.setScreen(gameScreen);
    }

    public static void setSettings(Main game) {
        game.setScreen(settingsScreen);
    }

    public static void setAbout(Main game) {
        game.setScreen(aboutScreen);
    }

    public static void setOver(Main game) {
        game.setScreen(overScreen);
    }

    public static void setDebug(Main game) {
        game.setScreen(debugScreen);
    }

    public static void setMode(Main game) { game.setScreen(modeScreen); }

    public static void resetAllStrings() {
        settingsScreen.changeLangBtn.setText(LanguageManager.getChangeLanguage());
        settingsScreen.aboutBtn.setText(LanguageManager.getAbout());
        settingsScreen.backBtn.setText(LanguageManager.getBack());
        menuScreen.exitBtn.setText(LanguageManager.getExit());
        menuScreen.settingsBtn.setText(LanguageManager.getSettings());
        menuScreen.playBtn.setText(LanguageManager.getPlay());
        aboutScreen.backBtn.setText(LanguageManager.getBack());
        overScreen.goodBtn.setText(LanguageManager.getGood());
        overScreen.againBtn.setText(LanguageManager.getAgain());
        if (SoundManager.enabled) settingsScreen.soundBtn.setText(LanguageManager.getSoundDisable());
        else settingsScreen.soundBtn.setText(LanguageManager.getSoundEnable());
        modeScreen.backBtn.setText(LanguageManager.getBack());
        modeScreen.circleBtn.setText(LanguageManager.getCircle());
        modeScreen.squareBtn.setText(LanguageManager.getSquare());
    }
}
