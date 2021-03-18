package creatures;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.awt.Color;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import huglife.Empty;

/** Tests the clorus class
 *  @author Haowen WANG
 */

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(6);
        assertEquals(6, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.move();
        assertEquals(5.96, c.energy(), 0.01);
        c.move();
        assertEquals(5.93, c.energy(), 0.01);
        c.stay();
        assertEquals(5.92, c.energy(), 0.01);
        c.stay();
        assertEquals(5.91, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(4.3);
        Clorus cr = c.replicate();
        assertNotEquals(c, cr);
        assertEquals(2.15, c.energy(), 0.01);
        assertEquals(2.15, cr.energy(), 0.01);
    }

    @Test
    public void testAttack() {
        Clorus c = new Clorus(2.4);
        Plip p = new Plip(1.8);
        c.attack(p);
        assertEquals(4.2, c.energy(), 0.01);
    }


    @Test
    public void testChoose() {
        Clorus c1 = new Clorus(0.6);  // empties.size()=0
        HashMap<Direction, Occupant> surrounded1 = new HashMap<Direction, Occupant>();
        surrounded1.put(Direction.TOP, new Impassible());
        surrounded1.put(Direction.BOTTOM, new Impassible());
        surrounded1.put(Direction.LEFT, new Impassible());
        surrounded1.put(Direction.RIGHT, new Impassible());

        Action actual1 = c1.chooseAction(surrounded1);
        Action expected1 = new Action(Action.ActionType.STAY);

        assertEquals(expected1, actual1);

        Clorus c2 = new Clorus(3.6);  // still stay
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        surrounded2.put(Direction.TOP, new Plip());
        surrounded2.put(Direction.BOTTOM, new Impassible());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Plip());

        Action actual2 = c2.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.STAY);

        assertEquals(expected2, actual2);

        Clorus c3 = new Clorus(2.1);  // attack
        HashMap<Direction, Occupant> surrounded3 = new HashMap<Direction, Occupant>();
        surrounded3.put(Direction.TOP, new Plip());
        surrounded3.put(Direction.BOTTOM, new Impassible());
        surrounded3.put(Direction.LEFT, new Empty());
        surrounded3.put(Direction.RIGHT, new Impassible());

        Action actual3 = c3.chooseAction(surrounded3);
        Action expected3 = new Action(Action.ActionType.ATTACK, Direction.TOP);

        assertEquals(expected3, actual3);

        Clorus c4 = new Clorus(1);  // replicate
        HashMap<Direction, Occupant> surrounded4 = new HashMap<Direction, Occupant>();
        surrounded4.put(Direction.TOP, new Impassible());
        surrounded4.put(Direction.BOTTOM, new Impassible());
        surrounded4.put(Direction.LEFT, new Empty());
        surrounded4.put(Direction.RIGHT, new Impassible());

        Action actual4 = c4.chooseAction(surrounded4);
        Action expected4 = new Action(Action.ActionType.REPLICATE, Direction.LEFT);

        assertEquals(expected4, actual4);

        Clorus c5 = new Clorus(0.6);  // move
        HashMap<Direction, Occupant> surrounded5 = new HashMap<Direction, Occupant>();
        surrounded5.put(Direction.TOP, new Impassible());
        surrounded5.put(Direction.BOTTOM, new Empty());
        surrounded5.put(Direction.LEFT, new Impassible());
        surrounded5.put(Direction.RIGHT, new Impassible());

        Action actual5 = c5.chooseAction(surrounded5);
        Action expected5 = new Action(Action.ActionType.MOVE, Direction.BOTTOM);

        assertEquals(expected5, actual5);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
}
