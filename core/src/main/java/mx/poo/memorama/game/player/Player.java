package mx.poo.memorama.game.player;

import mx.poo.memorama.board.Board;
public interface Player {
    String getName();
    int getScore();
    void addPoints(int points);
    void makeMove(Board board, MoveCallback callback);
    default boolean isHuman() { return true; } // Human por defecto

    @FunctionalInterface
    interface MoveCallback {
        void onMoveCompleted(boolean wasMatch);
    }
}
