import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        player2 = new Player(2, hand2);

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

    // Test for constructor and initial setup
    @Test
    public void testConstructorAndInitialSetup() {
        assertNotNull(player1);
        assertEquals(1, player1.getPlayerNumber());
        assertNotNull(player1.getPlayerHand());
        assertEquals(4, player1.getPlayerHand().size());
    }

    // Test for setting and getting decks
    @Test
    public void testSetAndGetDecks() {
        player1.setDeckToDrawFrom(deck2);
        player1.setDecktoPassTo(deck1);
        assertEquals(deck2, player1.getDeckToDrawFrom());   
        assertEquals(deck1, player1.getDeckToPassTo());
    }



    @Test
    public void testDrawCard() {
        int initialDeckSize = deck1.getDeckCards().size();
        player1.drawCard();
        assertEquals(initialDeckSize - 1, deck1.getDeckCards().size());
        assertEquals(5, player1.getPlayerHand().size());
    }

    // Test for handling empty deck in drawCard
    @Test(expected = IllegalStateException.class)
    public void testDrawCardWithEmptyDeck() {
        Deck emptyDeck = new Deck(3, new ArrayList<Card>());
        player1.setDeckToDrawFrom(emptyDeck);
        player1.drawCard();
    }

    @Test
    public void testPassCard() {
        Card cardToPass = player1.getPlayerHand().get(0);
        player1.passCard(cardToPass);
        assertFalse(player1.getPlayerHand().contains(cardToPass));
        assertTrue(deck2.getDeckCards().contains(cardToPass));
    }

    @Test
    public void testDecideCardToPassTo(){
        Card card = player1.decideCardToPass();
        assertNotNull(card);
        assertEquals(card.getValue(), 2);
    }

    @Test
    public void testCheckWin(){
        assertFalse(player1.checkWin());
        assertTrue(player2.checkWin());

    }

    // Test for winning the game
    @Test
    public void testWinMethod() {
        player2.win();
        assertTrue(Player.isGameOver());
        assertEquals(2, Player.getWinner());
    }

    // Test other player wins
    @Test
    public void testPrintOtherPlayerWins() throws IOException {
        player2.win();
        player1.printOtherPlayerWins();
        List<String> lines = Files.readAllLines(Paths.get("player1_output.txt"));
        List<String> lastLines = lines.subList(Math.max(lines.size() - 3, 0), lines.size());
        String actualOutput = String.join("\n", lastLines);
        String expectedOutput = "player 2 has informed player 1 that player 2 has won\n"
                            + "player 1 exits\n"
                            + "player 1 hand: " + player1.showHand();
        
        assertEquals(expectedOutput, actualOutput);
    }

    // Test for player turn
    @Test
    public void testPlayerTurn() {
        int initialDeckToDrawFromSize = deck1.getDeckCards().size();
        int initialDeckToPassToSize = deck2.getDeckCards().size();
        int initialHandSize = player1.getPlayerHand().size();
        player1.playerTurn();
        assertEquals(initialDeckToDrawFromSize - 1, deck1.getDeckCards().size());
        assertEquals(initialDeckToPassToSize + 1, deck2.getDeckCards().size());
        assertNotNull(player1.getPlayerHand());
        assertEquals(initialHandSize, player1.getPlayerHand().size());
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