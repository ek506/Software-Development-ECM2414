package cardgame;

/**
 * Represents a card with an integer value
 */
public class Card {
    /**
     * The value of the card
     */
    private int value;

    /**
     * Creates a card with a value
     * @param value the value of the card
     */
    public Card (int value) {
        this.value = value;
    }

    /**
     * Returns the value of the card
     * @return the value of the card
     */
    public int getValue(){
        return value;
    }
}
