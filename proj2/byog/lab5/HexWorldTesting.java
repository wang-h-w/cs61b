package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

public class HexWorldTesting {
    @Test
    public void calcSquareMaskTest() {
        HexWorld hex = new HexWorld(2);
        int[][] expected = new int[][] {new int[]{0, 1, 1, 0}, new int[]{1, 1, 1, 1},
                new int[]{1, 1, 1, 1}, new int[]{0, 1, 1, 0}};
        int[][] actual = hex.calcSquareMask(2);
        assertArrayEquals(expected, actual);

        hex = new HexWorld(3);
        expected = new int[][] {new int[]{0, 0, 1, 1, 0, 0}, new int[]{0, 1, 1, 1, 1, 0},
                new int[]{1, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 1, 1}, new int[]{1, 1, 1, 1, 1, 1},
                new int[]{0, 1, 1,1, 1, 0}, new int[]{0, 0, 1, 1, 0, 0}};
        actual = hex.calcSquareMask(3);
        assertArrayEquals(expected, actual);
    }
}
