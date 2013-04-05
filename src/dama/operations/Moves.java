package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;
import dama.io.Turn;


public class Moves
{
    //TODO col<->row

    //kontroluje, jestli je tento pohyb figurkou mozny--------------------------------------------------------------------------------------------------------------------
    public static boolean isMoveValid(Desk desk, Turn turn)
    {
        Position origin = turn.origin;
        Position dest = turn.destination;

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.row][origin.col];

        //kontrola rozsahu pozic
        if ((origin.col>7) || (origin.col<0)) return false;
        if ((origin.row>7) || (origin.row<0)) return false;
        if ((dest.col>7) || (dest.col<0)) return false;
        if ((dest.row>7) || (dest.row<0)) return false;

        //kontrola, jestli je pozice na kterou tahne, je volna
        if (this_board[dest.row][dest.col]!=null) return false;
        if (this_board[origin.row][origin.col]!=null) return false;

        //bily pesak
        if ( (fig.type=='P') && (fig.color=='W') )
        {    //bily pesak se muze posunout o jednu radu niz a o jeden sloupec doleva nebo doprava
            if ( (origin.row+1==dest.row) && ((origin.col==dest.col+1) || (origin.col==dest.col-1)) )
                return true;
            else return false;
        }
        //cerny pesak
        if ( (fig.type=='P') && (fig.color=='B') )
        {    //cerny pesak se muze posunout o jednu radu nahoru a o jeden sloupec doleva nebo doprava
            if ( (origin.row-1==dest.row) && ((origin.col==dest.col+1) || (origin.col==dest.col-1)) )
                return true;
            else return false;
        }
        //dama
        if (fig.type=='D')
        {
            //rozdil radku a sloupcu
            int d_col= dest.col-origin.col;
            int d_row= dest.row-origin.row;

            //dama se pohybuje libovolne po uhlopricce - je treba zkontolovat jestli se posunula o stejny pocet sloupcu jako radku
            if ( (Math.abs(d_row))!=(Math.abs(d_col)) ) return false;

            int i_col;    //prirustky
            int i_row;

            if (d_col<0) i_col= -1;    //nastaveni prirustku
            else i_col= 1;
            if (d_row<0) i_row= -1;
            else i_row= 1;

            Boolean jeToDobry=true;
            while(origin.row!=dest.row)
            {    //posuneme se na dalsi pozici
                origin.row=origin.row+i_row;
                origin.col=origin.row+i_col;
                //kontroluju, jestli je v ceste figurka
                if (this_board[origin.col][origin.row]!=null) jeToDobry=false;
            }
            return jeToDobry;
        }
        return false;
    }

    public boolean Move(Desk desk, Turn turn)
    {
        Position origin = turn.origin;
        Position dest = turn.destination;

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        //zavola fci na kontrolu validnosti pohybu
        if (!isMoveValid(desk, turn)) return false;
        else
        {    //kdyz je tah validni, presune figurku
            this_board[origin.col][origin.row]=null;
            this_board[dest.col][dest.row]=fig;
            return true;
        }
    }

    // kontroluje jestli je tah vyhozeni validni; pokud je validni, vraci pozici vyhazovane figury, jinak vraci null --------------------------------------------------------
    public static Position isTakeValid(Desk desk, Turn turn)
    {
        Position origin = turn.origin;
        Position dest = turn.destination;

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        Position pos=null;

        //kontrola rozsahu pozic
        if ((origin.col>7) || (origin.col<0)) return null;
        if ((origin.row>7) || (origin.row<0)) return null;
        if ((dest.col>7) || (dest.col<0)) return null;
        if ((dest.row>7) || (dest.row<0)) return null;

        //kontrola, jestli je pozice na kterou tahne, je volna
        if (this_board[dest.row][dest.col]!=null) return null;
        if (this_board[origin.row][origin.col]!=null) return null;

        //bily pesak
        if ( (fig.type=='P') && (fig.color=='W') )
        {
            //kontrola, jestli je dest o dva radky nize
            if (origin.row+2!=dest.row) return null;
            //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin cerna figurka
            Figure fig2=this_board[origin.col+1][origin.row+1];
            if ( (origin.col+2==dest.col) && (fig2!=null) && (fig2.type=='B') )
            {
                pos=new Position(origin.row+1, origin.col+1);
                return pos;
            }
            //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
            fig2=this_board[origin.col-1][origin.row+1];
            if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.type=='B') )
            {
                pos=new Position(origin.row+1, origin.col-1);
                return pos;
            }
            return null;
        }

        //cerny pesak
        if ( (fig.type=='P') && (fig.color=='B') )
        {
            //kontrola, jestli je dest o dva radky vyse
            if (origin.row-2!=dest.row) return null;
            //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin cerna figurka
            Figure fig2=this_board[origin.col+1][origin.row-1];
            if ( (origin.col+2==dest.col) && (fig2!=null) && (fig2.type=='W') )
            {
                pos=new Position(origin.row-1, origin.col+1);
                return pos;
            }
            //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
            fig2=this_board[origin.col-1][origin.row-1];
            if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.type=='W') )
            {
                pos=new Position(origin.row-1, origin.col-1);
                return pos;
            }
            return null;
        }

        //dama
        if (fig.type=='D')
        {
            //rozdil radku a sloupcu
            int d_col=dest.col-origin.col;
            int d_row=dest.row-origin.row;
            //dama se pohybuje libovolne po uhlopricce - je treba zkontolovat jestli se posunula o stejny pocet sloupcu jako radku
            if ( (Math.abs(d_row))!=(Math.abs(d_col)) ) return null;

            //prirustky
            int i_col;
            int i_row;
            if (d_col<0) i_col=(-1);    //nastaveni prirustku
            else i_col=(+1);
            if (d_row<0) i_row=(-1);
            else i_row=(+1);
            //ciatce vlastnich a oponentovych figur na draze damy
            int vlastnich=0;
            int oponentovych=0;
            while(origin.row!=dest.row)
            {    //posuneme se na dalsi pozici
                origin.row=origin.row+i_row;
                origin.col=origin.row+i_col;
                //kontroluju, jestli je v ceste figurka
                if (this_board[origin.col][origin.row]!=null)
                    if (this_board[origin.col][origin.row].type==fig.type) vlastnich++;
                    else
                    {
                        oponentovych++;
                        pos=new Position(origin.row, origin.col);    //ulozim si pozici vyhazovane figurky
                    }
            }
            if (vlastnich>0) return null;        //pri vyhazovani skace pres vlastni figurky
            if (oponentovych!=1) return null;    //pri vyhazovani skace pres vice nez jednu cernou nebo taky pres zadnou cernou
            return pos;
        }
        return null;
    }


    //brani----------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean Take(Desk desk, Turn turn)
    {
        Position origin = turn.origin;
        Position dest = turn.destination;

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        //zavola se fce na vyhodnoceni spravnosti brani
        Position vyhazovana=isTakeValid(desk, take);
        if (vyhazovana==null) return false;
        else
        {   //pokud je brani validni, presune se braci figurka a brana figurka je odstranena
            this_board[origin.col][origin.row]=null;
            this_board[vyhazovana.col][vyhazovana.row]=null;
            this_board[dest.col][dest.row]=fig;
            return true;
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Je tento tah proveditelný?
     * @param turn
     * @param desk
     * @return
     */
    public boolean isTurnValid(Turn turn, Desk desk)
    {
        //TODO barva kdo je na tahu odpovida origin

        //ismovevalid or istakevalid

        //jinak return false;
    }



    /**
     * Může kámen na této pozici(jestli tam vůbec je nějaký můj kámen) brát nějaký soupeřův kámen?
     * @param position
     * @param desk
     * @return
     */
    public boolean canTake(Position pos0, Desk desk)
    {//barva pos0 muze poslat jen hrac stejne barvy
        Figure[][] this_board=desk.getState();

        Figure fig = desk.getFigureAtPosition(pos0);

        for (int i=2; i<8; i++)
        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
            Position pos1 = new Position(pos0.row+i, pos0.col+i);
            Turn turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row+i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row-i, pos0.col+i);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row-i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
            if (this_board[pos0.col][pos0.row].type=='P') break;
        }
        return false;
    }


    public boolean canMove(Position pos0, Desk desk)
    {//barva pos0 muze poslat jen hrac stejne barvy
        Figure[][] this_board=desk.getState();
        Figure fig = desk.getFigureAtPosition(pos0);

        for (int i=1; i<8; i++)
        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici
            Position pos1 = new Position(pos0.row+i, pos0.col+i);
            Turn turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)!=true) return true;

            pos1 = new Position(pos0.row+i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)!=true) return true;

            pos1 = new Position(pos0.row-i, pos0.col+i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)!=true) return true;

            pos1 = new Position(pos0.row-i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)!=true) return true;

            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
            if (this_board[pos0.col][pos0.row].type=='P') break;
        }
        return false;
    }




}
