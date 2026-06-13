package oostap2.game.flappy.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

interface InterfaceElement {

    void draw(SpriteBatch batch);

    void resize(float width, float height);

    void applyText(String text);

    String getTextKey();
}
