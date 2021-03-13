import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(3);

    @Test
    public void testOffByOne() {
        assertTrue(offByN.equalChars('a', 'd'));
        assertTrue(offByN.equalChars('i', 'f'));
        assertTrue(offByN.equalChars('&', ')')); // special case

        assertFalse(offByN.equalChars('a', 'c'));
        assertFalse(offByN.equalChars('q', 'q'));
        assertFalse(offByN.equalChars('a', 'A')); // special case
    }
}
