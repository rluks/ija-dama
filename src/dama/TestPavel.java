package dama;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dama.state.*;
import dama.operations.*;
import dama.io.Turn;

public class TestPavel {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void test01()
    {
        //Test tridy Dama.State.Position

        // 0,0 = A1
        Position p0 = new Position(0, 0);
        String pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"0,0\" na \"A1\": ");
        assertEquals("CHYBA", pos0, "A1");
        System.out.println("OK");
        // 0,7 = H1
        p0 = new Position(0, 7);
        pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"0,7\" na \"H1\": ");
        assertEquals("CHYBA", pos0, "H1");
        System.out.println("OK");
        // 7,0 = A8
        p0 = new Position(7, 0);
        pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"7,0\" na \"A8\": ");
        assertEquals("CHYBA", pos0, "A8");
        System.out.println("OK");
        // 7,7 = H8
        p0 = new Position(7, 7);
        pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"7,7\" na \"H8\": ");
        assertEquals("CHYBA", pos0, "H8");
        System.out.println("OK");
        // 4,2 = C5
        p0 = new Position(4, 2);
        pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"4,2\" na \"C5\": ");
        assertEquals("CHYBA", pos0, "C5");
        System.out.println("OK");
        // 3,6 = G4
        p0 = new Position(3, 6);
        pos0=p0.getStr();
        System.out.print("Fce pro prevod pozice \"3,6\" na \"G4\": ");
        assertEquals("CHYBA", pos0, "G4");
        System.out.println("OK");


        // A1 = 0,0
        pos0="A1";
        p0=Position.positionStrToPosition(pos0);
        Position p1=new Position(0, 0);
        System.out.print("Fce pro prevod retezce \"A1\" na pozici \"0,0\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        pos0="A8";
        p0=Position.positionStrToPosition(pos0);
        p1=new Position(7, 0);
        System.out.print("Fce pro prevod retezce \"A8\" na pozici \"7,0\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        pos0="H1";
        p0=Position.positionStrToPosition(pos0);
        p1=new Position(0, 7);
        System.out.print("Fce pro prevod retezce \"H1\" na pozici \"0,7\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        pos0="H8";
        p0=Position.positionStrToPosition(pos0);
        p1=new Position(7, 7);
        System.out.print("Fce pro prevod retezce \"H8\" na pozici \"7,7\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        pos0="C5";
        p0=Position.positionStrToPosition(pos0);
        p1=new Position(4, 2);
        System.out.print("Fce pro prevod retezce \"C5\" na pozici \"4,2\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        pos0="G4";
        p0=Position.positionStrToPosition(pos0);
        p1=new Position(3, 6);
        System.out.print("Fce pro prevod retezce \"G4\" na pozici \"3,6\": ");
        assertTrue("CHYBA", p0.equals(p1));
        System.out.println("OK");

        //nesmyslna pozice
        pos0="I4";
        p0=Position.positionStrToPosition(pos0);
        System.out.print("Fce pro prevod retezce \"I4\" na null: ");
        assertNull("CHYBA", p0);
        System.out.println("OK");

    }

    @Test
    public void test02()
    {


    }

}
