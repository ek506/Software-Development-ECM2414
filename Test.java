import java.util.ArrayList;

public class Test {

    //Test Individual Methods
    public static void main(String[] args) {
        test2();




    }
    

    public static void test1(){
        //Create a player with a hand of x cards
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            hand.add(new Card(1));
        }
        Player player1 = new Player(1, hand);

        //Create a deck with x cards
        ArrayList<Card> deckCards = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards.add(new Card(2));
        }
        Deck deck1 = new Deck(1, deckCards);

        //Create deck2 with x cards
        ArrayList<Card> deckCards2 = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards2.add(new Card(3));
        }
        Deck deck2 = new Deck(1, deckCards2);

        player1.setDeckToDrawFrom(deck1);
        player1.setDecktoPassTo(deck2);



        //Set Passing and Taking
        System.out.println("player1: " + player1.showHand());
        System.out.println("deck1: " + deck1.showDeck());
        System.out.println("deck2: " + deck2.showDeck());
        player1.drawCard();
        System.out.println("player1: " + player1.showHand());
        System.out.println("deck1: " + deck1.showDeck());
        System.out.println("deck2: " + deck2.showDeck());
        player1.passCard(player1.decideCardToPass());
        System.out.println("player1: " + player1.showHand());
        System.out.println("deck1: " + deck1.showDeck());
        System.out.println("deck2: " + deck2.showDeck());
        System.out.println("Player 1 wins: " + player1.checkWin());
    }


    public static void test2() {
        //Create a player with a hand of x cards
        ArrayList<Card> hand = new ArrayList<Card>();
        for (int i = 0; i < 3; i++){
            hand.add(new Card(1));
        }
        hand.add(new Card(4));
        Player player1 = new Player(1, hand);

        //Create a deck with x cards
        ArrayList<Card> deckCards = new ArrayList<Card>();
        deckCards.add(new Card(1));
        for (int i = 0; i < 3; i++){
            deckCards.add(new Card(2));
        }
        Deck deck1 = new Deck(1, deckCards);

        //Create deck2 with x cards
        ArrayList<Card> deckCards2 = new ArrayList<Card>();
        for (int i = 0; i < 4; i++){
            deckCards2.add(new Card(3));
        }
        Deck deck2 = new Deck(1, deckCards2);

        player1.setDeckToDrawFrom(deck1);
        player1.setDecktoPassTo(deck2);


        //Test Turn method
        System.out.println("player1: " + player1.showHand());
        System.out.println("deck1: " + deck1.showDeck());
        System.out.println("deck2: " + deck2.showDeck());
        player1.playerTurn();
        System.out.println("player1: " + player1.showHand());
        System.out.println("deck1: " + deck1.showDeck());
        System.out.println("deck2: " + deck2.showDeck());
    }

}
