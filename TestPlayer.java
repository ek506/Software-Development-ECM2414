import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestPlayer {
    private Player player1;
    private Player player2;
    private Deck deck1;
    private Deck deck2;

    @Before
    public void setUp() {
        //Create player 1
        ArrayList<Card> hand1 = new ArrayList<Card>();
        for (int i = 0; i < 2; i++){
            hand1.add(new Card(1));
        }
        hand1.add(new Card(2));
        hand1.add(new Card(3));
        player1 = new Player(1, hand1);

        //Create player 2
        ArrayList<Card> hand2 = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            hand2.add(new Card(5));
        }
        player2 = new Player(1, hand2);

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

    @Test
    public void testDecideCardToPassto(){
        assertNotEquals(player1.decideCardToPass().getValue(), 1);
        assertEquals(player1.decideCardToPass().getValue(), 2);
    }

    @Test
    public void testCheckWin(){
        assertFalse(player1.checkWin());
        assertTrue(player2.checkWin());

    }

    @Test
    public void testWriteToFile() throws IOException {
        String message1 = "Test message 1";
        String message2 = "Test message 2";
        String message3 = "Test message 3";
        String filename = "player1_output.txt";

        player1.writeToFile(message1, false); // Check it writes to file
        assertEquals(message1, readFile(filename));

        player1.writeToFile(message2, true); // Check it appends to file
        assertEquals(message1 + message2, readFile(filename));

        player1.writeToFile(message3, false); // Check it overwrites file
        assertEquals(message3, readFile(filename));
    }

    private String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filename)));
    }

}