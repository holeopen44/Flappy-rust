package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.HashMap;
import java.util.Locale;

public class LanguagesManager {

    private static final String LANGUAGES_FILE = "languages.xml";
    private static final String DEFAULT_LANGUAGE = "en";

    private final HashMap<String, String> strings = new HashMap<>();


    public void setLanguage(String language) {
        if (load(language)) return;

        String system = Locale.getDefault().getLanguage();

        if (load(system)) return;

        load(DEFAULT_LANGUAGE);
    }

    public String getString(String key) {
        String string = strings.get(key);

        if (string != null) {
            return string;
        }

        return key;
    }

    public Array<LanguageItem> getAvailableLanguages() {
        Array<LanguageItem> result = new Array<>();

        try {
            Element root = loadDocument();
            Array<Element> languages = root.getChildrenByName("language");

            for (int i = 0; i < languages.size; i++) {
                Element language = languages.get(i);

                String name = language.getAttribute("name");
                String fullName = language.getAttribute("full_name");

                result.add(new LanguageItem(name, fullName));
            }
        } catch (Exception e) {
            Gdx.app.log("Error loading languages file", LANGUAGES_FILE, e);
        }

        return result;
    }

    private Element loadDocument() {
        XmlReader reader = new XmlReader();
        return reader.parse(Gdx.files.internal(LANGUAGES_FILE));
    }

    private boolean load(String lang) {
        try {
            Element root = loadDocument();
            Array<Element> languages = root.getChildrenByName("language");

            for (int i = 0; i < languages.size; i++) {
                Element language = languages.get(i);

                if (!language.getAttribute("name").equals(lang)) {
                    continue;
                }

                strings.clear();
                Array<Element> items = language.getChildrenByName("string");

                for (int j = 0; j < items.size; j++) {
                    Element item = items.get(j);

                    strings.put(
                        item.getAttribute("key"),
                        item.getAttribute("value")
                    );
                }

                return true;
            }
        } catch (Exception e) {
            Gdx.app.log("Error loading languages file", LANGUAGES_FILE, e);
        }

        return false;
    }


    public static class LanguageItem {

        private final String name;
        private final String fullName;


        LanguageItem(String name, String fullName) {
            this.name = name;
            this.fullName = fullName;
        }

        public String name() {
            return name;
        }

        public String fullName() {
            return fullName;
        }
    }
}
