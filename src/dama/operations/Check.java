package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;
import dama.io.Turn;
import dama.operations.Moves;

public final class Check {

    private Check(){}

    //v pripade ze je pocet figurek nektere barvy nulovy, vraci se true, znacici vitezstvi------------------------------------------------------------------------------------
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

    //kontrola jestli hrac ktery je na tahu (turnOf) musi vyhazovat nebo muze normalne jet------------------------------------------------------------------------------------
    public static boolean isMustTake(Desk desk, char turnOf)
    {
        Figure[][] this_board=desk.getState();
        int col,row;
        //Boolean priznak_brani=false;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if ( (this_board[row][col]!=null) )
                {    //najde figurku hrace, ktery je na tahu
                    if (this_board[row][col].color==turnOf)
                    {
                        Position pos0 = new Position(row, col);
                        int i;
                        for (i=2; i<8; i++)
                        {    //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
                            /*
                            Position pos1 = new Position(row+i, col+i);
                            if (Moves.isTakeValid(desk, pos0, pos1)!=null) return true;
                            pos1 = new Position(row+i, col-i);
                            if (Moves.isTakeValid(desk, pos0, pos1)!=null) return true;
                            pos1 = new Position(row-i, col+i);
                            if (Moves.isTakeValid(desk, pos0, pos1)!=null) return true;
                            pos1 = new Position(row-i, col-i);
                            if (Moves.isTakeValid(desk, pos0, pos1)!=null) return true;

                            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                            if (this_board[row][col].type=='P') break;
                            */

                            Position pos1 = new Position(pos0.row+i, pos0.col+i);
                            Turn turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null) return true;

                            pos1 = new Position(pos0.row+i, pos0.col-i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null) return true;

                            pos1 = new Position(pos0.row-i, pos0.col+i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null) return true;

                            pos1 = new Position(pos0.row-i, pos0.col-i);
                            turn = new Turn(this_board[row][col].color, pos0, pos1);
                            if (Moves.isTakeValid(desk, turn)!=null) return true;

                            //System.out.println("for");

                            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                            if (this_board[pos0.row][pos0.col].type=='P') break;
                        }
                    }

                }

        return false;
    }

    //kontroluj a rovnou men na damy (neni to ale spravny OOP ]:->) (to Roman: na to ti seru)----------------------------------------------------------------------------------
    public static boolean isPieceToDame(Desk desk)
    {
        Figure[][] this_board=desk.getState();
        int col;
        Boolean priznak_zmeny=false;
        for (col=0; col<Desk.BOARD_SIZE; col++)
        {
            //bile zacinaji v radach 0-2, damou se stavaji v rade 7
            if (this_board[7][col].color=='W')
            {
                this_board[7][col].type='D';
                priznak_zmeny=true;
            }
            //cerni zacinaji v radach 5-7, damou se stavaji v rade 0
            if (this_board[0][col].color=='B')
            {
                this_board[0][col].type='D';
                priznak_zmeny=true;
            }
        }
        return priznak_zmeny;
    }
}
