import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable> Queue<Queue<Item>> makeSingleItemQueues(
            Queue<Item> items) {
        Queue<Queue<Item>> queues = new Queue<>();
        for (Item item: items) {
            Queue<Item> q = new Queue<>();
            q.enqueue(item);
            queues.enqueue(q);
        }
        return queues;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> mergeQueues = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergeQueues.enqueue(getMin(q1, q2));
        }
        return mergeQueues;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(Queue<Item> items) {
        if (items.size() <= 1) {
            return items;
        }
        Queue<Item> itemsLeft = new Queue<>();
        Queue<Item> itemsRight = new Queue<>();
        int n = items.size();
        for (int i = 0; i < n; i++) {
            if (i < n / 2) {
                itemsLeft.enqueue(items.dequeue());
            } else {
                itemsRight.enqueue(items.dequeue());
            }
        }
        return mergeSortedQueues(mergeSort(itemsLeft), mergeSort(itemsRight));
    }

    private static <Item extends Comparable> void display(Queue<Item> items) {
        for (Item i: items) {
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Integer
        System.out.println("----------Testing Integer----------");
        Queue<Integer> nums = new Queue<>();
        nums.enqueue(3);
        nums.enqueue(1);
        nums.enqueue(8);
        nums.enqueue(9);
        nums.enqueue(1);
        nums.enqueue(-2);

        display(nums);
        Queue<Integer> numsSorted = mergeSort(nums);
        display(numsSorted);

        // String
        System.out.println("----------Testing String----------");
        Queue<String> s = new Queue<>();
        s.enqueue("Vincent");
        s.enqueue("Is");
        s.enqueue("The");
        s.enqueue("Best");
        s.enqueue("Guy");

        display(s);
        Queue<String> sSorted = mergeSort(s);
        display(sSorted);
    }
}
