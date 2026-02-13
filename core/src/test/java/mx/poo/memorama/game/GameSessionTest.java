package mx.poo.memorama.game;

import mx.poo.memorama.game.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameSessionTest {

    @Test
    public void testInitialization() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "Ana",
                "Luis",
                2,
                2
        );

        assertNotNull(session.getBoard());
        assertEquals(2, session.getPlayers().size());
        assertEquals(GameMode.TWO_PLAYERS, session.getMode());
    }

    @Test
    public void testStartResetsCurrentPlayer() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "A",
                "B",
                2,
                2
        );

        session.nextTurn();
        session.start();

        Player current = session.getCurrentPlayer();
        assertEquals("A", current.getName());
    }

    @Test
    public void testNextTurnSwitchesPlayer() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "Player1",
                "Player2",
                2,
                2
        );

        Player first = session.getCurrentPlayer();
        session.nextTurn();
        Player second = session.getCurrentPlayer();

        assertNotEquals(first, second);
    }

    @Test
    public void testGameOverInitiallyFalse() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "P1",
                "P2",
                2,
                2
        );

        assertFalse(session.isGameOver());
    }

    @Test
    public void testGetWinnerPlayer1Wins() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "P1",
                "P2",
                2,
                2
        );

        Player p1 = session.getPlayers().get(0);
        Player p2 = session.getPlayers().get(1);

        p1.addPoints(4);
        p2.addPoints(2);

        assertEquals(p1, session.getWinner());
    }

    @Test
    public void testGetWinnerPlayer2Wins() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "P1",
                "P2",
                2,
                2
        );

        Player p1 = session.getPlayers().get(0);
        Player p2 = session.getPlayers().get(1);

        p1.addPoints(2);
        p2.addPoints(4);

        assertEquals(p2, session.getWinner());
    }

    @Test
    public void testGetWinnerTieReturnsNull() {
        GameSession session = new GameSession(
                GameMode.TWO_PLAYERS,
                "P1",
                "P2",
                2,
                2
        );

        Player p1 = session.getPlayers().get(0);
        Player p2 = session.getPlayers().get(1);

        p1.addPoints(4);
        p2.addPoints(4);

        assertNull(session.getWinner());
    }
}
