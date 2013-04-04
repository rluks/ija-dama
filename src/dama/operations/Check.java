package dama.operations;

import dama.state.Desk;
import dama.state.Figure;

public final class Check {

    //v pripade ze je pocet figurek nektere barvy nulovy, vraci se true, znacici vitezstvi
    public static boolean isVictory(Desk desk)
    {
        Figure[][] this_board=desk.getState();
        int white_count=0;
        int black_count=0;
        int col,row;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if (this_board[col][row]!=null)
                {
                    if  (this_board[col][row].color=='B') black_count++;
                    else white_count++;
                }
        if ((white_count==0)  || (black_count==0) ) return true;
        else return false;
    }

    //kontrola jestli hrac ktery je na tahu (turnOf) musi vyhazovat nebo muze normalne jet
    public static boolean isMustTake(Desk desk, char turnOf)
    {
        Figure[][] this_board=desk.getState();
        int col,row;
        Boolean priznak_brani=false;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if (this_board[col][row]!=null)
                {


                }

        return priznak_brani;
    }

    //kontroluj a rovnou men na damy (neni to ale spravny OOP ]:->) (to Roman: na to ti seru)
    public static boolean isPieceToDame(Desk desk)
    {
        Figure[][] this_board=desk.getState();
        int col;
        Boolean priznak_zmeny=false;
        for (col=0; col<Desk.BOARD_SIZE; col++)
        {
            //bile zacinaji v radach 0-2, damou se stavaji v rade 7
            if (this_board[col][7].color=='W')
            {
                this_board[col][7].type='D';
                priznak_zmeny=true;
            }
            //cerni zacinaji v radach 5-7, damou se stavaji v rade 0
            if (this_board[col][0].color=='B')
            {
                this_board[col][0].type='D';
                priznak_zmeny=true;
            }
        }
        return priznak_zmeny;
    }
}
