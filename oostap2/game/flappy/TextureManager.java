package oostap2.game.flappy;

import com.badlogic.gdx.graphics.Texture;

public class TextureManager {

    public static Texture gray, dark_gray, black, red, white;
    public static Texture c_white, c_red;

    public static void InitTextures() {
        white = new Texture("pixels/white.png");
        gray = new Texture("pixels/gray.png");
        dark_gray = new Texture("pixels/dark_gray.png");
        black = new Texture("pixels/black.png");
        red = new Texture("pixels/red.png");
        c_white = new Texture("circles/white.png");
        c_red = new Texture("circles/red.png");
    }

    public static void dispose() {
        white.dispose();
        gray.dispose();
        dark_gray.dispose();
        black.dispose();
        red.dispose();
        c_white.dispose();
        c_red.dispose();
    }
}
