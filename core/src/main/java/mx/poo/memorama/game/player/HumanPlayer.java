package mx.poo.memorama.game.player;

import mx.poo.memorama.board.Board;

public class HumanPlayer implements Player {
    private final String name;
    private int score = 0;

    public HumanPlayer(String name) {
        this.name = name.isEmpty() ? "Jugador" : name;
    }

    @Override
    public String getName() { return name; }

    @Override
    public int getScore() { return score; }

    @Override
    public void addPoints(int points) { this.score += points; }

    @Override
    public void makeMove(Board board, MoveCallback callback) {

    }

    @Override
    public boolean isHuman() { return true; }
}
