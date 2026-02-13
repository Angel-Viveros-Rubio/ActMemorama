package mx.poo.memorama.game;

import com.badlogic.gdx.utils.Timer;
import mx.poo.memorama.board.Board;
import mx.poo.memorama.game.player.Player;
import mx.poo.memorama.game.player.PlayerFactory;

import java.util.List;

/**
 * Maneja una sesión completa del juego.
 * Controla el tablero, los jugadores,
 * los turnos, el puntaje y el estado
 * general de la partida.
 */
public class GameSession {

    /** Lista de jugadores de la partida */
    private final List<Player> players;

    /** Tablero del juego */
    private final Board board;

    /** Índice del jugador actual */
    private int currentPlayerIndex = 0;

    /** Modo de juego seleccionado */
    private final GameMode mode;

    /**
     * Crea una nueva sesión de juego.
     * @param mode modo de juego
     * @param player1Name nombre del jugador 1
     * @param player2Name nombre del jugador 2
     * @param rows filas del tablero
     * @param cols columnas del tablero
     */
    public GameSession(
        GameMode mode,
        String player1Name,
        String player2Name,
        int rows,
        int cols
    ) {
        this.mode = mode;
        this.board = new Board(rows, cols);

        if (mode == GameMode.TWO_PLAYERS && player2Name != null) {
            this.players = PlayerFactory.createPlayers(
                mode,
                player1Name,
                player2Name
            );
        } else {
            this.players = PlayerFactory.createPlayers(
                mode,
                player1Name,
                ""
            );
        }
    }

    /**
     * Inicia la partida desde el primer jugador.
     */
    public void start() {
        currentPlayerIndex = 0;
    }

    /**
     * Obtiene el jugador que tiene el turno actual.
     *
     * @return jugador actual
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Ejecuta el turno del jugador actual.
     *
     * Aplica la lógica de coincidencias,
     * puntaje y cambio de turno.
     */
    public void makeCurrentPlayerMove(Player.MoveCallback callback) {
        Player current = getCurrentPlayer();

        if (!current.isHuman()) {
            current.makeMove(board, wasMatch -> {
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if (wasMatch) {
                            current.addPoints(2);
                        } else {
                            nextTurn();
                        }
                        callback.onMoveCompleted(wasMatch);
                    }
                }, 0.5f);
            });
            return;
        }

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                boolean match = board.checkForMatch();

                if (!match) {
                    board.hideFlippedCards();
                    nextTurn();
                } else {
                    current.addPoints(2);
                }

                callback.onMoveCompleted(match);
            }
        }, 0.5f);
    }

    /**
     * Cambia el turno al siguiente jugador.
     */
    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    /**
     * Indica si la partida ha terminado.
     * @return true si el juego terminó
     */
    public boolean isGameOver() {
        return board.isGameOver();
    }

    /**
     * Obtiene el ganador de la partida.
     * @return jugador ganador o null si hay empate
     */
    public Player getWinner() {
        Player p1 = players.get(0);
        Player p2 = players.get(1);

        if (p1.getScore() > p2.getScore()) return p1;
        if (p2.getScore() > p1.getScore()) return p2;

        return null;
    }

    /**
     * Obtiene el tablero del juego.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Obtiene la lista de jugadores.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Obtiene el modo de juego actual.
     */
    public GameMode getMode() {
        return mode;
    }
}
