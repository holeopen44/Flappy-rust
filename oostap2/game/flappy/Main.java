package oostap2.game.flappy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.GL20;

import java.util.Random;

public class Main extends Game {

    public static final Random random = new Random();

    public SpriteBatch batch;
    public ScreenViewport viewport;
    public OrthographicCamera camera;

    int w, h;
    int halfW;

    float score;
    int record;

    private static boolean show_fps;
    public static GameMode gameMode;
    private boolean exiting;

    public void create() {
        w = Gdx.graphics.getWidth();
        halfW = (int) (w *.5f);
        h = Gdx.graphics.getHeight();

        show_fps = false;
        exiting = false;
        gameMode = GameMode.circle;

        load();
        TextureManager.InitTextures();
        FontManager.InitFonts();
        SoundManager.InitSounds();
        LanguageManager.InitLanguages();
        ScreenManager.InitScreens(this);
        Obstacle.ObstacleInit();
        MyMath.InitMyMath(Obstacle.radius, Player.radius);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        batch = new SpriteBatch();
        viewport = new ScreenViewport(camera);
        viewport.update(w, h, true);

        Gdx.gl.glClearColor(0.529f,0.808f,0.922f, 1);

        ScreenManager.setMenu(this);
    }

    public void render() {
        if (exiting) return;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();

        if (show_fps) {
            batch.begin();
            FontManager.gameFont.draw(batch, ""+Gdx.graphics.getFramesPerSecond(), w - 50, h -50);
            batch.end();
        }
    }

    public void dispose() {
        batch.dispose();
        FontManager.dispose();
        TextureManager.dispose();
        SoundManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        w = width;
        halfW = (int) (w *.5f);
        h = height;
        viewport.update(w, h, true);
        super.resize(width, height);
    }

    private void load() {
        if (Gdx.files.local("record.txt").exists()) {
            String text = Gdx.files.local("record.txt").readString();
            record = Integer.parseInt(text);
            return;
        }
        record = 0;
    }
    public void saveBestScore() {
        record = (int) score;
        Gdx.files.local("record.txt").writeString("" + record, false);
    }
    public void startExiting() {
        exiting = true;
    }

    //debug
    public static void reset(Main game) {
        game.record = 0;
        Gdx.files.local("record.txt").writeString("" + 0, false);
    }
//    public static void say(String text) {
//        System.out.println(text);
//    }
    public static void change_show_fps() {
        show_fps = !show_fps;
    }
}
