package mx.poo.memorama.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import mx.poo.memorama.I18N;
import mx.poo.memorama.MemoramaGame;
import mx.poo.memorama.assets.GameAssets;
import mx.poo.memorama.board.Position;
import mx.poo.memorama.game.GameSession;
import mx.poo.memorama.game.player.Player;

/**
 * Pantalla principal del juego.
 * Muestra el tablero de cartas, los turnos,
 * los puntajes y controla la lógica visual
 * del juego de memorama.
 */
public class GameScreen extends AbstractScreen {

    /** Sesión actual del juego */
    private final GameSession session;

    /** Assets gráficos del juego */
    private final GameAssets assets;

    /** Imágenes que representan las cartas en pantalla */
    private final Image[][] cardImages;

    /** Etiqueta que muestra el turno actual */
    private Label turnLabel;

    /** Etiquetas que muestran los puntajes */
    private Label[] scoreLabels;

    /** Estilo general de los textos */
    private final Label.LabelStyle labelStyle;

    /**
     * Crea la pantalla de juego usando la sesión actual.
     * @param game instancia principal del juego
     * @param session sesión con el estado del juego
     */
    public GameScreen(MemoramaGame game, GameSession session) {
        super(game);
        this.session = session;
        this.assets = new GameAssets();
        I18N.init();

        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.8f);
        labelStyle = new Label.LabelStyle(font, Color.WHITE);

        int rows = session.getBoard().getRows();
        int cols = session.getBoard().getCols();

        cardImages = new Image[rows][cols];
        scoreLabels = new Label[2];

        buildUI(rows, cols);
        updateHUD();
        updateBoard();
    }

    /**
     * Construye la interfaz gráfica del juego.
     * Incluye el HUD (turno y puntajes)
     * y el tablero de cartas.
     */
    private void buildUI(int rows, int cols) {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Table hud = new Table();
        turnLabel = new Label(
            I18N.get("game.turn") + " " + session.getCurrentPlayer().getName(),
            labelStyle
        );
        hud.add(turnLabel).colspan(cols).center().padBottom(30);
        hud.row();

        Table scoresTable = new Table();
        for (int i = 0; i < 2; i++) {
            scoreLabels[i] = new Label("", labelStyle);
            scoresTable.add(scoreLabels[i]).pad(20);
        }
        hud.add(scoresTable).colspan(cols).center();
        hud.row();

        mainTable.add(hud).fillX().padTop(20);
        mainTable.row();

        Table grid = new Table();
        float cardWidth = (Gdx.graphics.getWidth() * 0.8f) / cols;
        float cardHeight = (Gdx.graphics.getHeight() * 0.7f) / rows;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                final Position pos = new Position(r, c);
                Image cardImage = new Image(assets.cardBack);
                cardImage.setSize(cardWidth, cardHeight);
                cardImages[r][c] = cardImage;

                cardImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (session.getCurrentPlayer().isHuman()
                            && session.getBoard().getFlippedPositions().size() < 2) {
                            handleCardClick(pos);
                        }
                    }
                });

                grid.add(cardImage).size(cardWidth, cardHeight).pad(8);
            }
            grid.row();
        }

        mainTable.add(grid).expand();
    }

    /**
     * Maneja el clic sobre una carta.
     * Voltea la carta y valida si ya se
     * seleccionaron dos cartas.
     */
    private void handleCardClick(Position pos) {
        if (!session.getBoard().flipCard(pos)) return;

        updateBoard();

        if (session.getBoard().getFlippedPositions().size() == 2) {
            session.makeCurrentPlayerMove(wasMatch -> {
                updateBoard();
                updateHUD();

                if (!session.getCurrentPlayer().isHuman() && !session.isGameOver()) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            playAITurn();
                        }
                    }, 0.8f);
                }
            });
        }
    }

    /**
     * Ejecuta el turno de la CPU.
     *
     * La CPU juega automáticamente mientras
     * no termine el juego.
     */
    private void playAITurn() {
        if (session.getCurrentPlayer().isHuman() || session.isGameOver()) return;

        session.makeCurrentPlayerMove(wasMatch ->
            Gdx.app.postRunnable(() -> {
                updateBoard();
                updateHUD();

                if (wasMatch && !session.isGameOver()
                    && !session.getCurrentPlayer().isHuman()) {

                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            playAITurn();
                        }
                    }, 1.0f);
                }
            })
        );
    }

    /**
     * Actualiza visualmente el tablero.
     * Muestra cartas volteadas, ocultas
     * o ya emparejadas.
     */
    private void updateBoard() {
        for (int r = 0; r < cardImages.length; r++) {
            for (int c = 0; c < cardImages[r].length; c++) {
                Position pos = new Position(r, c);
                Image img = cardImages[r][c];

                if (session.getBoard().getCard(pos).isMatched()) {
                    img.setColor(1, 1, 1, 0.4f);
                } else if (session.getBoard().getCard(pos).isFaceUp()) {
                    int id = session.getBoard().getCard(pos).getId();
                    img.setDrawable(
                        new TextureRegionDrawable(assets.cardFaces.get(id))
                    );
                } else {
                    img.setDrawable(
                        new TextureRegionDrawable(assets.cardBack)
                    );
                }
            }
        }
    }

    /**
     * Actualiza la información del HUD.
     *
     * Muestra turno, puntajes y el
     * resultado final si el juego termina.
     */
    private void updateHUD() {
        turnLabel.setText(
            I18N.get("game.turn") + " " + session.getCurrentPlayer().getName()
        );

        for (int i = 0; i < 2; i++) {
            Player p = session.getPlayers().get(i);
            scoreLabels[i].setText(
                p.getName() + ": " + p.getScore() + " " + I18N.get("game.score")
            );
        }

        if (session.isGameOver()) {
            Player winner = session.getWinner();
            String msg = winner == null
                ? I18N.get("game.tie")
                : I18N.format("game.winner", winner.getName());

            turnLabel.setText(msg);
            turnLabel.setColor(Color.GOLD);
        }
    }

    /**
     * Renderiza la pantalla del juego.
     *
     * @param delta tiempo entre frames
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        updateHUD();
    }

    /**
     * Libera los recursos usados por la pantalla.
     */
    @Override
    public void dispose() {
        super.dispose();
        assets.dispose();
    }
}
