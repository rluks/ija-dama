package dama;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;

public class TestRoman {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    /** Test tridy Desk */
    @Test
    public void testClearMustTakeFlag() {

        Desk bezeZmen = new Desk();
        Desk budemVolatClear = new Desk();

        budemVolatClear.clearMustTakeFlag();
        assertEquals("clearMustTakeFlag volane na prave inicializovanou desku je bez efektu", bezeZmen, budemVolatClear);

    }

    /** Test tridy Desk */
    @Test
    public void testGetFigureAtPosition() {

        Desk d = new Desk();

        Position p9i = new Position(8 ,8);//Position(row, col)
        assertNull("Pozice p9i neexistuje.", d.getFigureAtPosition(p9i));

        Position p8a = new Position(7, 0);
        assertNull("Na pozici p8a nikdo neni", d.getFigureAtPosition(p8a));

        Position p8b = new Position(7, 1);
        Figure token = Figure.FigureBlackPiece("Token");
        assertEquals("Na pozici 8B je Token", token, d.getFigureAtPosition(p8b));
    }

}
