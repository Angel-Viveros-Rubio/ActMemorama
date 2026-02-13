package mx.poo.memorama.board;

import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testConstructor() {
        Position pos = new Position(2, 3);

        assertEquals(2, pos.row);
        assertEquals(3, pos.col);
    }

    @Test
    public void testEqualsSameObject() {
        Position pos = new Position(1, 1);

        assertTrue(pos.equals(pos));
    }

    @Test
    public void testEqualsSameValues() {
        Position pos1 = new Position(4, 5);
        Position pos2 = new Position(4, 5);

        assertTrue(pos1.equals(pos2));
    }

    @Test
    public void testEqualsDifferentRow() {
        Position pos1 = new Position(1, 2);
        Position pos2 = new Position(2, 2);

        assertFalse(pos1.equals(pos2));
    }

    @Test
    public void testEqualsDifferentCol() {
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 5);

        assertFalse(pos1.equals(pos2));
    }

    @Test
    public void testEqualsWithNull() {
        Position pos = new Position(0, 0);

        assertFalse(pos.equals(null));
    }

    @Test
    public void testEqualsWithDifferentObjectType() {
        Position pos = new Position(1, 1);

        assertFalse(pos.equals("NotAPosition"));
    }

    @Test
    public void testHashCodeConsistency() {
        Position pos = new Position(7, 8);

        int firstHash = pos.hashCode();
        int secondHash = pos.hashCode();

        assertEquals(firstHash, secondHash);
    }

    @Test
    public void testHashCodeSameForEqualObjects() {
        Position pos1 = new Position(9, 10);
        Position pos2 = new Position(9, 10);

        assertEquals(pos1.hashCode(), pos2.hashCode());
    }

    @Test
    public void testHashCodeDifferentForDifferentObjects() {
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);

        assertNotEquals(pos1.hashCode(), pos2.hashCode());
    }
}
