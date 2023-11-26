import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.junit.After;

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

    @After
    public void tearDown() {
        // Delete the file after each test
        try {
            Files.deleteIfExists(Paths.get("deck1_output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, you can rethrow the exception if you want the test to fail in case of an error during file deletion
            // throw new RuntimeException("Failed to delete test file", e);
        }
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
    public void testRemoveCard() {
        Card card = new Card(5);
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.add(card);
        Deck deck = new Deck(1, cards);

        assertEquals(card, deck.removeCard());
        assertFalse(deck.getDeckCards().contains(card));
    }

    // Test for drawing a card from an empty deck
    @Test(expected = IllegalStateException.class)
    public void testRemoveCardFromEmptyDeck() {
        Deck emptyDeck = new Deck(2, new ArrayList<Card>());
        emptyDeck.removeCard(); // Should throw IllegalStateException
    }

    // Test for showing deck contents
    @Test
    public void testShowDeck() {
        Deck deck = new Deck(3, new ArrayList<Card>());
        deck.addCard(new Card(1));
        deck.addCard(new Card(2));
        String expectedOutput = "1 2 ";
        assertEquals(expectedOutput, deck.showDeck());
    }

    @Test
    public void testWriteToFile() throws IOException {
        String message = "Deck 1 contents: 2 2 2 2 ";

        deck1.writeToFile();
        assertEquals(message, readFile("deck1_output.txt"));
    }

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }
}