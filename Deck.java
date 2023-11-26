

//import java.lang.reflect.Array;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a deck of cards that is between two players. Each deck has a number and a list of cards
 */
public class Deck {
    /**
     * The list of card objects in the deck
     */
    private ArrayList<Card> deckCards = new ArrayList<Card>();
    /**
     * The deck number
     */
    private int deckNumber;
    /**
     * The name of the output file to write to
     */
    private String filename;

    /**
     * Creates a deck with a deck number and a list of cards. Sets the file to ouput to
     * @param deckNumber the deck number
     * @param deckCards the list of cards in the deck
     */
    public Deck(int deckNumber, ArrayList<Card> deckCards) {
        this.deckCards = deckCards;
        this.deckNumber = deckNumber;
        this.filename = "deck" + deckNumber + "_output.txt";
    }

    /**
     * Returns the list of cards in the deck
     * @return the list of cards in the deck
     */
    public ArrayList<Card> getDeckCards(){
        return deckCards;
    }

    /**
     * Returns the deck number
     * @return the deck number
     */
    public int getDeckNumber(){
        return deckNumber;
    }

    /**
     * Returns the cards in the deck as a string of their values
     * @return String of card values
     */
    public String showDeck(){
        String cards = "";
        for (Card c: deckCards){
            cards += c.getValue() + " ";
        }
        return cards;
    }
    
    /**
     * Adds a card to the bottom of the deck
     * @param c the card to add
     */
    public void addCard(Card c){
        deckCards.add(c);
    } 

    /**
     * Removes the top card from the deck and returns it
     * @return the top card from the deck
     * @throws IllegalStateException if the deck is empty
     */
    public Card removeCard() {
        if (deckCards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return deckCards.remove(0);
    }

    /**
     * Writes the contents of the deck to the output file
     */
    public void writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false));
            String message = "Deck " + deckNumber + " contents: " + showDeck();
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }
    }
}