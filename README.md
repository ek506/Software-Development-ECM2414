# Software-Development-ECM2414

### CardGameTestSuite.java runs the following test classes: 
    TestDeck.class,
    TestPlayer.class,
    TestCard.class,
    TestCardGame.class

Each test file contains unit tests for the methods of their respective source code file.
The folder 'TestPacks' contains files that are used as test packs in some of the unit tests so it is required to run the suite.

Uses Junit 4

Path to junit: ./lib/junit-4.13.2.jar
Path to hamcrest-core: ./lib/hamcrest-core-1.3.jar

### Note:
If a card pack file doesn't contain at least four cards that have the value as a players number then no players will win and the game will terminate without a winner and a message will print to the console.
For example, if a pack has only one winning combination "8 8 8 8", but you only have players 1-4, then there will be no winner.
This is because each player prioritises their own player number when collecting cards, and all other cards are discarded.
The only exception to this is if a player is dealt 4 identical cards from the start.