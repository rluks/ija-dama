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

/**
 * Testovaci trida JUnit
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class TestDama {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    /** Test tridy Desk */
    @Test
    public void testClearMustTakeFlag()
    {
        Desk bezeZmen = new Desk();
        Desk budemVolatClear = new Desk();

        Figure[][] pole = budemVolatClear.getState();
        pole[2][2].mustTakeFlag=true;
        pole[5][1].mustTakeFlag=true;
        budemVolatClear.clearMustTakeFlag();

        System.out.print("Kontrola spravnosti fce pro cisteni flagu figurky indikujici ze musi brat: ");
        assertEquals("Kontrola spravnosti fce pro cisteni flagu figurky indikujici ze musi brat: CHYBA", bezeZmen, budemVolatClear);
        System.out.println("OK");

    }

    /** Test tridy Desk */
    @Test
    public void testGetFigureAtPosition()
    {

        Desk d = new Desk();

        Position p9i = new Position(8 ,8);//Position(row, col)
        System.out.print("Pokus o pristup na pozici I9 (ocekavana navratova hodnota: null): ");
        assertNull("Pokus o pristup na pozici I9 - CHYBA", d.getFigureAtPosition(p9i));
        System.out.println("OK");

        Position p8a = new Position(7, 0);
        System.out.print("Kontrola, zda je pozice A8 prazdna: ");
        assertNull("Kontrola, zda je pozice A8 prazdna: CHYBA", d.getFigureAtPosition(p8a));
        System.out.println("OK");

        Position p8b = new Position(7, 1);
        Figure token = Figure.FigureBlackPiece("Token");
        System.out.print("Kontrola, ze na pozici B8 je figurka se jmenem \"Token\": ");
        assertEquals("Kontrola, ze na pozici B8 je figurka se jmenem \"Token\": CHYBA", token, d.getFigureAtPosition(p8b));
        System.out.println("OK");
    }

    @Test
    public void test01()
    {
        //Test tridy Dama.State.Position

        // 0,0 = A1
        Position p0 = new Position(0, 0);
        System.out.print("Fce pro prevod pozice \"0,0\" na \"A1\": ");
        assertEquals("Fce pro prevod pozice \"0,0\" na \"A1\": CHYBA", p0.getStr(), "A1");
        System.out.println("OK");
        // 0,7 = H1
        p0 = new Position(0, 7);
        System.out.print("Fce pro prevod pozice \"0,7\" na \"H1\": ");
        assertEquals("Fce pro prevod pozice \"0,7\" na \"H1\": CHYBA", p0.getStr(), "H1");
        System.out.println("OK");
        // 7,0 = A8
        p0 = new Position(7, 0);
        System.out.print("Fce pro prevod pozice \"7,0\" na \"A8\": ");
        assertEquals("Fce pro prevod pozice \"7,0\" na \"A8\": CHYBA", p0.getStr(), "A8");
        System.out.println("OK");
        // 7,7 = H8
        p0 = new Position(7, 7);
        System.out.print("Fce pro prevod pozice \"7,7\" na \"H8\": ");
        assertEquals("Fce pro prevod pozice \"7,7\" na \"H8\": CHYBA", p0.getStr(), "H8");
        System.out.println("OK");
        // 4,2 = C5
        p0 = new Position(4, 2);
        System.out.print("Fce pro prevod pozice \"4,2\" na \"C5\": ");
        assertEquals("Fce pro prevod pozice \"4,2\" na \"C5\": CHYBA", p0.getStr(), "C5");
        System.out.println("OK");
        // 3,6 = G4
        p0 = new Position(3, 6);
        System.out.print("Fce pro prevod pozice \"3,6\" na \"G4\": ");
        assertEquals("Fce pro prevod pozice \"3,6\" na \"G4\": CHYBA", p0.getStr(), "G4");
        System.out.println("OK");


        // A1 = 0,0
        p0=Position.positionStrToPosition("A1");
        Position p1=new Position(0, 0);
        System.out.print("Fce pro prevod retezce \"A1\" na pozici \"0,0\": ");
        assertTrue("Fce pro prevod retezce \"A1\" na pozici \"0,0\": FAILED", p0.equals(p1));
        System.out.println("OK");

        p0=Position.positionStrToPosition("A8");
        p1=new Position(7, 0);
        System.out.print("Fce pro prevod retezce \"A8\" na pozici \"7,0\": ");
        assertTrue("Fce pro prevod retezce \"A8\" na pozici \"7,0\": CHYBA", p0.equals(p1));
        System.out.println("OK");

        p0=Position.positionStrToPosition("H1");
        p1=new Position(0, 7);
        System.out.print("Fce pro prevod retezce \"H1\" na pozici \"0,7\": ");
        assertTrue("Fce pro prevod retezce \"H1\" na pozici \"0,7\": CHYBA", p0.equals(p1));
        System.out.println("OK");

        p0=Position.positionStrToPosition("H8");
        p1=new Position(7, 7);
        System.out.print("Fce pro prevod retezce \"H8\" na pozici \"7,7\": ");
        assertTrue("Fce pro prevod retezce \"H8\" na pozici \"7,7\": CHYBA", p0.equals(p1));
        System.out.println("OK");

        p0=Position.positionStrToPosition("C5");
        p1=new Position(4, 2);
        System.out.print("Fce pro prevod retezce \"C5\" na pozici \"4,2\": ");
        assertTrue("Fce pro prevod retezce \"C5\" na pozici \"4,2\": CHYBA", p0.equals(p1));
        System.out.println("OK");

        p0=Position.positionStrToPosition("G4");
        p1=new Position(3, 6);
        System.out.print("Fce pro prevod retezce \"G4\" na pozici \"3,6\": ");
        assertTrue("Fce pro prevod retezce \"G4\" na pozici \"3,6\": CHYBA", p0.equals(p1));
        System.out.println("OK");

        //nesmyslna pozice
        System.out.print("Fce pro prevod retezce \"I4\" na pozici (ocekavana navratova hodnota: null): ");
        assertNull("Fce pro prevod retezce \"I4\" na null se: CHYBA", Position.positionStrToPosition("I4"));
        System.out.println("OK");
    }

    @Test
    public void test02()
    {
        Desk d = new Desk();
        Figure[][] this_board=d.getState();

        System.out.print("Fce pro kontorolu vitezneho stavu (pocatecni stav hry, ocekavana hodnota: false): ");
        assertFalse("Fce pro kontorolu vitezneho stavu (pocatecni stav hry, ocekavana hodnota: false): CHYBA", Check.isVictory(d));
        System.out.println("OK");

        //odstrani vsechny cerne figurky
        for (int row=0;row<8;row++)
            for (int col=0;col<8;col++)
                if ( (this_board[row][col]!=null) && ((this_board[row][col].color=='B')) )
                    this_board[row][col]=null;
        System.out.print("Na desce jsou jen bile figurky, doslo k vitezstvi: ");
        assertTrue("Na desce jsou jen bile figurky, doslo k vitezstvi: CHYBA", Check.isVictory(d));
        System.out.println("OK");

        //odstrani vsechny bile figurky
        d = new Desk();
        this_board=d.getState();
        for (int row=0;row<8;row++)
            for (int col=0;col<8;col++)
                if ( (this_board[row][col]!=null) && ((this_board[row][col].color=='W')) )
                    this_board[row][col]=null;
        System.out.print("Na desce jsou jen cerne figurky, doslo k vitezstvi: ");
        assertTrue("Na desce jsou jen cerne figurky, doslo k vitezstvi: CHYBA", Check.isVictory(d));
        System.out.println("OK");

        d = new Desk();
        this_board=d.getState();

        System.out.println("Primitivni vizualizace desky pro nasledujici assert.");
        System.out.println("pozn. P-pesak, D-dama, B-cerna, W-bila figurka");
        d.printSimpleBoard();
        System.out.print("Bily hrac nema koho brat: ");
        assertFalse("Bily hrac nema koho brat: CHYBA", Check.isMustTake(d, 'W'));
        System.out.println("OK");

        System.out.print("Cerny hrac nema koho brat: ");
        assertFalse("Cerny hrac nema koho brat: CHYBA", Check.isMustTake(d, 'B'));
        System.out.println("OK");

        Position origin=new Position(2, 0);
        Position dest=new Position(3, 1);
        Turn t = Turn.TurnWhite(origin, dest);
        System.out.print("Bily hraje A3->B4: ");
        assertTrue("Bily hraje A3->B4: CHYBA", Moves.Move(d, t) );
        System.out.println("OK");

        origin=new Position(3, 1);
        dest=new Position(4, 2);
        t = Turn.TurnWhite(origin, dest);
        System.out.print("Bily hraje B4->C5: ");
        assertTrue("Bily hraje B4->C5: CHYBA", Moves.Move(d, t) );
        System.out.println("OK");

        System.out.println("Stav desky po techto dvou tazich:");
        d.printSimpleBoard();

        origin=new Position(4, 2);
        dest=new Position(5, 3);
        t = Turn.TurnWhite(origin, dest);
        System.out.print("Bily hraje C5->D6(pozice je uz obsazena, ocekavana navratova hodnota: false): ");
        assertFalse("Bily hraje C5->D6(pozice je uz obsazena, ocekavana navratova hodnota: false): CHYBA", Moves.Move(d, t) );
        System.out.println("OK");

        origin=new Position(5, 1);
        dest=new Position(3, 3);
        t = Turn.TurnBlack(origin, dest);
        System.out.print("Kontrola, jestli jakakoli fugurka cerneho hrace muze brat bilou figurku: ");
        assertTrue("Kontrola, jestli jakakoli fugurka cerneho hrace muze brat bilou figurku: CHYBA", Check.isMustTake(d, 'B'));
        System.out.println("OK");

        System.out.print("Cerny hrac, brani B6->x->D4: ");
        assertTrue("Cerny hrac, brani B6->x->D4: CHYBA", Moves.Take(d, t));
        System.out.println("OK");

        d.clearMustTakeFlag();
        System.out.println("Stav desky po tomto tahu:");
        d.printSimpleBoard();

        System.out.print("Kontrola, jestli ma bily hrac koho brat (ocekavana navratova hodvota: true): ");
        assertTrue("Kontrola, jestli ma bily hrac koho brat (ocekavana navratova hodvota: true): CHYBA", Check.isMustTake(d, 'W'));
        System.out.println("OK");

        origin=new Position(2, 4);
        dest=new Position(4, 2);
        t = Turn.TurnWhite(origin, dest);
        System.out.print("Bily hrac, brani E3->x->C5: ");
        assertTrue("Bily hrac, brani E3->x->C5: CHYBA", Moves.Take(d, t));
        System.out.println("OK");

        d.clearMustTakeFlag();
        System.out.println("Stav desky po tomto tahu:");
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
        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        System.out.print("Pesaci na, kteri se dostali na druhou dtranu desky jsou zmeneny na damy: ");
        assertTrue("Pesaci na, kteri se dostali na druhou dtranu desky jsou zmeneny na damy: CHYBA", Check.isPieceToDame(d));
        System.out.println("OK");

        System.out.print("Na D8 je dama: ");
        assertEquals("Na D8 je dama: CHYBA", 'D', this_board[7][3].type);
        System.out.println("OK");

        System.out.print("Na E1 je dama: ");
        assertEquals("Na E1 je dama: CHYBA", 'D', this_board[0][4].type);
        System.out.println("OK");

        this_board[1][3]= Figure.FigureWhitePiece("random victim");

        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        origin=new Position(0, 4);
        dest=new Position(4, 0);
        t = Turn.TurnBlack(origin, dest);
        System.out.print("Kontrola, jestli muze cerny hrac s nekterou ze svych figur brat (ano muze): ");
        assertTrue("Kontrola, jestli muze cerny hrac s nekterou ze svych figur brat (ano muze): CHYBA", Check.isMustTake(d, 'B'));
        System.out.println("OK");

        System.out.print("Cerny hrac, brani damou E1->x->A5: ");
        assertTrue("Cerny hrac, brani damou E1->x->A5: CHYBA", Moves.Take(d, t));
        System.out.println("OK");

        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        this_board[3][1]= Figure.FigureWhitePiece("random victim");
        this_board[2][2]= Figure.FigureBlackPiece("random victim2");

        System.out.println("Pridany nove figurky: ");
        d.printSimpleBoard();

        origin=new Position(4, 0);
        dest=new Position(0, 4);
        System.out.print("Kontrola, jestli muze cerny hrac s nekterou ze svych figur brat (ne nemuze): ");
        assertFalse("Kontrola, jestli muze cerny hrac s nekterou ze svych figur brat (ne nemuze): CHYBA", Check.isMustTake(d, 'B'));
        System.out.println("OK");

        System.out.print("Pokus o pohyb pres dve figurky (jedna cizi a jedna vlastni) A5->E1 (ocekava se false): ");
        assertFalse("Pokus o pohyb pres dve figurky (jedna cizi a jedna vlastni) A5->E1 (ocekava se false): CHYBA", Moves.Move(d, t));
        System.out.println("OK");

        System.out.print("Pokus o brani pres dve figurky (jedna cizi a jedna vlastni) A5->E1 (ocekava se false): ");
        assertFalse("Pokus o brani pres dve figurky (jedna cizi a jedna vlastni) A5->E1 (ocekava se false): CHYBA", Moves.Take(d, t));
        System.out.println("OK");

        //canTake a canMove
        System.out.print("Kontrola, jestli se dama na A5 muze posunout (jina fce, ocekavano true): ");
        assertTrue("Kontrola, jestli se dama na A5 muze posunout: CHYBA", Moves.canMove(origin, d));
        System.out.println("OK");

        System.out.print("Kontrola, jestli ma dama na A5 co vyhodit (jina fce, ocekavano false): ");
        assertFalse("Kontrola, jestli ma dama na A5 co vyhodit (jina fce, ocekavano false): CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        this_board[2][2]= null;
        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        System.out.print("Dama na A5 nyny MA co vyhodit (ocekava se true): ");
        assertTrue("Dama na A5 nyny MA co vyhodit (true): CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        origin=new Position(0, 2);
        System.out.print("Pesak na C1 se ma kam pohnout: ");
        assertTrue("Pesak na C1 se ma kam pohnout: CHYBA", Moves.canMove(origin, d));
        System.out.println("OK");

        System.out.print("Pesak na C1 nema co vyhodit (ocekava se false): ");
        assertFalse("Pesak na C1 nema co vyhodit (ocekavano false): CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        d = new Desk();
        this_board=d.getState();
        origin=new Position(0, 2);
        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        System.out.print("Pesak na C1 se nema kam pohnout: ");
        assertFalse("Pesak na C1 se nema kam pohnout: CHYBA", Moves.canMove(origin, d));
        System.out.println("OK");

        System.out.print("Pesak na C1 nema co vyhodit: ");
        assertFalse("Pesak na C1 nema co vyhodit: CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        this_board[0][2].type='D';
        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        System.out.print("Dama na C1 se nema kam pohnout: ");
        assertFalse("Dama na C1 se nema kam pohnout: CHYBA", Moves.canMove(origin, d));
        System.out.println("OK");

        System.out.print("Dama na C1 nema co vyhodit: ");
        assertFalse("Dama na C1 nema co vyhodit: CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        this_board[3][3]=Figure.FigureBlackPiece("random: CHYBA");
        System.out.println("Nova podoba desky: ");
        d.printSimpleBoard();

        origin=new Position(2, 2);
        System.out.print("Pesak na C3 MA co vyhodit: ");
        assertTrue("Pesak na C3 MA co vyhodit: CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");

        origin=new Position(2, 4);
        System.out.print("Pesak na E3 MA co vyhodit: ");
        assertTrue("Pesak na E3 MA co vyhodit: CHYBA", Moves.canTake(origin, d));
        System.out.println("OK");
    }

}







































