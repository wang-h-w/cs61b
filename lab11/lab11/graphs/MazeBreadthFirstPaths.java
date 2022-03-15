package lab11.graphs;
import java.util.LinkedList;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private final int s;
    private final int t;
    private boolean targetFound = false;
    private final Maze maze;
    private LinkedList<Integer> queue;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
        queue = new LinkedList<>();
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        marked[s] = true;
        queue.add(s);
        while (!queue.isEmpty()) {
            announce();
            int curr = queue.removeFirst();
            if (curr == t) {
                return;
            }
            for (int neighbor: maze.adj(curr)) {
                if (!marked[neighbor]) {
                    marked[neighbor] = true;
                    edgeTo[neighbor] = curr;
                    distTo[neighbor] = distTo[curr] + 1;
                    queue.add(neighbor);
                    announce();
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

