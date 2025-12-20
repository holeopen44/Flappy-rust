package oostap2.game.flappy;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obstacle {
    public static float diam, radius;
    private static float drawSize, halfSize;

    private final float x;
    private float y;

    public static void ObstacleInit() {
        diam = 90;
        radius = diam *.5f;
        float realDiam = 90;
        float texSize = 128;
        float scale = diam / realDiam;
        drawSize = texSize * scale;
        halfSize = drawSize *.5f;
    }

    Obstacle(float x) {
        this.x = x;
        this.y = -radius;
    }

    public void draw(SpriteBatch batch) {
        if (Main.gameMode == GameMode.circle) {
            batch.draw(TextureManager.c_red, x - halfSize, y - halfSize, drawSize, drawSize);
            return;
        }
        batch.draw(TextureManager.red, x - radius, y - radius, diam, diam);
    }

    public void update(float delta) {
        this.y += delta * 100;
    }

    public boolean tooHigh(int h) {
        return y > h +diam;
    }

    public boolean checkPlayerCollision(float px, float py) {
        if (Main.gameMode == GameMode.circle) {
            return MyMath.checkCircleCollision(px, py, this.x, this.y);
        }
        return MyMath.checkSquareCollision(px, py, this.x, this.y);
    }
}
