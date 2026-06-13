package oostap2.game.flappy.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import oostap2.game.flappy.Resources;

public class UserInterface {

    private static final int[] KEYS = {
        Keys.NUM_1, Keys.NUM_2, Keys.NUM_3, Keys.NUM_4,
        Keys.NUM_5, Keys.NUM_6, Keys.NUM_7, Keys.NUM_8
    };

    private final Array<InterfaceElement> elements = new Array<>();
    private final Resources resources;


    public UserInterface(Resources resources) {
        this.resources = resources;
    }

    public void addButton(int index, String key, Runnable action) {
        Button button = new Button(index, key, resources.pixel(), resources.font(), resources.click(), action);
        elements.add(button);
    }

    public void addLabel(String key) {
        Label label = new Label(key, resources.font());
        elements.add(label);
    }

    public void handleKeyboard() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) return;

        for (int i = 0; i < KEYS.length; i++) {
            if (Gdx.input.isKeyJustPressed(KEYS[i])) {
                clickButtonByIndex(i);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        for (int i = 0; i < elements.size; i++) {
            elements.get(i).draw(batch);
        }
    }

    public void resize(float width, float height) {
        for (int i = 0; i < elements.size; i++) {
            elements.get(i).resize(width, height);
        }
    }

    public void handleTouch(float touchX, float touchY) {
        for (int i = 0; i < elements.size; i++) {
            InterfaceElement element = elements.get(i);

            if (element instanceof Button) {
                Button button = (Button) element;
                button.touch(touchX, touchY);
            }
        }
    }

    public void applyLocalization() {
        for (int i = 0; i < elements.size; i++) {
            InterfaceElement element = elements.get(i);

            element.applyText(resources.getString(element.getTextKey()));
        }
    }

    private void clickButtonByIndex(int index) {
        for (int i = 0; i < elements.size; i++) {
            InterfaceElement element = elements.get(i);

            if (element instanceof Button) {
                Button button = (Button) element;

                if (button.index() == index) {
                    button.click();
                }
            }
        }
    }
}
