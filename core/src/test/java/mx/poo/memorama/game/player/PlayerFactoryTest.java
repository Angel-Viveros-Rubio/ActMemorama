package mx.poo.memorama.game.player;

import mx.poo.memorama.game.GameMode;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlayerFactoryTest {

    @Test
    public void testTwoPlayersModeCreatesTwoHumans() {
        List<Player> players = PlayerFactory.createPlayers(
                GameMode.TWO_PLAYERS,
                "Ana",
                "Luis"
        );

        assertEquals(2, players.size());
        assertTrue(players.get(0) instanceof HumanPlayer);
        assertTrue(players.get(1) instanceof HumanPlayer);
    }

    @Test
    public void testSinglePlayerModeCreatesHumanAndCPU() {
        List<Player> players = PlayerFactory.createPlayers(
                GameMode.SINGLE_PLAYER,
                "Carlos",
                ""
        );

        assertEquals(2, players.size());
        assertTrue(players.get(0) instanceof HumanPlayer);
        assertTrue(players.get(1) instanceof AIPlayer);
    }

    @Test
    public void testEmptyPlayer1NameDefaultsCorrectly() {
        List<Player> players = PlayerFactory.createPlayers(
                GameMode.SINGLE_PLAYER,
                "",
                ""
        );

        Player player1 = players.get(0);

        assertTrue(player1 instanceof HumanPlayer);
        assertEquals("Jugador 1", player1.getName());
    }

    @Test
    public void testEmptyPlayer2NameDefaultsInTwoPlayersMode() {
        List<Player> players = PlayerFactory.createPlayers(
                GameMode.TWO_PLAYERS,
                "Mario",
                ""
        );

        Player player2 = players.get(1);

        assertTrue(player2 instanceof HumanPlayer);
        assertEquals("Jugador 2", player2.getName());
    }

    @Test
    public void testCpuAlwaysNamedCPUInSinglePlayer() {
        List<Player> players = PlayerFactory.createPlayers(
                GameMode.SINGLE_PLAYER,
                "Sofía",
                "NoImporta"
        );

        Player cpu = players.get(1);

        assertTrue(cpu instanceof AIPlayer);
        assertEquals("CPU", cpu.getName());
    }
}

