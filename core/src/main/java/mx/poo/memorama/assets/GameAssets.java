package mx.poo.memorama.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

public class GameAssets {
    public final Texture cardBack;
    public final Map<Integer, Texture> cardFaces = new HashMap<>();

    public GameAssets() {
        cardBack = new Texture(Gdx.files.internal("cards/CardReverse.png"));

        for (int i = 1; i <= 8; i++) {
            cardFaces.put(i, new Texture(Gdx.files.internal("cards/Card" + i + ".png")));
        }
    }

    public void dispose() {
        cardBack.dispose();
        cardFaces.values().forEach(Texture::dispose);
    }
}
