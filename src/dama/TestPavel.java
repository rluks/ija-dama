package dama;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        ////System.out.print("Fce pro prevod pozice \"0,0\" na \"A1\": ");
        assertEquals("CHYBA", p0.getStr(), "A1");
        ////System.out.println("OK");
        // 0,7 = H1
        p0 = new Position(0, 7);
         ////System.out.print("Fce pro prevod pozice \"0,7\" na \"H1\": ");
        assertEquals("CHYBA", p0.getStr(), "H1");
        ////System.out.println("OK");
        // 7,0 = A8
        p0 = new Position(7, 0);
        ////System.out.print("Fce pro prevod pozice \"7,0\" na \"A8\": ");
        assertEquals("CHYBA", p0.getStr(), "A8");
        ////System.out.println("OK");
        // 7,7 = H8
        p0 = new Position(7, 7);
        //System.out.print("Fce pro prevod pozice \"7,7\" na \"H8\": ");
        assertEquals("CHYBA", p0.getStr(), "H8");
        //System.out.println("OK");
        // 4,2 = C5
        p0 = new Position(4, 2);
        //System.out.print("Fce pro prevod pozice \"4,2\" na \"C5\": ");
        assertEquals("CHYBA", p0.getStr(), "C5");
        //System.out.println("OK");
        // 3,6 = G4
        p0 = new Position(3, 6);
        //System.out.print("Fce pro prevod pozice \"3,6\" na \"G4\": ");
        assertEquals("CHYBA", p0.getStr(), "G4");
        //System.out.println("OK");


        // A1 = 0,0
        p0=Position.positionStrToPosition("A1");
        Position p1=new Position(0, 0);
        //System.out.print("Fce pro prevod retezce \"A1\" na pozici \"0,0\": ");
        assertTrue("Fce pro prevod retezce \"A1\" na pozici \"0,0\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        p0=Position.positionStrToPosition("A8");
        p1=new Position(7, 0);
        //System.out.print("Fce pro prevod retezce \"A8\" na pozici \"7,0\": ");
        assertTrue("Fce pro prevod retezce \"A8\" na pozici \"7,0\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        p0=Position.positionStrToPosition("H1");
        p1=new Position(0, 7);
        //System.out.print("Fce pro prevod retezce \"H1\" na pozici \"0,7\": ");
        assertTrue("Fce pro prevod retezce \"H1\" na pozici \"0,7\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        p0=Position.positionStrToPosition("H8");
        p1=new Position(7, 7);
        //System.out.print("Fce pro prevod retezce \"H8\" na pozici \"7,7\": ");
        assertTrue("Fce pro prevod retezce \"H8\" na pozici \"7,7\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        p0=Position.positionStrToPosition("C5");
        p1=new Position(4, 2);
        //System.out.print("Fce pro prevod retezce \"C5\" na pozici \"4,2\": ");
        assertTrue("Fce pro prevod retezce \"C5\" na pozici \"4,2\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        p0=Position.positionStrToPosition("G4");
        p1=new Position(3, 6);
        //System.out.print("Fce pro prevod retezce \"G4\" na pozici \"3,6\": ");
        assertTrue("Fce pro prevod retezce \"G4\" na pozici \"3,6\" se pokazila", p0.equals(p1));
        //System.out.println("OK");

        //nesmyslna pozice
        //System.out.print("Fce pro prevod retezce \"I4\" na null: ");
        assertNull("Fce pro prevod retezce \"I4\" na null se pokazila", Position.positionStrToPosition("I4"));
        //System.out.println("OK");
    }

    @Test
    public void test02()
    {
        Desk d = new Desk();
        Figure[][] this_board=d.getState();

        //System.out.println("");
        assertFalse("Hra v pocatecnim stavu, nedoslo k vitezstvi", Check.isVictory(d));

        //odstrani vsechny cerne figurky
        for (int row=0;row<8;row++)
            for (int col=0;col<8;col++)
                if ( (this_board[row][col]!=null) && ((this_board[row][col].color=='B')) )
                    this_board[row][col]=null;
        assertTrue("Na desce jsou jen bile figurky, doslo k vitezstvi", Check.isVictory(d));

        //odstrani vsechny bile figurky
        d = new Desk();
        this_board=d.getState();
        for (int row=0;row<8;row++)
            for (int col=0;col<8;col++)
                if ( (this_board[row][col]!=null) && ((this_board[row][col].color=='W')) )
                    this_board[row][col]=null;
        assertTrue("Na desce jsou jen cerne figurky, doslo k vitezstvi", Check.isVictory(d));

        d = new Desk();
        this_board=d.getState();

        d.printSimpleBoard();
        assertFalse("Bily hrac nema koho brat", Check.isMustTake(d, 'W'));
        assertFalse("Cerny hrac nema koho brat", Check.isMustTake(d, 'B'));

        Position origin=new Position(2, 0);
        Position dest=new Position(3, 1);
        Turn t = Turn.TurnWhite(origin, dest);
        assertTrue("Bily hraje A3->B4", Moves.Move(d, t) );
        d.printSimpleBoard();

        origin=new Position(3, 1);
        dest=new Position(4, 2);
        t = Turn.TurnWhite(origin, dest);
        assertTrue("Bily hraje B4->C5", Moves.Move(d, t) );
        d.printSimpleBoard();

        origin=new Position(4, 2);
        dest=new Position(5, 3);
        t = Turn.TurnWhite(origin, dest);
        assertFalse("Bily hraje C5->D6, ale tato pozice je uz obsazena", Moves.Move(d, t) );
        d.printSimpleBoard();


        origin=new Position(5, 1);
        dest=new Position(3, 3);
        t = Turn.TurnBlack(origin, dest);
        assertTrue("Cerny hrac MA koho brat", Check.isMustTake(d, 'B'));
        assertTrue("Brani B6->x->D4", Moves.Take(d, t));
        d.printSimpleBoard();
        assertTrue("Bily hrac MA koho brat", Check.isMustTake(d, 'W'));

        origin=new Position(2, 4);
        dest=new Position(4, 2);
        t = Turn.TurnWhite(origin, dest);
        assertTrue("Bily hrac MA koho brat", Check.isMustTake(d, 'W'));
        assertTrue("Brani E3->x->C5", Moves.Take(d, t));
        d.printSimpleBoard();


        //hratky s damami
        for (int row=0;row<8;row++)
            for (int col=0;col<8;col++)
            {
                if ((col==1) && (row==7)) continue;
                else if ((col==3) && (row==7)) this_board[row][col].color='W';
                else if ((col==2) && (row==0)) continue;
                else if ((col==4) && (row==0)) this_board[row][col].color='B';
                else this_board[row][col]=null;
            }
        d.printSimpleBoard();

        assertTrue("zmena pesaku na damy", Check.isPieceToDame(d));
        d.printSimpleBoard();
        assertEquals("Na D8 je dama", 'D', this_board[7][3].type);
        assertEquals("Na E1 je dama", 'D', this_board[0][4].type);

        this_board[1][3]= Figure.FigureWhitePiece("random victim");
        d.printSimpleBoard();

        origin=new Position(0, 4);
        dest=new Position(4, 0);
        t = Turn.TurnBlack(origin, dest);
        assertTrue("Cerny hrac MA koho brat", Check.isMustTake(d, 'B'));
        assertTrue("Brani E3->x->C5", Moves.Take(d, t));
        d.printSimpleBoard();









    }

}







































