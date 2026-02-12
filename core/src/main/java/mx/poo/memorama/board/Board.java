package mx.poo.memorama.board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el tablero del juego Memorama.
 * Administra la disposición de las cartas,
 * el volteo, la verificación de pares
 * y el estado general del juego.
 */
public class Board {

    /** Matriz de cartas del tablero */
    private final Card[][] grid;

    /** Número de filas del tablero */
    private final int rows;

    /** Número de columnas del tablero */
    private final int cols;

    /** Lista de posiciones actualmente volteadas */
    private final List<Position> flippedCards = new ArrayList<>(2);

    /**
     * Crea un nuevo tablero con el tamaño indicado.
     * @param rows número de filas
     * @param cols número de columnas
     * @throws IllegalArgumentException si el total de cartas no es par
     */
    public Board(int rows, int cols) {
        if (rows * cols % 2 != 0) {
            throw new IllegalArgumentException(
                "Total de cartas debe ser par"
            );
        }
        this.rows = rows;
        this.cols = cols;
        this.grid = new Card[rows][cols];
        initializeCards();
        shuffle();
    }

    /**
     * Inicia las cartas del tablero en pares.
     */
    private void initializeCards() {
        int totalCards = rows * cols;
        int pairs = totalCards / 2;
        int cardIndex = 0;

        for (int i = 1; i <= pairs; i++) {
            grid[cardIndex / cols][cardIndex % cols] = new Card(i);
            cardIndex++;
            grid[cardIndex / cols][cardIndex % cols] = new Card(i);
            cardIndex++;
        }
    }

    /**
     * Mezcla aleatoriamente las cartas del tablero.
     */
    public void shuffle() {
        List<Card> cards = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cards.add(grid[r][c]);
            }
        }

        Collections.shuffle(cards);

        int index = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = cards.get(index++);
            }
        }
    }

    /**
     * Obtiene la carta en una posición específica.
     * @param pos posición solicitada
     * @return carta correspondiente
     */
    public Card getCard(Position pos) {
        return grid[pos.row][pos.col];
    }

    /**
     * Voltea una carta si la jugada es válida.
     *
     * @param pos posición de la carta
     * @return true si la carta se volteó correctamente
     */
    public boolean flipCard(Position pos) {
        Card card = getCard(pos);

        if (card.isMatched()
            || flippedCards.size() >= 2
            || card.isFaceUp()) {
            return false;
        }

        card.flip();
        flippedCards.add(pos);
        return true;
    }

    /**
     * Verifica si las dos cartas volteadas coinciden.
     *
     * @return true si forman un par
     */
    public boolean checkForMatch() {
        if (flippedCards.size() != 2) return false;

        Card first = getCard(flippedCards.get(0));
        Card second = getCard(flippedCards.get(1));

        if (first.matches(second)) {
            first.setMatched();
            second.setMatched();
            flippedCards.clear();
            return true;
        }
        return false;
    }

    /**
     * Oculta las cartas volteadas cuando no hay coincidencia.
     */
    public void hideFlippedCards() {
        for (Position pos : flippedCards) {
            getCard(pos).flip();
        }
        flippedCards.clear();
    }

    /**
     * Indica si todas las cartas han sido emparejadas.
     *
     * @return true si el juego terminó
     */
    public boolean isGameOver() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!grid[r][c].isMatched()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Obtiene el número de filas del tablero.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtiene el número de columnas del tablero.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Devuelve las posiciones de las cartas actualmente volteadas.
     * @return lista de posiciones volteadas
     */
    public List<Position> getFlippedPositions() {
        return new ArrayList<>(flippedCards);
    }
}
