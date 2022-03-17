package lab11.graphs;
import java.util.Comparator;
import edu.princeton.cs.algs4.MinPQ;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private Maze maze;
    private MinPQ<Node> fringe;

    private class Node {
        private int v;
        private int priority;

        Node(int v) {
            this.v = v;
            this.priority = distTo[v] + h(v);
        }
    }

    private class NodeComparator implements Comparator<Node> {
        @Override
        public int compare(Node v1, Node v2) {
            return v1.priority - v2.priority;
        }
    }

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        fringe = new MinPQ<>(new NodeComparator());
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Performs an A star search from vertex s. */
    private void astar(int b) {
        Node curr = new Node(b);
        marked[b] = true;
        fringe.insert(curr);
        while (!fringe.isEmpty()) {
            announce();
            curr = fringe.delMin();
            if (curr.v == t) {
                return;
            }
            for (int w: maze.adj(curr.v)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = curr.v;
                    distTo[w] = distTo[curr.v] + 1;
                    announce();
                    if (w == t) {
                        return;
                    } else {
                        fringe.insert(new Node(w));
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

