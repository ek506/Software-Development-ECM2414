import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TestCardGame {
    //Resets players and decks lists before each test
    @Before
    public void setUp() {
        CardGame.players = new ArrayList<>();
        CardGame.decks = new ArrayList<>();
        Player.resetWinner();
        Player.resetGameOver();
    }

    @After
    public void tearDown() {
        // Delete the file after each test
        try {
            Files.deleteIfExists(Paths.get("deck1_output.txt"));
            Files.deleteIfExists(Paths.get("deck2_output.txt"));
            Files.deleteIfExists(Paths.get("deck3_output.txt"));
            Files.deleteIfExists(Paths.get("deck4_output.txt"));
            Files.deleteIfExists(Paths.get("player1_output.txt"));
            Files.deleteIfExists(Paths.get("player2_output.txt"));
            Files.deleteIfExists(Paths.get("player3_output.txt"));
            Files.deleteIfExists(Paths.get("player4_output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsValidNumPlayers() {
        assertTrue(CardGame.isValidNumPlayers(3));
        assertFalse(CardGame.isValidNumPlayers(1));
    }

    // Tests valid pack, pack with too many cards, pack containing non integers, pack containing negative numbers, and incorrect file location
    @Test
    public void testIsValidPack() {
        assertTrue(CardGame.isValidPack("TestPacks/Valid4PlayerPack.txt", 4));
        assertFalse(CardGame.isValidPack("TestPacks/4PlayerPack_TooManyCards.txt", 4));
        assertFalse(CardGame.isValidPack("TestPacks/4PlayerPack_NotIntegers.txt", 4));
        assertFalse(CardGame.isValidPack("TestPacks/4PlayerPack_ContainsNegative.txt", 4));
        assertFalse(CardGame.isValidPack("Not_Real_File.txt", 4));
    }

    //Tests the first card is correct and the number of cards created is correct
    @Test
    public void testReadCards() throws IOException {
        ArrayList<Card> cards = CardGame.readCards("TestPacks/Valid4PlayerPack.txt");
        assertEquals(1, cards.get(0).getValue());
        assertEquals(32, cards.size());
    }

    @Test
    public void testSplitDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            cards.add(new Card(i));
        }
        ArrayList<ArrayList<Card>> splitDecks = CardGame.splitDeck(cards, 2);
        // Verify correct splitting for player 1 and deck 1
        assertEquals(4, splitDecks.get(0).size()); // First player's hand
        assertEquals(4, splitDecks.get(2).size()); // First deck

        // Checks cards are dealt in a round-robin fashion
        ArrayList<Card> firstPlayerHand = new ArrayList<>();
        for (int i = 1; i <= 8; i+=2) {
            firstPlayerHand.add(new Card(i));
        }
        for (int i = 0; i < 4; i++) {
            assertEquals(firstPlayerHand.get(i).getValue(), splitDecks.get(0).get(i).getValue());
        }
    }

    @Test
    public void testCreatePlayers() {
        ArrayList<ArrayList<Card>> hands = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            ArrayList<Card> hand = new ArrayList<>();
            hand.add(new Card(i));
            hands.add(hand);
        }
        ArrayList<Player> players = new ArrayList<>();
        CardGame.createPlayers(hands, 2, players);

        // Verify correct number of players
        assertEquals(2, players.size());
        // Verify player 1's hand
        assertEquals(1, players.get(0).getPlayerHand().get(0).getValue());
    }

    @Test
    public void testCreateDecks() {
        ArrayList<ArrayList<Card>> hands = new ArrayList<>();
        for (int i = 01; i < 5; i++) {
            ArrayList<Card> hand = new ArrayList<>();
            hand.add(new Card(i));
            hands.add(hand);
        }
        ArrayList<Deck> decks = new ArrayList<>();
        CardGame.createDecks(hands, 2, decks);

        // Verify correct number of decks
        assertEquals(2, decks.size());
        // Verify deck 1's card
        assertEquals(3, decks.get(0).getDeckCards().get(0).getValue());
    }

    // Gives a player a winning hand and tests if that player wins
    @Test
    public void testCheckInitialWinTrue() {
        ArrayList <Card> cards1 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            cards1.add(new Card(1));
        }
        ArrayList <Card> cards2 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            cards2.add(new Card(i));
        }
        Player player1 = new Player(1, cards1);
        Player player2 = new Player(2, cards2);
        ArrayList <Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        CardGame.players = players;

        assertTrue(CardGame.checkInitialWin());
        assertEquals(1, Player.getWinner());

    }

    // 
    @Test
    public void testCheckInitialWinFalse() {
        ArrayList <Card> cards1 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            cards1.add(new Card(i));
        }
        ArrayList <Card> cards2 = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            cards2.add(new Card(i));
        }
        Player player1 = new Player(1, cards1);
        Player player2 = new Player(2, cards2);
        ArrayList <Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        CardGame.players = players;

        assertFalse(CardGame.checkInitialWin());
        assertEquals(-1, Player.getWinner());
    }

    @Test
    public void testInputNumPlayers() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(3, CardGame.inputNumPlayers());
    }

    @Test
    public void testInputNumPlayersInvalid() {
        String input = "a\n1\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals(3, CardGame.inputNumPlayers());
    }

    @Test
    public void testInputFilename() {
        String input = "TestPacks/Valid4PlayerPack.txt\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals("TestPacks/Valid4PlayerPack.txt", CardGame.inputFilename(4));
    }

    @Test
    public void testInputFilenameInvalid() {
        String input = "Not_Real_File.txt\nTestPacks/Valid4PlayerPack.txt\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        assertEquals("TestPacks/Valid4PlayerPack.txt", CardGame.inputFilename(4));
    }

    @Test
    public void testSetUpGame() {
        ArrayList <ArrayList <Card>> hands = new ArrayList<>();
        for (int i = 0; i < 5 ; i++) {
            ArrayList <Card> hand = new ArrayList<>();
            for (int j = 1; j < 5; j++) {
                hand.add(new Card(j));
            }
            hands.add(hand);
        }
    
        CardGame.setUpGame(2, hands);
    
        assertEquals(2, CardGame.players.size());
        assertEquals(2, CardGame.decks.size());
    
        for (int k = 0; k < 2; k++) {
            assertEquals(CardGame.decks.get((k + 1) % 2), CardGame.players.get(k).getDeckToPassTo());
            assertEquals(CardGame.decks.get(k), CardGame.players.get(k).getDeckToDrawFrom());
        }
    }

    // Uses a pack where player 1 should  win and checks if player 1 wins
    @Test
    public void testPlayerOneWins() {
        CardGame.newGame(2, "TestPacks/Valid2PlayerPack_Player1Wins.txt");
        assertEquals(1, Player.getWinner());
    }

    @Test 
    public void testNoPlayerWins(){
        CardGame.newGame(4,"TestPacks/4PlayerPackNoWinner.txt");
        assertEquals(-1, Player.getWinner());
    }


}
