package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import static org.junit.Assert.*;

public class Percolation {

    private final int N;
    private final int top;
    private final int btm;
    private final int[] grid;
    private final WeightedQuickUnionUF uf;
    private int numOpen;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;

        this.grid = new int[N * N];
        for (int i = 0; i < N * N; i++) {
            this.grid[i] = 0;
        }

        this.uf = new WeightedQuickUnionUF(N * N + 2);
        this.top = N * N;
        this.btm = N * N + 1;
    }

    // helper method: suitable for union()
    private int xyTo1D(int row, int col) {
        return row * N + col;
    }

    // helper method: throw exception
    private boolean testInput(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < N;
    }

    // helper method: are neighbors opened? if opened, then union.
    private void unionNeighbors(int row, int col) {
        int center = xyTo1D(row, col);
        if (row == 0) {
            uf.union(center, top);
        }
        if (row == N - 1) {
            uf.union(center, btm);
        }
        if (testInput(row - 1, col)) {
            if (isOpen(row - 1, col)) {
                uf.union(center, xyTo1D(row - 1, col));
            }
        }
        if (testInput(row, col - 1)) {
            if (isOpen(row, col - 1)) {
                uf.union(center, xyTo1D(row, col - 1));
            }
        }
        if (testInput(row + 1, col)) {
            if (isOpen(row + 1, col)) {
                uf.union(center, xyTo1D(row + 1, col));
            }
        }
        if (testInput(row, col + 1)) {
            if (isOpen(row, col + 1)) {
                uf.union(center, xyTo1D(row, col + 1));
            }
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!testInput(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (isOpen(row, col)) {
            return;
        }
        grid[xyTo1D(row, col)] = 1;
        numOpen += 1;
        unionNeighbors(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!testInput(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return grid[xyTo1D(row, col)] == 1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!testInput(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return uf.connected(top, xyTo1D(row, col));
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(top, btm);
    }

    // use for unit testing
    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        p.open(3, 4);
        p.open(2, 4);
        assertFalse(p.isOpen(2, 2));
        p.open(2, 2);
        assertTrue(p.isOpen(2, 2));
        p.open(2, 3);
        p.open(0, 2);
        assertFalse(p.isFull(2, 2));
        p.open(1, 2);
        assertTrue(p.isFull(2, 2));
        p.open(1, 2);
        assertEquals(6, p.numberOfOpenSites());
        assertFalse(p.percolates());
        p.open(4, 4);
        assertTrue(p.percolates());
    }
}
