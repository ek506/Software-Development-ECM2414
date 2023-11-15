//import java.lang.reflect.Array;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> deckCards = new ArrayList<Card>();
    private int deckNumber;
    private String filename;

    public Deck(int deckNumber, ArrayList<Card> deckCards) {
        this.deckCards = deckCards;
        this.deckNumber = deckNumber;
        this.filename = "deck" + deckNumber + "_output.txt";
    }

    public void addCard(Card c){
        deckCards.add(c);
    } 

    public ArrayList<Card> getDeckCards(){
        return deckCards;
    }

    public int getDeckNumber(){
        return deckNumber;
    }

    //Rename to toString
    public String showDeck(){
        String cards = "";
        for (Card c: deckCards){
            cards += c.getValue() + " ";
        }
        return cards;
    }

    //Returns card at front of queue
    public Card drawCard(){
        return deckCards.remove(0);
    }

    public String toString(){
        return "Deck " + deckNumber + ": " + showDeck();
    }

    //Write deck to file at end of game
    public void writeToFile(String message) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }
    }
}