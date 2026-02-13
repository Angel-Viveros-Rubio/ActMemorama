package mx.poo.memorama.game.player;

import java.util.ArrayList;
import java.util.List;
import mx.poo.memorama.game.GameMode;

/**
 * Fábrica de jugadores del juego.
 * Se encarga de crear los jugadores según
 * el modo de juego seleccionado.
 */
public class PlayerFactory {

    /**
     * Crea la lista de jugadores para la partida.
     * Si el modo es de dos jugadores, ambos son humanos.
     * Si el modo es de un jugador, el segundo es la CPU.
     * @param mode modo de juego seleccionado
     * @param player1Name nombre del jugador 1
     * @param player2Name nombre del jugador 2
     * @return lista de jugadores creados
     */
    public static List<Player> createPlayers(
        GameMode mode,
        String player1Name,
        String player2Name
    ) {
        List<Player> players = new ArrayList<>();

        players.add(
            new HumanPlayer(
                player1Name.isEmpty() ? "Jugador 1" : player1Name
            )
        );

        if (mode == GameMode.TWO_PLAYERS) {
            players.add(
                new HumanPlayer(
                    player2Name.isEmpty() ? "Jugador 2" : player2Name
                )
            );
        } else {
            players.add(new AIPlayer("CPU"));
        }

        return players;
    }
}
