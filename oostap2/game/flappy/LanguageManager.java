package oostap2.game.flappy;

public class LanguageManager {

    private static String play, score, settings, changeLanguage, exit, back, about, aboutGame, good, again, highScore, newRecord, soundEnable, soundDisable, circle, square, gameMode;
    private static Language current;

    private static void setEnglish() {
        current = Language.EN;

        play = "Play";
        score = "Score: ";
        settings = "Settings";
        changeLanguage = "English";
        exit = "Exit";
        back = "Back";
        about = "About";
        aboutGame = "This is my first android game.";
        good = "Good";
        again = "Again";
        highScore = "Record: ";
        newRecord = "New record!";
        soundEnable = "Enable Sound";
        soundDisable = "Disable Sound";
        circle = "Circle";
        square = "Square";
        gameMode = "Game mode";
    }

    private static void setUkrainian() {
        current = Language.UA;

        play = "Грати";
        score = "Рахунок: ";
        settings = "Налаштування";
        changeLanguage = "Українська";
        exit = "Вийти";
        back = "Назад";
        about = "Про гру";
        aboutGame = "Це моя перша гра для android.";
        good = "Добре";
        again = "Заново";
        highScore = "Рекорд: ";
        newRecord = "Новий рекорд!";
        soundEnable = "Увімкнути звук";
        soundDisable = "Вимкнути звук";
        circle = "Куля";
        square = "Квадрат";
        gameMode = "Режим гри";
    }

    private static void setPolish() {
        current = Language.PL;

        play = "Graj";
        score = "Punkty: ";
        settings = "Ustawienia";
        changeLanguage = "Polski";
        exit = "Wyjdź";
        back = "Wróć";
        about = "O grze";
        aboutGame = "To jest moja pierwsza gra na android.";
        good = "Okej";
        again = "Graj ponownie";
        highScore = "Rekord: ";
        newRecord = "Nowy rekord!";
        soundEnable = "Włącz dźwięk";
        soundDisable = "Wyłącz dźwięk";
        circle = "Kula";
        square = "Kwadrat";
        gameMode = "Tryb gry";
    }

    public static void changeLanguage() {
        switch (current) {
            case EN: setUkrainian(); break;
            case UA: setPolish(); break;
            case PL: setEnglish(); break;
        }
    }

    public static void InitLanguages() {
        String systemLanguage = java.util.Locale.getDefault().getLanguage();
        switch (systemLanguage) {
            case "uk": setUkrainian(); break;
            case "pl": setPolish(); break;
            default: setEnglish();
        }
    }

    public static String getPlay() { return play; }
    public static String getScore() { return score; }
    public static String getSettings() {return settings; }
    public static String getChangeLanguage() {return changeLanguage; }
    public static String getExit() {return exit; }
    public static String getBack() { return back; }
    public static String getAbout() { return about; }
    public static String getAboutGame() { return aboutGame; }
    public static String getAgain() { return again; }
    public static String getGood() { return good; }
    public static String getHighScore() { return highScore; }
    public static String getNewRecord() { return newRecord; }
    public static String getSoundEnable() { return soundEnable; }
    public static String getSoundDisable() { return soundDisable; }
    public static String getCircle() { return circle; }
    public static String getSquare() { return square; }
    public static String getGameMode() { return gameMode; }
}
