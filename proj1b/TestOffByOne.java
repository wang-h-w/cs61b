import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByOne {
    // You must use this CharacterComparator and not instantiate
    // new ones, or the autograder might be upset.
    static CharacterComparator offByOne = new OffByOne();

    // Your tests go here.
    @Test
    public void testOffByOne() {
        assertTrue(offByOne.equalChars('a', 'b'));
        assertTrue(offByOne.equalChars('g', 'f'));
        assertTrue(offByOne.equalChars('&', '%')); // special case

        assertFalse(offByOne.equalChars('a', 'c'));
        assertFalse(offByOne.equalChars('q', 'd'));
        assertFalse(offByOne.equalChars('a', 'A')); // special case
    }
}
