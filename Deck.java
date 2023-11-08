import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> deckCards = new ArrayList<Card>();

    public void addCard(Card c){
        deckCards.add(c);
    } 

    //Returns card at front of queue
    public Card drawCard(){
        return deckCards.remove(0);
    }
}