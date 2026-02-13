package mx.poo.memorama.board;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBoardWithOddNumberOfCardsShouldThrowException() {
        new Board(3, 3); // 9 cartas → impar
    }

    @Test
    public void testBoardInitialization() {
        Board board = new Board(2, 2);

        assertEquals(2, board.getRows());
        assertEquals(2, board.getCols());
        assertFalse(board.isGameOver());
    }

    @Test
    public void testFlipCardSuccessfully() {
        Board board = new Board(2, 2);
        Position pos = new Position(0, 0);

        boolean result = board.flipCard(pos);

        assertTrue(result);
        assertTrue(board.getCard(pos).isFaceUp());
    }

    @Test
    public void testFlipSameCardTwiceShouldFail() {
        Board board = new Board(2, 2);
        Position pos = new Position(0, 0);

        board.flipCard(pos);
        boolean result = board.flipCard(pos);

        assertFalse(result);
    }

    @Test
    public void testCannotFlipMoreThanTwoCards() {
        Board board = new Board(2, 2);

        board.flipCard(new Position(0, 0));
        board.flipCard(new Position(0, 1));
        boolean result = board.flipCard(new Position(1, 0));

        assertFalse(result);
    }

    @Test
    public void testCheckForMatchWhenNotTwoCards() {
        Board board = new Board(2, 2);

        board.flipCard(new Position(0, 0));

        assertFalse(board.checkForMatch());
    }

    @Test
    public void testMatchFlow() {
        Board board = new Board(2, 2);

        // Buscamos dos cartas que coincidan
        Position first = null;
        Position second = null;

        for (int r1 = 0; r1 < 2; r1++) {
            for (int c1 = 0; c1 < 2; c1++) {
                for (int r2 = 0; r2 < 2; r2++) {
                    for (int c2 = 0; c2 < 2; c2++) {
                        if (r1 == r2 && c1 == c2) continue;

                        Position p1 = new Position(r1, c1);
                        Position p2 = new Position(r2, c2);

                        if (board.getCard(p1).matches(board.getCard(p2))) {
                            first = p1;
                            second = p2;
                            break;
                        }
                    }
                }
            }
        }

        assertNotNull(first);
        assertNotNull(second);

        board.flipCard(first);
        board.flipCard(second);

        boolean match = board.checkForMatch();

        assertTrue(match);
        assertTrue(board.getCard(first).isMatched());
        assertTrue(board.getCard(second).isMatched());
    }

    @Test
    public void testHideFlippedCardsWhenNotMatch() {
        Board board = new Board(2, 2);

        Position p1 = new Position(0, 0);
        Position p2 = new Position(0, 1);

        if (board.getCard(p1).matches(board.getCard(p2))) {
            p2 = new Position(1, 0); // aseguramos que no coincidan
        }

        board.flipCard(p1);
        board.flipCard(p2);

        assertFalse(board.checkForMatch());

        board.hideFlippedCards();

        assertFalse(board.getCard(p1).isFaceUp());
        assertFalse(board.getCard(p2).isFaceUp());
    }

    @Test
    public void testGameOverWhenAllMatched() {
        Board board = new Board(2, 2);

        // emparejamos todas
        for (int r1 = 0; r1 < 2; r1++) {
            for (int c1 = 0; c1 < 2; c1++) {
                for (int r2 = 0; r2 < 2; r2++) {
                    for (int c2 = 0; c2 < 2; c2++) {
                        if (r1 == r2 && c1 == c2) continue;

                        Position p1 = new Position(r1, c1);
                        Position p2 = new Position(r2, c2);

                        if (board.getCard(p1).matches(board.getCard(p2))) {
                            board.flipCard(p1);
                            board.flipCard(p2);
                            board.checkForMatch();
                        }
                    }
                }
            }
        }

        assertTrue(board.isGameOver());
    }
}
