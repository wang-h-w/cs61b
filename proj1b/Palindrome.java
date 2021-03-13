import java.util.Collections;

public class Palindrome {
    /** Given a String, wordToDeque should return a Deque
     *  where the characters appear in the same order as
     *  in the String. */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> d = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i += 1) {
            d.addLast(word.charAt(i));
        }
        return d;
    }

    /** Helper method for recursion approach of isPalindrome. */
    private boolean helperIsPalindrome(Deque<Character> d) {
        Character first = d.removeFirst();
        Character last = d.removeLast();
        if (first == null || last == null) {
            return true;
        }
        if (!first.equals(last)) {
            return false;
        }
        return helperIsPalindrome(d);
    }

    /** Return true if the given word is a palindrome,
     *  and false otherwise. */
    public boolean isPalindrome(String word) {
        Palindrome p = new Palindrome();
        Deque<Character> d = p.wordToDeque(word);
        return helperIsPalindrome(d);
    }

    /** Helper method for recursion approach of isPalindrome. */
    private boolean helperIsPalindromeCC(Deque<Character> d, CharacterComparator cc) {
        Character first = d.removeFirst();
        Character last = d.removeLast();
        if (first == null || last == null) {
            return true;
        }
        if (!cc.equalChars(first, last)) {
            return false;
        }
        return helperIsPalindromeCC(d, cc);
    }

    /** Return true if the word is a palindrome according
     *  to the character comparison test provided by the
     *  CharacterComparator passed in as argument cc. */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Palindrome p = new Palindrome();
        Deque<Character> d = p.wordToDeque(word);
        return helperIsPalindromeCC(d, cc);
    }
}
