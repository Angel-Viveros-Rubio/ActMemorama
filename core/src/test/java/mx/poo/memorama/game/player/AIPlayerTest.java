package mx.poo.memorama.game.player;

import mx.poo.memorama.board.Board;
import mx.poo.memorama.board.Position;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.Assert.*;

public class AIPlayerTest {

    @Test
    public void testConstructorDefaultName() {
        AIPlayer ai = new AIPlayer("");
        assertEquals("CPU", ai.getName());
    }

    @Test
    public void testConstructorCustomName() {
        AIPlayer ai = new AIPlayer("Bot");
        assertEquals("Bot", ai.getName());
    }

    @Test
    public void testAddPoints() {
        AIPlayer ai = new AIPlayer("CPU");
        ai.addPoints(2);
        ai.addPoints(3);
        assertEquals(5, ai.getScore());
    }

    @Test
    public void testIsHumanIsFalse() {
        AIPlayer ai = new AIPlayer("CPU");
        assertFalse(ai.isHuman());
    }

    @Test
    public void testMemoryDoesNotExceedLimit() throws Exception {
        AIPlayer ai = new AIPlayer("CPU");
        Board board = new Board(4, 4);

        // Acceder al método privado updateMemory
        Method updateMemory = AIPlayer.class.getDeclaredMethod(
                "updateMemory",
                Board.class,
                Position.class,
                Position.class
        );
        updateMemory.setAccessible(true);

        // Forzar múltiples recuerdos
        for (int i = 0; i < 5; i++) {
            updateMemory.invoke(
                    ai,
                    board,
                    new Position(0, i % board.getCols()),
                    new Position(1, i % board.getCols())
            );
        }

        // Acceder al campo privado knownCards
        Field memoryField = AIPlayer.class.getDeclaredField("knownCards");
        memoryField.setAccessible(true);

        Map<?, ?> memory = (Map<?, ?>) memoryField.get(ai);

        assertTrue(memory.size() <= 4);
    }
}
