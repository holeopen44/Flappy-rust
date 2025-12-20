package oostap2.game.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static Sound click, explosion, hit, coin;
    public static boolean enabled;

    public static void InitSounds() {
        click = Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        hit = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
        coin = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.wav"));
        enabled = true;
    }

    public static void dispose() {
        click.dispose();
        explosion.dispose();
        hit.dispose();
        coin.dispose();
    }

    public static void playClick() {
        play(click, .9f);
    }

    public static void playExplosion() {
        play(explosion, .2f);
    }

    public static void playHit() {
        play(hit, .8f);
    }

    public static void playCoin() {
        play(coin, .9f);
    }

    private static void play(Sound sound, float volume) {
        if (enabled) sound.play(volume);
    }

    public static void turn_on() {
        enabled = true;
    }

    public static void turn_off() {
        enabled = false;
    }
}
