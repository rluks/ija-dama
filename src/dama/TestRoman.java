package dama;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dama.state.Desk;
import dama.state.Position;

public class TestRoman {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void test01() {

        Desk d = new Desk();

        Position p0 = new Position(2, 0);
        Position p1 = new Position(3, 1);
        Turn t = new TurnWhite();


        Position p1 = d1.getPositionAt('h', 5);
        Position p2 = p1.nextPosition(-1, 2);
        Position p3 = d1.getPositionAt('g', 7);
        assertEquals("Posun z pozice h5 o (-1,2) = pozice g7", p2, p3);

        Position p10 = d1.getPositionAt('h', 5);
        assertEquals("Spravna pozice h5", p10, p1);
        assertNull("Pozice h5 je prazda (bez figurky).", p10.getFigure());

        Position p11 = d1.getPositionAt('h', 12);
        assertNull("Pozice h12 neexistuje.", p11);
    }

    /** Test tridy Desk a navazanych trid Figure (vcetne odvozenych) a Position */
    @Test
    public void test03() {
        Desk d1 = new Desk(8);

        Figure f1 = new Rook(d1.getPositionAt('d', 2));

        Figure f2 = d1.getPositionAt('d', 2).getFigure();
        assertEquals("Na pozici d2 je vez.", f2, f1);

        Figure f21 = d1.getFigureAt('d', 2);
        assertEquals("Na pozici d2 je vez", f21, f1);
        Figure f22 = d1.getFigureAt('d', 5);
        assertNull("Pozice d5 je prazda (bez figurky).", f22);


        Position p1 = d1.getPositionAt('d', 3);
        assertTrue("Vez se muze presunout z d2 na d3.", f2.canMove(p1));
        assertTrue("Vez se presune z d2 na d3.", f2.move(p1));
        assertTrue("Pozice d2 je prazdna", d1.getPositionAt('d', 2).getFigure() == null);

        Figure f3 = d1.getPositionAt('d', 3).getFigure();
        assertEquals("Na pozici d3 je vez.", f3, f1);
        assertEquals("Vez je na pozici d3.", f3.getPosition(), p1);

        Position p2 = d1.getPositionAt('g', 5);
        assertFalse("Vez se nemuze presunout z d3 na g5.", f2.canMove(p2));
        assertFalse("Pokus o presun veze z d3 na g5 je neuspesny.", f2.move(p2));
    }

}
