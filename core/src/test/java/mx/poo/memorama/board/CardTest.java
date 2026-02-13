package mx.poo.memorama.board;

import org.junit.Test;
import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void testInitialState() {
        Card card = new Card(10);

        assertEquals(10, card.getId());
        assertEquals(CardState.FACE_DOWN, card.getState());
        assertFalse(card.isFaceUp());
        assertFalse(card.isMatched());
    }

    @Test
    public void testFlipFromFaceDownToFaceUp() {
        Card card = new Card(1);

        card.flip();

        assertTrue(card.isFaceUp());
        assertEquals(CardState.FACE_UP, card.getState());
    }

    @Test
    public void testFlipBackToFaceDown() {
        Card card = new Card(2);

        card.flip();
        card.flip();

        assertFalse(card.isFaceUp());
        assertEquals(CardState.FACE_DOWN, card.getState());
    }

    @Test
    public void testFlipDoesNothingIfMatched() {
        Card card = new Card(3);

        card.setMatched();
        card.flip();

        assertTrue(card.isMatched());
        assertEquals(CardState.MATCHED, card.getState());
    }

    @Test
    public void testSetMatchedChangesState() {
        Card card = new Card(4);

        card.setMatched();

        assertTrue(card.isMatched());
        assertEquals(CardState.MATCHED, card.getState());
    }

    @Test
    public void testMatchesSameIdDifferentObjects() {
        Card card1 = new Card(5);
        Card card2 = new Card(5);

        assertTrue(card1.matches(card2));
    }

    @Test
    public void testMatchesDifferentId() {
        Card card1 = new Card(6);
        Card card2 = new Card(7);

        assertFalse(card1.matches(card2));
    }

    @Test
    public void testMatchesSameObjectShouldBeFalse() {
        Card card = new Card(8);

        assertFalse(card.matches(card));
    }



    @Test
    public void testToStringContainsIdAndState() {
        Card card = new Card(11);

        String text = card.toString();

        assertTrue(text.contains("11"));
        assertTrue(text.contains("FACE_DOWN"));
    }
}
