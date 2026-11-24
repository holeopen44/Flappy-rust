package oostap2.game.flappy.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import oostap2.game.flappy.Resources;
import java.util.Random;

public class Game {

    private final Preferences saves;
    private final Player player;
    private final Obstacles obstacles;
    private final Score score;
    private final Pause pause;

    private float halfWidth;
    private boolean running;


    public Game(Resources resources) {
        Random rng = new Random();

        saves = resources.saves();
        player = new Player(resources.circle(), resources.hit(), resources.explosion());
        obstacles = new Obstacles(resources.circle(), player, rng);
        score = new Score(resources.font());
        pause = new Pause(resources.font());
    }

    public void reset() {
        player.reset();
        obstacles.reset();
        score.reset();
        running = true;
    }

    public void draw(SpriteBatch batch) {
        player.draw(batch);
        obstacles.draw(batch);
        score.draw(batch);
        pause.draw(batch);
    }

    public void move(float delta) {
        if (pause.countdown(delta)) return;

        keyboardControls();
        player.move(delta);
        obstacles.move(delta);
        score.move(delta);

        if (player.isAlive()) return;

        running = false;

        saveScore();
    }

    public void touch(float touchX) {
        if (touchX > halfWidth) {
            player.jumpRight();
            return;
        }

        player.jumpLeft();
    }

    public void resize(float width, float height) {
        player.resize(width, height);
        obstacles.resize(width, height);
        score.resize(width, height);
        pause.resize(width, height);

        halfWidth = width * 0.5f;
    }

    public void pause() {
        pause.pause();
    }

    public void resume() {
        pause.resume();
    }

    private void keyboardControls() {
        if (Gdx.app.getType() == Application.ApplicationType.Android) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.A) || Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            player.jumpLeft();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.D) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            player.jumpRight();
        }
    }

    private void saveScore() {
        saves.putInteger("last_score", score.get());

        int highScore = saves.getInteger("high_score", 0);
        if (score.get() > highScore) {
            saves.putInteger("high_score", score.get());
        }

        saves.flush();
    }


    public boolean isRunning() {
        return running;
    }
}
