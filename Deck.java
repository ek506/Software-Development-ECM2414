//import java.lang.reflect.Array;
import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> deckCards = new ArrayList<Card>();
    private int deckNumber;

    public Deck(int deckNumber, ArrayList<Card> deckCards) {
        this.deckCards = deckCards;
        this.deckNumber = deckNumber;
    }

    public void addCard(Card c){
        deckCards.add(c);
    } 

    public ArrayList<Card> getDeckCards(){
        return deckCards;
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
}