package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int s;
    private boolean circle = false;
    private int[] link;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        s = maze.xyTo1D(1, 1);
        link = new int[maze.V()];
        link[s] = s;  // replace edgeTo[]
    }

    @Override
    public void solve() {
        dfs(s);
    }

    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        announce();

        for (int w: maze.adj(v)) {
            if (marked[w] && w != link[v]) {
                circle = true;
                link[w] = v;
                int curr = v;
                edgeTo[curr] = link[curr];
                while (curr != w) {
                    curr = link[curr];
                    edgeTo[curr] = link[curr];
                }
                announce();
                return;
            }
            if (!marked[w]) {
                link[w] = v;
                dfs(w);
                if (circle) {
                    return;
                }
            }
        }
    }
}

