package mx.poo.memorama.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import mx.poo.memorama.I18N;
import mx.poo.memorama.MemoramaGame;
import mx.poo.memorama.game.GameMode;
import mx.poo.memorama.game.GameSession;

/**
 * Pantalla del menú principal del juego.
 *
 * Muestra el título y las opciones principales:
 * jugar en modo un jugador, dos jugadores,
 * cambiar idioma y salir del juego.
 */
public class MainMenuScreen extends AbstractScreen {

    /** Etiqueta que muestra el título del juego */
    private final Label titleLabel;

    /** Estilo visual del título */
    private final Label.LabelStyle titleStyle;

    /**
     * Crea el menú principal y configura todos los botones.
     */
    public MainMenuScreen(MemoramaGame game) {
        super(game);

        I18N.init();

        /**
         * Configura el estilo del título del menú
         */
        BitmapFont titleFont = new BitmapFont();
        titleFont.getData().setScale(3f);
        titleStyle = new Label.LabelStyle(titleFont, Color.GOLD);

        /**
         * Configura el estilo general de los botones
         */
        BitmapFont buttonFont = new BitmapFont();
        buttonFont.getData().setScale(2f);
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;
        buttonStyle.fontColor = Color.WHITE;
        buttonStyle.overFontColor = Color.YELLOW;

        /**
         * Tabla principal que organiza los elementos del menú
         */
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        /**
         * Título del menú
         */
        titleLabel = new Label(I18N.get("menu.title"), titleStyle);
        table.add(titleLabel).padBottom(100);
        table.row();

        /**
         * Botón de un jugador (contra CPU)
         */
        TextButton onePlayerBtn = new TextButton(I18N.get("menu.single"), buttonStyle);
        onePlayerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String player1 = I18N.get("player.one");
                String player2 = I18N.get("player.cpu");
                GameSession session = new GameSession(
                    GameMode.SINGLE_PLAYER,
                    player1,
                    player2,
                    4,
                    4
                );
                game.setScreen(new GameScreen(game, session));
            }
        });
        table.add(onePlayerBtn).size(400, 100).pad(20);
        table.row();

        /**
         * Botón de dos jugadores (local)
         */
        TextButton twoPlayersBtn = new TextButton(I18N.get("menu.two"), buttonStyle);
        twoPlayersBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String player1 = I18N.get("player.one");
                String player2 = I18N.get("player.two");
                GameSession session = new GameSession(
                    GameMode.TWO_PLAYERS,
                    player1,
                    player2,
                    4,
                    4
                );
                game.setScreen(new GameScreen(game, session));
            }
        });
        table.add(twoPlayersBtn).size(400, 100).pad(20);
        table.row();

        /**
         * Botón para cambiar el idioma del juego
         */
        TextButton langBtn = new TextButton(
            I18N.get("menu.language") + I18N.currentLang.toUpperCase(),
            buttonStyle
        );
        langBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                I18N.toggleLanguage();
                game.setScreen(new MainMenuScreen(game));
            }
        });
        table.add(langBtn).size(400, 100).pad(20);
        table.row();

        /**
         * Botón para salir del juego
         */
        TextButton exitBtn = new TextButton(I18N.get("menu.exit"), buttonStyle);
        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitBtn).size(400, 100).pad(20);
    }

    /**
     * Renderiza la pantalla del menú.
     *
     * @param delta tiempo entre frames
     */
    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
