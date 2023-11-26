package cardgame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.InputStreamReader;

/**
 * Represents a single card game between multiple players. Each game has a list of players and a list of decks
 */
public class CardGame {
    /**
     * The list of players in the game
     */
    static ArrayList<Player> players = new ArrayList<Player>();
    /**
     * The list of decks that are between players in the game
     */
    static ArrayList<Deck> decks = new ArrayList<Deck>();

    public static void main(String[] args) {
        try{    
            int numPlayers = inputNumPlayers();
            String filename = inputFilename(numPlayers);
            newGame(numPlayers, filename);
        } catch (IllegalArgumentException e) {
            System.out.println("Too Many Invalid Inputs, Game Not Started");
        }
    }

    /**
     * Creates a new game by asking user for number of players and the location of the pack to load and then setting up and ruunning the game
     * Prints the winner of the game to the console
     * 
     */
    public static void newGame(int numPlayers, String filename) {
        ArrayList<Card> cardArray = readCards(filename);  // List of cards
        ArrayList<ArrayList<Card>> splitDeck = splitDeck(cardArray, numPlayers); //List of hands
        setUpGame(numPlayers, splitDeck);

        //If win with starting hand, end game
        if (checkInitialWin()) {  
            for (Player p: players) {
                if (!p.checkWin()) {
                    p.printOtherPlayerWins();
                }
            }
            endGame();
            return;
        }

        // Start player threads
        ArrayList<Thread> threads = new ArrayList<>();
        for (Player p: players) {
            Thread playerThread = new Thread(p);        
            playerThread.start();
            threads.add(playerThread);
        }
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
        //Once all threads are done, end game
        endGame();


    }

    /**
     * Checks if any player has won with their starting hand
     * If a player has won, runs win() method for that player
     * @return true if any player has won, false otherwise
     */
    public static boolean checkInitialWin() {
        for (Player p: players) {
            if (p.checkWin()) {
                p.win();
                return true;
            }
        }
        return false;
    }

    /**
     * Asks the user for the number of players and returns the number
     * Loops until a valid number is entered
     * @return the number of players
     */
    public static int inputNumPlayers() throws IllegalArgumentException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the number of players: ");
        int numPlayers = 0;
        int i = 0;
        while (i < 10) {
            try {
                numPlayers = Integer.parseInt(reader.readLine());
                if (!isValidNumPlayers(numPlayers)) {
                    System.out.println("Invalid number of players, please enter a valid number:");
                    i++;
                } else {
                    break;
                }
            } catch (NumberFormatException | IOException e) {
                System.out.println("Invalid input, please enter a valid number:");
                i++;
            }
        }
        if (i == 10) {
            throw new IllegalArgumentException("No number of players entered");
        }
        System.out.println("Number of players: " + numPlayers);
        return numPlayers;
    }

    /**
     * Asks the user for the location of the pack to load and returns the filename
     * Loops until a valid pack is entered
     * @param numPlayers
     * @return
     */
    public static String inputFilename(int numPlayers) throws IllegalArgumentException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter location of pack to load: ");
        String filename = "";
        int i = 0;
        while (i < 10) {
            try {
                filename = reader.readLine();
                if (!isValidPack(filename, numPlayers)) {
                    System.out.println("Invalid pack, please enter a valid pack:");
                    i++;
                } else {
                    break;
                }
            } catch (IOException e) {
                System.out.println("Invalid input, please enter a valid pack:");
                i++;
            }
        }
        if (i == 10) {
            throw new IllegalArgumentException("No filename entered");
        }
        System.out.println("Pack to load: " + filename);
        return filename;
    }

    /**
     * Checks if the number of players is valid. Must be greater than 1
     * @param numPlayers Number of players to be checked
     * @return true if the number of players is valid, false otherwise
     */
    public static boolean isValidNumPlayers(int numPlayers) {
        return numPlayers > 1;
    }

    /**
     * Checks if a pack stored at a given filename is valid.
     * A valid input pack is a plain text file, where each row contains a single non-negative integer value,
     * and has 8n rows
     * Prints File not found if the file passed in is not found
     * @param filename Name of the file to be checked
     * @param numPlayers Number of players in the game
     * @return true if the pack is valid, false otherwise
     */
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
                        System.out.println("Invalid card number: '" + line+"'");
                        return false;
                    }
                    if (card < 0) {
                        System.out.println("Card should not be negative: " + card);
                        return false;
                    }
                    lineCount++;
                }
                return lineCount % (8*numPlayers) == 0;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        }
    }

    /**
     * Reads the file and returns a list of card objects where each card object has a value from the file
     * Prints File not found if the file passed in is not found
     * Prints Invalid card number if a line in the file is not an integer
     * @param filename Name of the file to be read
     * @return List of card objects
     */
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

    /**
     * Takes a list of cards and splits it into a list of hands where each hand is a list of cards.
     * Deals the first half of the cards to the players' hands in a round robin fashion, 
     * and the second half to the decks also in a round robin fashion.
     * @param cardArray List of cards to be dealt
     * @param numPlayers Number of players to be dealt to
     * @return List of hands where each hand is a list of cards
     */
    public static ArrayList<ArrayList<Card>> splitDeck(ArrayList<Card> cardArray, int numPlayers) {
         // Create empty hands for each player and deck
        ArrayList<ArrayList<Card>> splitDeck = new ArrayList<ArrayList<Card>>();
        for (int i = 0; i < 2*numPlayers; i++) { 
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

    /**
     * Creates players and decks by calling createPlayers() and createDecks()
     * Assigns deckToPassTo and deckToDrawFrom for each player
     * @param numPlayers Number of players to be created
     * @param hands List of hands where each hand is a list of cards
     */
    public static void setUpGame(int numPlayers, ArrayList<ArrayList<Card>> hands) {
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

    /**
     * Creates the players using the hands passed in and adds them to the list of players
     * Takes the first half of the list of hands and creates players from them
     * @param hands List of hands where each hand is a list of cards
     * @param numPlayers Number of players to be created
     * @param players List of players to add player objects to
     */
    public static void createPlayers(ArrayList<ArrayList<Card>> hands, int numPlayers, ArrayList<Player> players) {
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player(i+1, hands.get(i));
            players.add(player);
        }
    }

    /**
     * Creates the decks using the hands passed in and adds them to the list of decks
     * Takes the second half of the list of hands and creates decks from them
     * @param hands List of hands where each hand is a list of cards
     * @param numPlayers Number of decks to be created (should be same as number of players)
     * @param decks List of decks to add deck objects to
     */
    public static void createDecks(ArrayList<ArrayList<Card>> hands, int numPlayers, ArrayList<Deck> decks) {
        for (int i = 0; i < numPlayers; i++) {
            Deck deck = new Deck(i+1, hands.get(i + numPlayers));
            decks.add(deck);
        }
    }

    /**
     * Makes all the decks print their contents to their respective files
     */
    public static void endGame() {
        for (Deck d: decks){
            d.writeToFile();
        }
    }

    /**
     * Returns the number of players in the game
     * @return the number of players in the game
     */
    public static int getNumPlayers() {
        return players.size();
    }
}


