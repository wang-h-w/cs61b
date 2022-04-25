import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;

/**
 * Trie for Bear Map.
 * Author: Haowen Wang
 * Ref: https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/TrieST.java.html
 */
public class Trie {
    private int size;
    private Node root = new Node();

    private static class Node {
        private boolean isKey;
        private final Hashtable<Character, Node> links;

        Node() {
            this.isKey = false;
            this.links = new Hashtable<>();
        }
    }

    void add(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        } else {
            this.root = addRecursively(root, s, 0);
        }
    }

    /**
     * Return trie rooted at current node, start from s[place].
     */
    private Node addRecursively(Node n, String s, int place) {
        if (place == s.length()) {
            if (n.links.isEmpty()) {
                this.size += 1;
            }
            n.isKey = true;
            return n;
        }
        char curr = s.charAt(place);
        if (!n.links.containsKey(curr)) {
            n.links.put(curr, new Node());
        }
        n.links.put(curr, addRecursively(n.links.get(curr), s, place + 1));
        return n;
    }

    List<String> keyWithPrefix(String prefix) {
        List<String> results = new LinkedList<>();
        Node n = find(prefix);
        if (n == null) {
            return results;
        }
        collect(prefix, n, results);
        return results;
    }

    private Node find(String prefix) {
        return findRecursively(this.root, prefix, 0);
    }

    private Node findRecursively(Node n, String prefix, int place) {
        char c = prefix.charAt(place);
        if (!n.links.containsKey(c)) {
            return null;
        }
        if (place == prefix.length() - 1) {
            return n.links.get(c);
        }
        return findRecursively(n.links.get(c), prefix, place + 1);
    }

    private void collect(String store, Node n, List<String> results) {
        if (n.isKey) {
            results.add(store);
        }
        for (char c: n.links.keySet()) {
            collect(store + c, n.links.get(c), results);
        }
    }

    public int size() {
        return this.size;
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.add("wang");
        t.add("waw");
        t.add("zhang");
        t.add("z");
        t.add("wang");
        t.add("w");
        t.add("wanghaowen");
        Node n = t.find("wa");
        System.out.println(t.keyWithPrefix("w"));
    }
}
