import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Collections;

/**
 * A Star algorithm for Bear Map.
 * Author: Haowen Wang
 */
public class AStar {
    private final PriorityQueue<ANode> fringe = new PriorityQueue<>(new ANodeComparator());
    private final Map<Long, Double> distTo = new HashMap<>();
    private final Map<Long, Long> edgeTo = new HashMap<>();
    private final Set<Long> marked = new HashSet<>();
    private final List<Long> shortestPath = new LinkedList<>();
    private final GraphDB g;
    private final long startID;
    private final long endID;

    AStar(GraphDB g, long startID, long endID) {
        this.g = g;
        this.startID = startID;
        this.endID = endID;
        this.distTo.put(startID, 0.0);
        this.edgeTo.put(startID, -1L);
    }

    private double h(long id) {
        return g.distance(id, this.endID);
    }

    private void pathHelper() {
        ANode curr = new ANode(startID);
        this.marked.add(startID);
        this.fringe.add(curr);

        while (!fringe.isEmpty()) {
            curr = fringe.poll();
            this.marked.add(curr.id);
            if (curr.id == this.endID) {
                return;
            }
            for (long adjID: this.g.adjacent(curr.id)) {
                if (!this.marked.contains(adjID)) {
                    // relaxing an edge if needed
                    if (!distTo.containsKey(adjID) || distTo.get(curr.id)
                            + g.distance(adjID, curr.id) < distTo.get(adjID)) {
                        this.distTo.put(adjID, distTo.get(curr.id) + g.distance(adjID, curr.id));
                        this.edgeTo.put(adjID, curr.id);
                        this.fringe.add(new ANode(adjID));
                    }
                }
            }
        }
    }

    private void recallPath(long id) {
        if (id < 0) {
            return;
        }
        this.shortestPath.add(id);
        recallPath(this.edgeTo.get(id));
    }

    List<Long> getPath() {
        pathHelper();
        recallPath(this.endID);
        Collections.reverse(this.shortestPath);
        return this.shortestPath;
    }

    private static class ANodeComparator implements Comparator<ANode> {
        @Override
        public int compare(ANode n1, ANode n2) {
            if (n1.priority - n2.priority < 0) {
                return -1;
            } else if (n1.priority - n2.priority > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class ANode {
        private final long id;
        private final double priority;

        ANode(long id) {
            this.id = id;
            this.priority = distTo.get(id) + h(id);
        }
    }
}
