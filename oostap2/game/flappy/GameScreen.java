package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.Input.Keys;

public class GameScreen implements Screen {

    final Main game;

    Player player;
    float dieIterator;
    float obstacleIterator;
    Array<Obstacle> obstacles;

    public GameScreen(final Main game) {
        this.game = game;
        player = new Player();
        obstacles = new Array<>();
    }

    @Override
    public void show() {
        obstacleIterator = 0;
        player.reset(game);
        obstacles.clear();
        dieIterator = .5f;
        game.score = 0;
    }

    @Override
    public void render(float delta) {
        draw();
        logic(delta);
        input();
    }

    private boolean checkIfOver(float delta) {
        if (player.is_alive) return false;
        dieIterator -= delta;
        if (dieIterator < 0) ScreenManager.setOver(game);
        return true;
    }

    private void createObstacle() {
        Obstacle obstacle = new Obstacle(Main.random.nextInt(game.w));
        obstacles.add(obstacle);
        obstacleIterator = 0;
    }

    private void draw() {
        game.viewport.apply();
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        player.draw(game.batch);

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(game.batch);
        }

        FontManager.gameFont.draw(game.batch, LanguageManager.getScore() + (int) game.score, 10, game.h -50);

        game.batch.end();

    }

    private void logic(float delta) {
        if (checkIfOver(delta)) return;
        player.update(delta);
        game.score += delta * 10;

        obstacleIterator += delta;

        if (obstacleIterator > 1000f/game.w) {
            createObstacle();
        }

        for (int i = obstacles.size - 1; i >= 0; i--) {
            Obstacle obstacle = obstacles.get(i);
            obstacle.update(delta);
            if (obstacle.tooHigh(game.h)) {
                obstacles.removeIndex(i);
            }
            if (obstacle.checkPlayerCollision(player.getX(), player.getY())) {
                player.kill();
            }
        }
    }

    private void input() {
        if (!player.is_alive) return;

        if (Gdx.input.isKeyJustPressed(Keys.A) || Gdx.input.isKeyJustPressed(Keys.LEFT)) {
            player.goLeft();
        }
        if (Gdx.input.isKeyJustPressed(Keys.D) || Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
            player.goRight();
        }

        if (Gdx.input.justTouched()) {
            if (Gdx.input.getX() > game.halfW) {
                player.goRight();
            } else {
                player.goLeft();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void hide() {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void dispose() {}
}
