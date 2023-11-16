import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;

public class TestDeck {
    private Deck deck1;

    @Before
    public void setUp(){
        ArrayList<Card> deckCards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards.add(new Card(2));
        }
        deck1 = new Deck(1, deckCards);
    }

    @Test
    public void testConstructorAndGetters() {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(new Card(5));
        Deck deck = new Deck(1, cards);

        assertEquals(1, deck.getDeckNumber());
        assertEquals(cards, deck.getDeckCards());
    }

    @Test
    public void testAddCardAndGetDeckCards() {
        Deck deck = new Deck(1, new ArrayList<Card>());
        Card card = new Card(5);
        deck.addCard(card);

        assertTrue(deck.getDeckCards().contains(card));
    }

    @Test
    public void testDrawCard() {
        Card card = new Card(5);
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(card);
        Deck deck = new Deck(1, cards);

        assertEquals(card, deck.drawCard());
        assertFalse(deck.getDeckCards().contains(card));
    }

    @Test
    public void testWriteToFile() throws IOException {
        String message = "Test message";

        deck1.writeToFile(message);
        assertEquals(message, readFile("deck1_output.txt"));
    }

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}