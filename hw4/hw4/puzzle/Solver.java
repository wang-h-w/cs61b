package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Comparator;

public class Solver {

    private List<WorldState> solution;  // for solution()
    private Map<WorldState, Integer> cache;
    private MinPQ<SearchNode> minPQ = new MinPQ<>(new SearchNodeComparator());

    /**
     * Constructor which solves the puzzle, computing
     * everything necessary for moves() and solution() to
     * not have to solve the problem again. Solves the
     * puzzle using the A* algorithm. Assumes a solution exists.
     * @param initial initial world state
     */
    public Solver(WorldState initial) {

        // Initialization
        solution = new ArrayList<>();
        SearchNode sentinel = new SearchNode(null, 0, null);
        SearchNode s = new SearchNode(initial, 0, sentinel);
        minPQ.insert(s);

        while (!minPQ.min().worldState.isGoal()) {
            SearchNode min = minPQ.delMin();
            Iterable<WorldState> surround = min.worldState.neighbors();
            for (WorldState w: surround) {
                if (!w.equals(min.prevSearch.worldState)) {
                    minPQ.insert(new SearchNode(w, min.numMoves + 1, min));
                }
            }
        }

        // source back from the destination -> get path
        // wrong: add every min to solution!!!
        Stack<WorldState> path = new Stack<>();
        for (SearchNode n = minPQ.min(); n != sentinel; n = n.prevSearch) {
            path.push(n.worldState);
        }
        while (!path.isEmpty()) {
            solution.add(path.pop());
        }
    }

    /**
     * Returns the minimum number of moves to solve the puzzle starting
     * at the initial WorldState.
     */
    public int moves() {
        return solution.size() - 1;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        return solution;
    }

    private static class SearchNode {
        private final WorldState worldState;
        private final int numMoves;
        private final SearchNode prevSearch;

        SearchNode(WorldState w, int n, SearchNode s) {
            worldState = w;
            numMoves = n;
            prevSearch = s;
        }
    }

    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode s1, SearchNode s2) {
            int s1Total = s1.numMoves + s1.worldState.estimatedDistanceToGoal();
            int s2Total = s2.numMoves + s2.worldState.estimatedDistanceToGoal();
            return Integer.compare(s1Total, s2Total);
        }

        // Optimization
        private int getCache(SearchNode s) {
            if (!cache.containsKey(s.worldState)) {
                cache.put(s.worldState, s.worldState.estimatedDistanceToGoal());
            }
            return cache.get(s.worldState);
        }
    }
}
