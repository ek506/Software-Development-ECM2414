

import java.util.ArrayList;
import java.io.*;

/**
 * Represents a player in the game. Each player has a hand of cards, a number, and a deck to pass to and draw from
 */
public class Player implements Runnable {
    /**
     * The player's number
     */
    private int playerNumber;
    /**
     * The player's hand as a list of cards
     */
    private ArrayList<Card> playerHand = new ArrayList<Card>();
    /**
     * The deck object to pass a card to that is on the players right
     */
    private Deck deckToPassTo;
    /**
     * The deck object to draw a card from that is on the players left
     */
    private Deck deckToDrawFrom;
    /**
     * The card value that the player prefers to keep. Same as player number
     */
    private int preferedCard;
    /**
     * The name of the output file to write to
     */
    private String filename;
    /**
     * Whether the game is over. Volatile so threads see change immediately. Otherwise threads may cache the value and not see the change.
     */
    private static volatile boolean gameOver = false; 
    /**
     * The player number of the winner of the game. -1 if no one has won yet
     */               
    private static volatile int winner = -1;
    /**
     * The number of turns the player has played
     */
    private int numTurns = 0;

    /**
     * Creates a player with a player number and a hand of cards. Sets the file to ouput to and prints the initial hand to the file
     * @param playerNumber the player's number
     * @param playerHand the player's hand
     */
    public Player(int playerNumber, ArrayList<Card> playerHand){
        this.playerNumber = playerNumber;
        this.playerHand = playerHand;
        this.preferedCard = playerNumber;
        this.filename = "player" + playerNumber + "_output.txt";
        writeToFile("Player " + playerNumber + " initial hand " + showHand() + "\n", false);
    }

    /**
     * Resets the gameOver variable to false
     */
    public static void resetGameOver() {
        gameOver = false;
    }
    
    /**
     * Resets the winner variable to -1
     */
    public static void resetWinner() {
        winner = -1;
    }

    /**
     * Sets the deck to pass to to the deck object that is passed in
     * @param deckToPassTo deck object to players right
     */
    public void setDecktoPassTo (Deck deckToPassTo){
        this.deckToPassTo = deckToPassTo;
    }

    /**
     * Sets the deck to draw from to the deck object that is passed in
     * @param deckToDrawFrom deck object to players left
     */
    public void setDeckToDrawFrom (Deck deckToDrawFrom){
        this.deckToDrawFrom = deckToDrawFrom;
    }

    /**
     * Gets the player's hand and returns it
     * @return playerHand The list of cards the player has
     */
    public ArrayList<Card> getPlayerHand (){
        return playerHand;
    }

    /**
     * Returns the player's prefered card value
     * @return the card value that the player prefers to keep
     */
    public int getPlayerNumber(){
        return playerNumber;
    }

    /**
     * Returns the deck object that the player passes to
     * @return the deck object that the player passes to
     */
    public Deck getDeckToPassTo(){
        return deckToPassTo;
    }

    /**
     * Returns the deck object that the player draws from
     * @return the deck object that the player draws from
     */
    public Deck getDeckToDrawFrom(){
        return deckToDrawFrom;
    }

    /**
     * Returns the gameOver variable stating whether the game has ended
     * @return whether the game has ended
     */
    public static boolean isGameOver() {
        return gameOver;
    }

    /**
     * Returns the winner variable stating the player number of the winner
     * @return the player number of the winner
     */
    public static int getWinner() {
        return winner;
    }

    /**
     * Draws a card from the deck to the player's left and adds it to the player's hand. Prints the card drawn to the file
     * @throws IllegalStateException if the deck is empty
     */
    public void drawCard() throws IllegalStateException {
        try{
            Card drawnCard = deckToDrawFrom.removeCard(); 
            playerHand.add(drawnCard);
            String message = "player " + playerNumber + " draws a " + drawnCard.getValue() + " from deck " + deckToDrawFrom.getDeckNumber() +"\n";
            writeToFile(message, true);
        } catch (IllegalStateException e){
            throw e;
        }
    }

    /**
     * Removes a card from the player's hand and adds it to the deck to the player's right. Prints the card passed to the file
     * @param cardToPass the card object that is passed
     */
    public void passCard(Card cardToPass){
        playerHand.remove(cardToPass);
        deckToPassTo.addCard(cardToPass);
        String message = "player " + playerNumber + " discards a " + cardToPass.getValue() + " to deck " + deckToPassTo.getDeckNumber() + "\n";    
        writeToFile(message, true);
    }

    /**
     * Selects the first card from the players hand that is not the player's prefered card
     * @return the card object that is to be passed
     */
    public Card decideCardToPass(){
        for (Card c: playerHand){
            if (c.getValue() != preferedCard){
                return c;
            } 
        }
        return null;
    } 

    /**
     * Returns the player's hand as a string of card values
     * @return the player's hand as a string of card values
     */
    public String showHand(){
        String cards = "";
        for (Card c: playerHand){
            cards += c.getValue() + " ";
        }
        return cards;
    }

    /**
     * Checks if the player has won by checking if all the cards in the player's hand are the same
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(){
        int firstCard = playerHand.get(0).getValue();
        boolean win = true;
        for (Card c: playerHand){
            if (c.getValue() == firstCard){
                continue;
            } else {
                win = false;
                return win;
            }
        }
        return win;
    }

    /**
     * The player's complete turn as an atomic action. 
     * Draws a card, decides which card to pass, passes the card, and checks if the player has won.
     * If the deck is empty, the player thread sleeps for 1 second and tries again.
     * If the player has won, the win() method is run
     */
    public void playerTurn() {
        try{
            drawCard();
            Card cardToPass= decideCardToPass();
            passCard(cardToPass);
            String message = "player " + playerNumber + " current hand is " + showHand() + "\n";
            writeToFile(message, true);
            if (checkWin()){
                win();
            }
            numTurns++;
        } catch (IllegalStateException e) {
            //System.out.println("Deck is empty");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /**
     * The run method for the player thread. Runs the playerTurn method until the game is over or the player has played the max number of turns
     * If the player has played the max number of turns, the player thread prints a message to the file and returns
     * If the player has not won when the game ends, the player thread prints a message to the player output file and returns
     */
    public void run() {
        int maxTurns = 8 * CardGame.getNumPlayers();
        while (!gameOver) {
            if (numTurns > maxTurns) {
                System.out.println("Player " + playerNumber + " has played " + maxTurns + " turns and has not won");
                return;
            } else {
                playerTurn();
            }
        }
        if (winner != playerNumber) { 
            printOtherPlayerWins();
        }
    }
        
    /**
     * Prints a message to the file stating that the player has won and exits the player thread
     */
    public void win() {
        gameOver = true;
        winner = playerNumber;
        System.out.println("player " + playerNumber + " wins");
        String message = "player " + playerNumber + " wins" + "\n" + 
                        "player "  + playerNumber + " exits " + "\n" +
                        "player " + playerNumber + " final hand: " + showHand() + "\n";
        writeToFile(message, true);
    }
    
    /**
     * Prints a message that is passed in, to the output file of the player 
     * @param message the message to be printed to the file
     * @param append true to append to the file, false to overwrite it
     */
    public void writeToFile(String message, boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }
    }

    /**
     * Prints the message that another player has won, the player exits and the players final hand to the player's output file
     */
    public void printOtherPlayerWins() {
        String message = ("player " + winner + " has informed player " + playerNumber + " that player " + winner + " has won\n"
                        + "player " + playerNumber + " exits\n"
                        + "player " + playerNumber + " hand: " + showHand() + "\n");
        writeToFile(message, true);
    }
}
