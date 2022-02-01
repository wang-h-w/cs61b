import org.junit.Test;
import static org.junit.Assert.*;

public class DisjointSetTest
{
	@Test
	public void UnionFindTest() {
		int n = 10;
		UnionFind s = new UnionFind(n);

		for (int i = 0; i < n; i++) {
			assertEquals(1, s.sizeOf(i));
		}

		s.union(2, 3);
		s.union(1, 2);
		s.union(5, 7);
		s.union(8, 4);
		s.union(7, 2);
		assertEquals(3, s.find(5));
		assertEquals(4, s.find(8));
		assertEquals(5, s.sizeOf(5));
		assertEquals(2, s.sizeOf(8));
		s.union(0, 6);
		s.union(6, 4);
		s.union(6, 3);
		assertEquals(3, s.find(8));
		assertEquals(3, s.find(6));
		assertEquals(9, s.sizeOf(0));
		assertTrue(s.connected(5, 0));
		assertFalse(s.connected(5, 9));
	}
}