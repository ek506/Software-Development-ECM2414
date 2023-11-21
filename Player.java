import java.util.ArrayList;
import java.io.*;

public class Player implements Runnable {
    // Implement the card class with thread-safe operations

    //  Methods
    //
    //Draw card
    //Select card to dicard (check deck not empty)
    //Discard card
    //Play turn (draw and discard atomically)
    //Check win
    //Declare win
    
    //Small Deck as subclass
    
    private int playerNumber;
    private ArrayList<Card> playerHand = new ArrayList<Card>();
    private Deck deckToPassTo;
    private Deck deckToDrawFrom;
    private int preferedCard;
    private String filename;
    private static volatile boolean gameOver = false;               //Volatile so it changes the threads see the change immediately. Otherwise the threads may cache the value and not see the change. 
    private static volatile int winner = -1;
    private int numTurns = 0;

    public Player(int playerNumber, ArrayList<Card> playerHand){
        this.playerNumber = playerNumber;
        this.playerHand = playerHand;
        this.preferedCard = playerNumber;
        this.filename = "player" + playerNumber + "_output.txt";
        //write the first line of the file
        writeToFile("Player " + playerNumber + " initial hand " + showHand() + "\n", false);
    }

    public void setDecktoPassTo (Deck deckToPassTo){
        this.deckToPassTo = deckToPassTo;
    }

    public void setDeckToDrawFrom (Deck deckToDrawFrom){
        this.deckToDrawFrom = deckToDrawFrom;
    }

    public ArrayList<Card> getPlayerHand (){
        return playerHand;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public Deck getDeckToPassTo(){
        return deckToPassTo;
    }

    public Deck getDeckToDrawFrom(){
        return deckToDrawFrom;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static int getWinner() {
        return winner;
    }

    // Takes from deck and adds to hand
    // Will have to check if deck is empty 
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

    //Remove from hand and add to deck
    public void passCard(Card cardToPass){
        playerHand.remove(cardToPass);
        deckToPassTo.addCard(cardToPass);
        String message = "player " + playerNumber + " discards a " + cardToPass.getValue() + " to deck " + deckToPassTo.getDeckNumber() + "\n";    
        writeToFile(message, true);
    }

    public Card decideCardToPass(){
        for (Card c: playerHand){
            if (c.getValue() != preferedCard){
                return c;
            } 
        }
        return null;
    } 

    //Rename to toString
    // Return list of card integers
    public String showHand(){
        String cards = "";
        for (Card c: playerHand){
            cards += c.getValue() + " ";
        }
        return cards;
    }

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
            System.out.println("Deck is empty");
            try {
                Thread.sleep(1000); // make the thread sleep for 1 second
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

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
        



    public void win() {
        //tell other thread
        gameOver = true;
        winner = playerNumber;
        System.out.println("player " + playerNumber + " wins");
        String message = "player " + playerNumber + " wins" + "\n" + 
                        "player "  + playerNumber + " exits " + "\n" +
                        "player " + playerNumber + " final hand: " + showHand() + "\n";
        writeToFile(message, true);
    }
    
    public void writeToFile(String message, boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, append));
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + filename);
        }
    }

    // public String toString(){
    //     return "Player " + playerNumber + " has hand: " + showHand() + 
    //     "\n    " + "draws from " + deckToDrawFrom.getDeckNumber() + "\n    " + "passes to " + deckToPassTo.getDeckNumber() + "\n";
    // }

    public void printOtherPlayerWins() {
        String message = ("player " + winner + " has informed player " + playerNumber + " that player " + winner + " has won\n"
                        + "player " + playerNumber + " exits\n"
                        + "player " + playerNumber + " hand: " + showHand() + "\n");
        writeToFile(message, true);
    }

}
