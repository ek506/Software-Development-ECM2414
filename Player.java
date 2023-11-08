import java.util.ArrayList;

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


    public Player(int playerNumber, ArrayList<Card> playerHand){
        this.playerNumber = playerNumber;
        this.playerHand = playerHand;
        this.preferedCard = playerNumber;
    }

    public void setDecktoPassTo (Deck deckToPassTo){
        this.deckToPassTo = deckToPassTo;
    }

    public void setDeckToDrawFrom (Deck deckToDrawFrom){
        this.deckToDrawFrom = deckToDrawFrom;
    }

    // Takes from deck and adds to hand
    public void drawCard(){
        Card drawnCard = deckToDrawFrom.drawCard(); 
        playerHand.add(drawnCard);
    }

    //Remove from hand and add to deck
    public void passCard(Card cardToPass){
        playerHand.remove(cardToPass);
        deckToPassTo.addCard(cardToPass);
    }

    public Card decideCardToPass(){
        for (Card c: playerHand){
            if (c.getValue() != preferedCard){
                return c;
            } 
        }
        return null;
    } 

    public void playerTurn() {
        //checkWin();
        //drawCard();
        //decideCardToPass();
        //passCard();
        //checkWin();
    }
}