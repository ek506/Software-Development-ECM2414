# Software-Development-ECM2414

### CardGAmeTestSuite.java runs the following test classes: 
    TestDeck.class,
    TestPlayer.class,
    TestCard.class,
    TestCardGame.class

Each test file contains unit tests for the methods of their respective source code file.
The folder 'TestPacks' contains files that are used as test packs in some of the unit tests so it is required to run the suite.

Uses Junit 4

<<<<<<< HEAD
Path to junit: ./lib/junit-4.13.2.jar
Path to hamcrest-core: ./lib/hamcrest-core-1.3.jar
=======
### Note:
Do not use card pack files that contain only winning combinatioms of numbers that do not share a plager mumber.
For example, if a pack has only one winning combination "8 8 8 8", but you only have players 1-4, then there will be nk winner.
This is because each player prioritises their own player number when collecting cards, and all other cards are discarded.
The only exception to this is if a player is dealt 4 identical cards from the start.
>>>>>>> fabda6d5711c0732e68de88f8fe14e1109c0f134
