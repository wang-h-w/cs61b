import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MyTrieSet implements TrieSet61B {

    private static final int R = 256; // ASCII
    private Node root;
    private List<String> x;

    private static class Node {
        private char ch;
        private boolean isKey;
        private Map<Character, Node> map;

        private Node(char c, boolean b) {
            ch = c;
            isKey = b;
            map = new HashMap<>();
        }
    }

    public MyTrieSet() {
        clear();
    }

    @Override
    public void clear() {
        root = new Node('r', false);
    }

    @Override
    public boolean contains(String key) {
        Node curr = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                return false;
            }
            curr = curr.map.get(c);
        }
        return curr.isKey;
    }

    @Override
    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        x = new ArrayList<>();
        Node curr = root;
        for (int i = 0, n = prefix.length(); i < n; i++) {
            char c = prefix.charAt(i);
            if (!curr.map.containsKey(c)) {
                return null;
            }
            curr = curr.map.get(c);
        }
        preHelp(prefix, curr);
        return x;
    }

    private void preHelp(String s, Node n) {
        if (n.isKey) {
            x.add(s);
        }
        for (Character k: n.map.keySet()) {
            preHelp(s+k, n.map.get(k));
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }
}
