import java.util.ArrayList;
import java.io.*;

public class Player {
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

    // Takes from deck and adds to hand
    public void drawCard(){
        Card drawnCard = deckToDrawFrom.drawCard(); 
        playerHand.add(drawnCard);
        String message = "player " + playerNumber + " draws a " + drawnCard.getValue() + " from deck " + deckToDrawFrom.getDeckNumber() + "\n";
        writeToFile(message, true);
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
        //select the first card in the hand
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
        //checkWin();
        //drawCard();
        //decideCardToPass();
        //passCard();
        //checkWin();

        drawCard();
        Card cardToPass= decideCardToPass();
        passCard(cardToPass);
        String message = "player " + playerNumber + " current hand is " + showHand() + "\n";
        writeToFile(message, true);
        if (checkWin()){
            System.out.println("Player " + playerNumber + " wins!");
            win();
            //Declare win

        }
    }

    public void win() {
        //tell other threads

        String message = "player " + playerNumber + " wins" + "\n" + 
                        "player"  + playerNumber + " exits " + "\n" +
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

    public String toString(){
        return "Player " + playerNumber + " has hand: " + showHand() + 
        "\n    " + "draws from " + deckToDrawFrom.toString() + "\n    " + "passes to " + deckToPassTo.toString() ;
    }


}
