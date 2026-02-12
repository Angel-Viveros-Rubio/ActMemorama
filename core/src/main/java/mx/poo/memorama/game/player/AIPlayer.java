package mx.poo.memorama.game.player;

import com.badlogic.gdx.utils.Timer;
import java.util.*;
import mx.poo.memorama.board.Board;
import mx.poo.memorama.board.Card;
import mx.poo.memorama.board.Position;

/**
 * Jugador controlado por la computadora (IA).
 *
 * La IA puede recordar cartas vistas previamente
 * y tomar decisiones basadas en esa información
 * para encontrar pares.
 *
 */
public class AIPlayer implements Player {

    /** Nombre del jugador IA */
    private final String name;
    private static final int MAX_MEMORY = 4;
    /** Puntaje actual del jugador */
    private int score = 0;

    /** Memoria de cartas vistas: id de carta -> posición
     *  Máximo hecho con MAX_memory */
    private final Map<Integer, Position> knownCards = new HashMap<>();

    /**
     * Crea un jugador IA.
     *
     * @param name nombre del jugador
     */
    public AIPlayer(String name) {
        this.name = name.isEmpty() ? "CPU" : name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addPoints(int points) {
        this.score += points;
    }

    /**
     * Ejecuta el turno de la IA de forma asíncrona.
     */
    @Override
    public void makeMove(Board board, MoveCallback callback) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Position first = getRandomUnflipped(board);
                if (first == null) {
                    callback.onMoveCompleted(false);
                    return;
                }
                board.flipCard(first);
                callback.onMoveCompleted(false);

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        Position second = chooseSecondCard(board, first);
                        if (second == null) {
                            callback.onMoveCompleted(false);
                            return;
                        }
                        board.flipCard(second);
                        callback.onMoveCompleted(false);

                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                boolean match = board.checkForMatch();
                                if (!match) {
                                    board.hideFlippedCards();
                                }
                                updateMemory(board, first, second);
                                callback.onMoveCompleted(match);
                            }
                        }, 0.5f);
                    }
                }, 0.5f);
            }
        }, 0.5f);
    }

    /**
     * Calcula la mejor jugada posible usando la memoria.
     */
    private List<Position> calculateBestMove(Board board) {
        for (Map.Entry<Integer, Position> entry : knownCards.entrySet()) {
            int id = entry.getKey();
            Position knownPos = entry.getValue();
            for (int r = 0; r < board.getRows(); r++) {
                for (int c = 0; c < board.getCols(); c++) {
                    Position pos = new Position(r, c);
                    if (!pos.equals(knownPos)) {
                        Card card = board.getCard(pos);
                        if (card.getId() == id && !card.isMatched() && !card.isFaceUp()) {
                            return List.of(knownPos, pos);
                        }
                    }
                }
            }
        }

        if (!knownCards.isEmpty()) {
            Position known = new ArrayList<>(knownCards.values()).get(0);
            Position random = getRandomUnflipped(board);
            if (random != null) return List.of(known, random);
        }

        Position first = getRandomUnflipped(board);
        Position second = getRandomUnflipped(board);
        if (first != null && second != null) return List.of(first, second);
        return List.of();
    }

    /** Obtiene una posición aleatoria que no esté volteada. */
    private Position getRandomUnflipped(Board board) {
        List<Position> unflipped = new ArrayList<>();
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                Position pos = new Position(r, c);
                Card card = board.getCard(pos);
                if (!card.isMatched() && !card.isFaceUp()) {
                    unflipped.add(pos);
                }
            }
        }
        if (unflipped.isEmpty()) return null;
        return unflipped.get(new Random().nextInt(unflipped.size()));
    }

    /**
     * Ejecuta una jugada de forma síncrona (solo para pruebas).
     */
    private boolean executeMove(Board board, List<Position> positions) {
        if (positions.size() < 2) return false;
        board.flipCard(positions.get(0));
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        board.flipCard(positions.get(1));
        return board.checkForMatch();
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    /** Decide la segunda carta a voltear. */
    private Position chooseSecondCard(Board board, Position first) {
        Card firstCard = board.getCard(first);
        if (knownCards.containsKey(firstCard.getId())) {
            Position known = knownCards.get(firstCard.getId());
            if (!known.equals(first) && !board.getCard(known).isMatched()) {
                return known;
            }
        }
        return getRandomUnflipped(board);
    }

    /**
     * Actualiza la memoria de la CPU después de una jugada.
     * Memoria limitada a 4 tipos de cartas distintas.
     */
    private void updateMemory(Board board, Position first, Position second) {
        for (Position pos : List.of(first, second)) {
            Card card = board.getCard(pos);
            int id = card.getId();

            if (card.isMatched()) {
                // Olvida el par encontrado
                if (knownCards.containsKey(id)) {
                    //System.out.println("[IA] Olvida carta ID " + id + " (par encontrado)");
                    knownCards.remove(id);
                }
            } else {
                // Intenta recordar si es nueva y hay espacio
                if (!knownCards.containsKey(id) && knownCards.size() < MAX_MEMORY) {
                    knownCards.put(id, pos);
                    //System.out.println("[IA] Recuerda nueva carta ID " + id + " en posición " + pos);
                } else if (knownCards.containsKey(id)) {
                    // Actualiza posición si ya la conocía (por si la otra fue matched)
                    knownCards.put(id, pos);
                } else if (knownCards.size() >= MAX_MEMORY) {
                    //System.out.println("[IA] Memoria llena (" + MAX_MEMORY + "), no guarda ID " + id + " en " + pos);
                }
            }
        }

        // Estado final de la memoria después del turno
        //System.out.println("[IA] Memoria actual: " + knownCards.size() + "/" + MAX_MEMORY +
       //     " cartas recordadas. IDs: " + knownCards.keySet());
    }
}
