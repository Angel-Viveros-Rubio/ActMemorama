package mx.poo.memorama.board;

public class Card {
    private final int id;
    private CardState state;

    public Card(int id) {
        this.id = id;
        this.state = CardState.FACE_DOWN;
    }

    public int getId() { return id; }
    public CardState getState() { return state; }

    public void flip() {
        if (state != CardState.MATCHED) {
            state = (state == CardState.FACE_DOWN) ? CardState.FACE_UP : CardState.FACE_DOWN;
        }
    }

    public void setMatched() { this.state = CardState.MATCHED; }

    public boolean isFaceUp() { return state == CardState.FACE_UP; }
    public boolean isMatched() { return state == CardState.MATCHED; }

    public boolean matches(Card other) {
        return this.id == other.id && this != other;
    }

    @Override
    public String toString() {
        return "Card{id=" + id + ", state=" + state + "}";
    }
}
