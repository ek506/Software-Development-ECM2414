public class Card {
    // Implement the card class with thread-safe operations

    //Value
    private int value;

    public Card (int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
