package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Haowen WANG
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(5);
        assertEquals(5, arb.capacity());
        assertTrue(arb.isEmpty());
        arb.enqueue(0);
        assertFalse(arb.isEmpty());
        arb.enqueue(1);
        arb.enqueue(2);
        assertEquals(0, arb.dequeue());
        assertEquals(2, arb.fillCount());
        assertEquals(1, arb.peek());
        arb.enqueue(3);
        arb.enqueue(4);
        arb.enqueue(5);
        assertTrue(arb.isFull());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
