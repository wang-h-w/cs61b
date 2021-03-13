import org.junit.Test;
import static org.junit.Assert.*;

/** @source StudentArrayDequeLauncher.java
 *  @author Haowen WANG
 */

public class TestArrayDequeGold {
    @Test
    public void testArrayDeque() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        StringBuilder record = new StringBuilder();

        int testNum = 1000;
        for (int i = 1; i < testNum; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();  // 0~1
            if (numberBetweenZeroAndOne < 0.25) {
                sad.addFirst(i);
                ads.addFirst(i);
                record.append("addFirst(").append(i).append(")\n");
            } else if (numberBetweenZeroAndOne < 0.5) {
                sad.addLast(i);
                ads.addLast(i);
                record.append("addLast(").append(i).append(")\n");
            } else if (numberBetweenZeroAndOne < 0.75) {
                if (sad.isEmpty() || ads.isEmpty()) {
                    continue;
                }
                Integer x = sad.removeFirst();
                Integer y = ads.removeFirst();
                assertEquals(record + "removeFirst()", x, y);
                record.append("removeFirst()\n");
            } else {
                if (sad.isEmpty() || ads.isEmpty()) {
                    continue;
                }
                Integer x = sad.removeLast();
                Integer y = ads.removeLast();
                assertEquals(record + "removeLast()", x, y);
                record.append("removeLast()\n");
            }
        }
    }
}
