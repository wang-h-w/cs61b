public class LinkedListDeque<T> {
    private int size;
    private Node sentinel;

    /** Create an empty linked list deque. */
    public LinkedListDeque() {
        sentinel = new Node(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** Create a deep copy of other. */
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new Node(null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
        for (int i = 0; i < other.size(); i += 1) {
            addLast((T) other.get(i));
        }
    }

    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        sentinel.next = new Node(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
        if (size == 1) {
            sentinel.prev = sentinel.next;
        }
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        sentinel.prev = new Node(sentinel.prev, item, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return sentinel.next == sentinel & sentinel.prev == sentinel;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last,
     *  separated by a space. Once all the items have been printed,
     *  print out a new line. */
    public void printDeque() {
        Node ptr = sentinel.next;
        for (int i = 1; i <= size; i++) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.
     *  If no such item exists, returns null. */
    public T removeFirst() {
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size -= 1;
        return sentinel.next.item;
    }

    /** Removes and returns the item at the back of the deque.
     *  If no such item exists, returns null. */
    public T removeLast() {
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return sentinel.prev.item;
    }

    /** Gets the item at the given index, where 0 is the front,
     *  1 is the next item, and so forth. If no such item exists,
     *  returns null. Must not alter the deque! */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        Node ptr = sentinel.next;
        for (int i = 0; i < index; i++) {
            ptr = ptr.next;
        }
        return ptr.item;
    }

    /** Gets the item at the given index, use recursion. */
    private T getRecursiveHelper(Node n, int index) {
        if (index == 0) {
            return n.item;
        } else {
            n = n.next;
            return getRecursiveHelper(n, index - 1);
        }
    }

    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    /** Create helper class: Node (Doubly Linked Lists) */
    private class Node {
        T item;
        Node prev;
        Node next;

        Node(Node p, T t, Node n) {
            prev = p;
            item = t;
            next = n;
        }

        Node(T t) {
            item = t;
        }
    }
}
