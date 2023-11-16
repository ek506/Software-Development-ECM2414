import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    TestDeck.class,
    TestPlayer.class,
    TestCard.class
})
public class CardGameTestSuite {
    // This class remains empty, it is used only as a holder for the above annotations
}