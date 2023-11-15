import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class TestPlayer {
    private Player player1;
    private Deck deck1;
    private Deck deck2;

    @Before
    public void setUp() {
        //Create a player with a hand of x cards
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < 3; i++){
            hand.add(new Card(1));
        }
        hand.add(new Card(2));
        player1 = new Player(1, hand);

        //Create a deck with x cards
        ArrayList<Card> deckCards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards.add(new Card(2));
        }
        deck1 = new Deck(1, deckCards);

        //Create deck2 with x cards
        ArrayList<Card> deckCards2 = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards2.add(new Card(3));
        }
        deck2 = new Deck(1, deckCards2);

        player1.setDeckToDrawFrom(deck1);
        player1.setDecktoPassTo(deck2);
    }

    @Test
    public void testDrawCard() {
        player1.drawCard();
        assertEquals(5, (player1.getPlayerHand()).size());
        assertEquals(3, (deck1.getDeckCards()).size());
    }

    @Test
    public void testPassCard() {
        assertEquals(4, (player1.getPlayerHand()).size());
        player1.passCard(player1.decideCardToPass());
        assertEquals(3, (player1.getPlayerHand()).size());
        assertEquals(5, (deck2.getDeckCards()).size());
    }


    
}