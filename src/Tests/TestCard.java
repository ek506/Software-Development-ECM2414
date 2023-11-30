import org.junit.Test;

import static org.junit.Assert.*;


public class TestCard {
    @Test
    public void testConstructorAndGetValue() {
        int value = 5;
        Card card = new Card(value);
        assertEquals(value, card.getValue());
    }
}