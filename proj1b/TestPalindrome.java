import org.junit.Test;
import static org.junit.Assert.*;

public class TestPalindrome {
    // You must use this palindrome, and not instantiate
    // new Palindromes, or the autograder might be upset.
    static Palindrome palindrome = new Palindrome();

    @Test
    public void testWordToDeque() {
        Deque d = palindrome.wordToDeque("persiflage");
        String actual = "";
        for (int i = 0; i < "persiflage".length(); i++) {
            actual += d.removeFirst();
        }
        assertEquals("persiflage", actual);
    }

    @Test
    public void testIsPalindrome() {
        assertTrue(palindrome.isPalindrome("")); // size = 0
        assertTrue(palindrome.isPalindrome("a")); // size = 1
        assertTrue(palindrome.isPalindrome("mom")); // size = single
        assertTrue(palindrome.isPalindrome("noon")); // size = double

        assertFalse(palindrome.isPalindrome("vincent")); // size = single
        assertFalse(palindrome.isPalindrome("aaabaa")); // size = double
    }

    @Test
    public void testIsPalindromeCC() {
        CharacterComparator obo = new OffByOne();

        assertTrue(palindrome.isPalindrome("", obo)); // size = 0
        assertTrue(palindrome.isPalindrome("a", obo)); // size = 1
        assertTrue(palindrome.isPalindrome("oap", obo)); // size = single
        assertTrue(palindrome.isPalindrome("adcb", obo)); // size = double
        assertTrue(palindrome.isPalindrome("flake", obo));

        assertFalse(palindrome.isPalindrome("aba", obo));
        assertFalse(palindrome.isPalindrome("noon", obo));
        assertFalse(palindrome.isPalindrome("abdc", obo));
    }
}
