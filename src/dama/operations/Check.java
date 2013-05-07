package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;
import dama.io.Turn;
import dama.operations.Moves;

/**
 * Třída Třída obsahuje statické metody ke kontrole stavu hry po každém tahu (vítězství, nutnost
 * braní soupeřova kamene a změnu pěšce na dámu).
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public final class Check
{

    /**
     * Privatni konstruktor (zabranuje vytvoreni instance teto tridy)
     */
    private Check(){}

    /**
     * Kontroluje, jestli nedoslo k vitezstvi jednoho z hracu
     * @param desk     nad kterou deskou se ma kontrola vykonat
     * @return         vraci true pokud doslo k vitezstvi jednoho z hrace
     */
    public static boolean isVictory(Desk desk)
    {
        Figure[][] this_board=desk.getState();
        int white_count=0;
        int black_count=0;
        int col,row;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if (this_board[row][col]!=null)
                {
                    if  (this_board[row][col].color=='B') black_count++;
                    if  (this_board[row][col].color=='W') white_count++;
                }
        if ((white_count==0)  || (black_count==0) ) return true;
        else return false;
    }

    public static boolean victoryOf(Desk desk, char hrac)
    {
        Figure[][] this_board=desk.getState();
        int hrac_count=0;
        int col,row;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if (this_board[row][col]!=null)
                {
                    if  (this_board[row][col].color==hrac) hrac_count++;

                }
        if (hrac_count==0) return false;
        else return true;
    }

    /**
     * Kontrola jestli hrac ktery je na tahu (turnOf) musi vyhazovat nebo muze normalne jet (u takovychto figurek nastavi flag brani na true)
     * @param desk      deska nad kterou se ma kontrola provest
     * @param turnOf    urcuje pro ktereho hrace se ma kontrola vykonat
     * @return            pokud hrac musi brat, vraci true, jinak vraci false
     */
    public static boolean isMustTake(Desk desk, char turnOf)
    {
        Figure[][] this_board=desk.getState();
        int col,row;
        Boolean priznak_brani=false;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if ( (this_board[row][col]!=null) )
                {    //najde figurku hrace, ktery je na tahu
                    if (this_board[row][col].color==turnOf)
                    {
                        Position pos0 = new Position(row, col);
                        Position pos0_zaloha = new Position(row, col);
                        int i;
                        for (i=2; i<8; i++)
                        {    //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
                            Position pos1 = new Position(row+i, col+i);
                            Turn turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null)
                            {
                                this_board[row][col].mustTakeFlag=true;
                                priznak_brani = true;
                            }
                            pos0 = new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(row+i, col-i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null)
                            {
                                this_board[row][col].mustTakeFlag=true;
                                priznak_brani = true;
                            }
                            pos1 = new Position(row-i, col+i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null)
                            {
                                this_board[row][col].mustTakeFlag=true;
                                priznak_brani = true;
                            }
                            pos1 = new Position(row-i, col-i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null)
                            {
                                this_board[row][col].mustTakeFlag=true;
                                priznak_brani = true;
                            }
                            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka

                            if (this_board[row][col].type=='P') break;
                        }
                    }

                }
        return priznak_brani;
    }

    /**
     * Pokud nejaci pesaci dosli na druhou stranu plochy, jsou transformovani na damy
     * @param desk    nad kterou deskou se ma fce provest
     * @return         vraci true pokud byli nejaci pesaci zmeneni na damy
     */
    public static boolean isPieceToDame(Desk desk)
    {
        Figure[][] this_board=desk.getState();
        int col;
        Boolean priznak_zmeny=false;
        for (col=0; col<Desk.BOARD_SIZE; col++)
        {
            //bile zacinaji v radach 0-2, damou se stavaji v rade 7
            if ( (this_board[7][col]!=null) && (this_board[7][col].color=='W') )
            {
                this_board[7][col].type='D';
                priznak_zmeny=true;
            }
            //cerni zacinaji v radach 5-7, damou se stavaji v rade 0
            if ((this_board[0][col]!=null) && (this_board[0][col].color=='B'))
            {
                this_board[0][col].type='D';
                priznak_zmeny=true;
            }
        }
        return priznak_zmeny;
    }

    /**
     * Kontroluje, jestli je prichozi string validni notace
     * @param str    tento string se validuje
     * @return        vraci true, pokud je string validni, jinak vraci false
     */
    public static boolean isNotationValid(String str)
    {
        if (str.length()!=5) return false;

        char pom=str.charAt(0);
        if (!( (pom>='a') && (pom<='h') )) return false;
        pom=str.charAt(1);
        if (!( (pom>='1') && (pom<='8') )) return false;
        pom=str.charAt(2);
        if (!( (pom=='-') || (pom=='x') )) return false;
        pom=str.charAt(3);
        if (!( (pom>='a') && (pom<='h') )) return false;
        pom=str.charAt(4);
        if (!( (pom>='1') && (pom<='8') )) return false;

        return true;
    }


}




