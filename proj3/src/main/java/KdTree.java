import java.util.Objects;

/**
 * Kd Tree for Bear Map.
 * Support operations: add, nearest, isEmpty, size.
 * Author: Haowen Wang
 */

public class KdTree {
    private KdNode root;
    private int size;

    private static class KdNode implements Comparable<KdNode> {
        private final GraphDB.Node center;
        private KdNode prev;
        private KdNode left;
        private KdNode right;
        private boolean useLon;

        KdNode(GraphDB.Node n, boolean useLeft) {
            this.center = n;
            this.prev = null;
            this.left = null;
            this.right = null;
            this.useLon = useLeft;
        }

        public double distanceTo(KdNode k) {
            if (k == null) {
                return java.lang.Double.MAX_VALUE;
            }
            return GraphDB.distance(this.center.lon, this.center.lat, k.center.lon, k.center.lat);
        }

        @Override
        public int compareTo(KdNode k) {
            if (this.center.lon == k.center.lon && this.center.lat == k.center.lat) {
                return 0;
            } else if ((k.useLon && this.center.lon < k.center.lon)
                    || (!k.useLon && this.center.lat < k.center.lat)) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    private KdNode addRecursive(KdNode existNode, KdNode newNode, boolean addLeft) {
        if (existNode == null) {
            size += 1;
            if (newNode.prev != null) {
                if (addLeft) {
                    newNode.prev.left = newNode;
                } else {
                    newNode.prev.right = newNode;
                }
                newNode.useLon = !newNode.prev.useLon;
                return newNode;
            }
            return newNode;
        }
        switch (newNode.compareTo(existNode)) {
            case 0:
                existNode.center.id = newNode.center.id;
                existNode.center.extraInfo = newNode.center.extraInfo;
                return existNode;
            case -1:
                newNode.prev = existNode;
                return Objects.requireNonNull(addRecursive(existNode.left, newNode, true)).prev;
            case 1:
                newNode.prev = existNode;
                return Objects.requireNonNull(addRecursive(existNode.right, newNode, false)).prev;
            default:
                return null;
        }
    }

    public KdNode createKdNode(GraphDB.Node node) {
        return new KdNode(node, true);
    }

    public void add(GraphDB.Node node) {
        KdNode newNode = createKdNode(node);  // useLeft might be modified
        this.root = addRecursive(root, newNode, false);
    }

    private boolean bestDistPossible(KdNode parent, KdNode goal, KdNode best) {
        if (parent.useLon) {
            return Math.abs(parent.center.lon - goal.center.lon) < best.distanceTo(goal);
        } else {
            return Math.abs(parent.center.lat - goal.center.lat) < best.distanceTo(goal);
        }
    }

    private KdNode nearestHelper(KdNode n, KdNode goal, KdNode best) {
        if (n == null) {
            return best;
        }
        if (goal.distanceTo(n) < goal.distanceTo(best)) {
            best = n;
        }

        KdNode goodSide;
        KdNode badSide;
        if (goal.compareTo(n) < 0) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }

        best = nearestHelper(goodSide, goal, best);
        if (bestDistPossible(n, goal, best)) {
            best = nearestHelper(badSide, goal, best);
        }

        return best;
    }

    public GraphDB.Node nearest(GraphDB.Node ref) {
        KdNode goal = createKdNode(ref);
        KdNode near = nearestHelper(root, goal, null);
        return near.center;
    }

    public static void main(String[] args) {
        GraphDB.Node n1 = new GraphDB.Node(1, 2, 3);
        GraphDB.Node n2 = new GraphDB.Node(2, 4, 2);
        GraphDB.Node n3 = new GraphDB.Node(3, 4, 2);
        GraphDB.Node n4 = new GraphDB.Node(4, 4, 5);
        GraphDB.Node n5 = new GraphDB.Node(5, 3, 3);
        GraphDB.Node n6 = new GraphDB.Node(6, 1, 5);
        GraphDB.Node n7 = new GraphDB.Node(7, 4, 4);
        GraphDB.Node ref = new GraphDB.Node(-1, 0, 7);

        KdTree tree = new KdTree();
        System.out.println(tree.isEmpty());
        tree.add(n1);
        tree.add(n2);
        tree.add(n3);
        tree.add(n4);
        tree.add(n5);
        tree.add(n6);
        tree.add(n7);
        GraphDB.Node near = tree.nearest(ref);
        System.out.println(near.lon);
        System.out.println(near.lat);
    }
}
