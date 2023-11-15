import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class CardGame {
    static ArrayList<Player> players = new ArrayList<Player>();
    static ArrayList<Deck> decks = new ArrayList<Deck>();


    public static void main(String[] args) {
        // Read input and start the game
        // Initialize players, card decks, and threads
        // Handle game logic as specified
        System.out.println(isValidPack("testDeck.txt", 2));
        ArrayList<Card> cardArray = readCards("testDeck.txt");
        ArrayList<ArrayList<Card>> splitDeck = splitDeck(cardArray, 2);
        // for (Card c: cardArray){
        //     System.out.println(c.getValue());
        // }
        // ArrayList<ArrayList<Card>> splitDeck = splitDeck(cardArray, 4);
        // for (ArrayList<Card> a: splitDeck){
        //     System.out.println("New Deck");
        //     for (Card c: a){
        //         System.out.println(c.getValue());
        //     }
        // }
        setUpGame(2, splitDeck);
        for (int i=0; i<15; i++){
            players.get(0).playerTurn();
            players.get(1).playerTurn();
        }
        
        

    }

    // End Game

    //Check Valid Pack
    public static boolean isValidPack(String filename, int numPlayers) {
        try {
            File file = new File(filename);
            int lineCount = 0;
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int card;

                    try {
                        card = Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid card number: " + line);
                        return false;
                    }
                    
                    if (card < 0) {
                        System.out.println("Card should not be negative: " + card);
                        return false;
                    }
                    lineCount++;
                }
                //scanner.close();

                return lineCount % (8*numPlayers) == 0;
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        }
    }

    //Read Cards (Will read them even if card pack is invalid)
    // Returns a list of card objects from the file
    public static ArrayList<Card> readCards(String filename) {
        ArrayList<Card> cardArray = new ArrayList<Card>();
        try {
            File file = new File(filename);
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    int cardNumber;
                    try {
                        cardNumber = Integer.parseInt(line);
                        // Make card and add to array
                        Card card = new Card(cardNumber);
                        cardArray.add(card);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid card number: " + line);
                    }
                }
                return cardArray;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return cardArray;
        }
    }

    // Split Deck (Deal)
    //Returns a list of hands where each hand is a list of cards
    public static ArrayList<ArrayList<Card>> splitDeck(ArrayList<Card> cardArray, int numPlayers) {
        ArrayList<ArrayList<Card>> splitDeck = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < 2*numPlayers; i++) {  // Create empty hands for each player and deck
            splitDeck.add(new ArrayList<Card>());
        }

        // Distribute cards: first half of the deck goes to players' hands, second half to their decks
        for (int i = 0; i < cardArray.size(); i++) {
            int recipientIndex;
            if (i < cardArray.size() / 2) {  // The first half of the deck goes to the players' hands
                recipientIndex = i % numPlayers;
            } else {  // The second half goes to their decks
                recipientIndex = (i % numPlayers) + numPlayers;
            }

            // Add the card to the appropriate hand or deck
            splitDeck.get(recipientIndex).add(cardArray.get(i));
        }
        return splitDeck;
    }

    // Creates players and decks and assigns deckToPassTo and deckToDrawFrom
    public static void setUpGame(int numPlayers, ArrayList<ArrayList<Card>> hands) {

        //create players
        createPlayers(hands, numPlayers, players);   
        createDecks(hands, numPlayers, decks);
        

        //set deck to pass to
        for (int i = 0; i < numPlayers; i++) {
            int deckToPassToIndex = (i + 1) % numPlayers;
            players.get(i).setDecktoPassTo(decks.get(deckToPassToIndex));
        }

        //set deck to draw from
        for (int i = 0; i < numPlayers; i++) {
            players.get(i).setDeckToDrawFrom(decks.get(i));
        }
        
    }

    public static void createPlayers(ArrayList<ArrayList<Card>> hands, int numPlayers, ArrayList<Player> players) {
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i+1, hands.get(i));
            players.add(player);
        }
    }

    public static void createDecks(ArrayList<ArrayList<Card>> hands, int numPlayers, ArrayList<Deck> decks) {
        for (int i = 0; i < numPlayers; i++) {
            Deck deck = new Deck(i+1, hands.get(i + numPlayers));
            decks.add(deck);
        }
    }
}



// LOOK AT INPUT AND OUTPUT