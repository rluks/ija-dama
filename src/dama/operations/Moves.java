package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;
import dama.io.Turn;

/**
 * Trida Moves se stara o vsechny operace souvisejici s pohybem figurek. Jedna se predevsim o
 * validitu tahu, kontroly zda dana figurka muze provest nejaky tah a vlastni provadeni tahu.
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class Moves
{

    /**
     * Kontroluje, jestli je pohyb figurkou mozny.
     * @param desk    nad jakou deskou se ma kontrola provest
     * @param turn_p    tak ktery se ma zkontrolovat
     * @return        vraci true pokud je tah validni, jinak vraci false
     */
    public static boolean isMoveValid(Desk desk, Turn turn_p)
    {
        Turn turn=new Turn(turn_p.playerColor, turn_p.origin, turn_p.destination);
        Position origin = new Position(turn_p.origin.row, turn_p.origin.col);
        Position dest = new Position(turn_p.destination.row, turn_p.destination.col);

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.row][origin.col];

        //kontrola rozsahu pozic
        if ((origin.col>7) || (origin.col<0)) return false;
        if ((origin.row>7) || (origin.row<0)) return false;
        if ((dest.col>7) || (dest.col<0)) return false;
        if ((dest.row>7) || (dest.row<0)) return false;

        //kontrola, jestli je pozice na a z ktere tahne, je volna a jestli je s cim tahnout
        if ( (this_board[dest.row][dest.col]!=null) || (this_board[origin.row][origin.col]==null) ) return false;

        //kontrola jestli barva figurky se kterou tahneme odpovida barve hrace na tahu
        if (fig.color!=turn.playerColor) return false;

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
                origin.col=origin.col+i_col;
                //kontroluju, jestli je v ceste figurka
                if (this_board[origin.row][origin.col]!=null) jeToDobry=false;
            }
            return jeToDobry;
        }
        return false;
    }

    /**
     * Provede tah s figurkou (pohyb ne brani)
     * @param desk    nad kterou deskou se ma pohyb vykonat
     * @param turn_p    jaky pohyb se ma vykonat
     * @return    vraci true, pokud byl tah uspesne proveden, jinak vraci false
     */
    public static boolean Move(Desk desk, Turn turn_p)
    {
        Turn turn = new Turn(turn_p);
        Position origin = new Position(turn.origin.row, turn.origin.col) ;
        Position dest = new Position(turn.destination.row, turn.destination.col)  ;

        Figure[][] this_board=desk.getState();

        if(origin.row < 0 || origin.col < 0){
            return false;
        }

        Figure fig=this_board[origin.row][origin.col];

        //zavola fci na kontrolu validnosti pohybu
        if (!isMoveValid(desk, turn)) return false;
        else
        {    //kdyz je tah validni, presune figurku
            this_board[origin.row][origin.col]=null;
            this_board[dest.row][dest.col]=fig;
            return true;
        }
    }

    /**
     * Kontroluje, jestli je brani figurkou mozne
     * @param desk     deska nad kterou se ma kontrola provest
     * @param turn_p     jaky tah se ma zkontrolovat
     * @return         vraci pozici vyhazovane figurky, pokud je brani mozne, jinak vraci null
     */
    public static Position isTakeValid(Desk desk, Turn turn_p)
    {
        Turn turn=new Turn(turn_p.playerColor, turn_p.origin, turn_p.destination);
        Position origin = new Position(turn_p.origin.row, turn_p.origin.col);
        Position dest = new Position(turn_p.destination.row, turn_p.destination.col);

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.row][origin.col];

        Position pos=null;

        //kontrola rozsahu pozic
        if ( ((origin.col>7) || (origin.col<0)) || ((origin.row>7) || (origin.row<0)) )return null;
        if (((dest.col>7) || (dest.col<0))||((dest.row>7) || (dest.row<0)))return null;

        //kontrola, jestli je pozice na a z ktere tahne, je volna
        if ( (this_board[dest.row][dest.col]!=null) || (this_board[origin.row][origin.col]==null) ) return null;

        //kontrola jestli barva figurky se kterou tahneme odpovida barve hrace na tahu
        if (fig.color!=turn.playerColor)return null;

        //bily pesak
        if ( (fig.type=='P') && (fig.color=='W') )
        {
            //kontrola, jestli je dest o dva radky nize
            if (origin.row+2!=dest.row) return null;
            if ((origin.row+1<8)&&(origin.col+1<8))
            {
                //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin cerna figurka
                Figure fig2=this_board[origin.row+1][origin.col+1];
                if ( (origin.col+2==dest.col) && (fig2!=null) && (fig2.color=='B') )
                {
                    pos=new Position(origin.row+1, origin.col+1);
                    return pos;
                }
            }
            if ((origin.row+1<8)&&(origin.col-1>=0))
            {
                //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
                Figure fig2=this_board[origin.row+1][origin.col-1];
                if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.color=='B') )
                {
                    pos=new Position(origin.row+1, origin.col-1);
                    return pos;
                }
            }
            return null;
        }

        //cerny pesak
        if ( (fig.type=='P') && (fig.color=='B') )
        {
            //kontrola, jestli je dest o dva radky vyse
            if (origin.row-2!=dest.row) return null;
            if ((origin.row-1>=0)&&(origin.col+1<8))
            {
                //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin bila figurka
                Figure fig2=this_board[origin.row-1][origin.col+1];
                if ( ((origin.col+2)==(dest.col)) )
                {
                    if  (fig2!=null)
                    {
                        if (fig2.color=='W')
                        {
                            pos=new Position(origin.row-1, origin.col+1);
                            return pos;
                        }
                    }
                }
            }
            if ((origin.row-1>=0)&&(origin.col-1>=0))
            {

                //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
                Figure fig2=this_board[origin.row-1][origin.col-1];
                if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.color=='W') )
                {
                    pos=new Position(origin.row-1, origin.col-1);
                    return pos;
                }
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
            //citace vlastnich a oponentovych figur na draze damy
            int vlastnich=0;
            int oponentovych=0;

            Position zaloha_origin = new Position(origin.row, origin.col);
            while(origin.row!=dest.row)
            {    //posuneme se na dalsi pozici
                origin.row=origin.row+i_row;
                origin.col=origin.col+i_col;

                //kontroluju, jestli je v ceste figurka
                if (this_board[origin.row][origin.col]!=null)
                {
                    if (this_board[origin.row][origin.col].color==fig.color) vlastnich++;
                    else
                    {
                        oponentovych++;
                        pos=new Position(origin.row, origin.col);    //ulozim si pozici vyhazovane figurky
                    }
                }
            }
            origin=zaloha_origin;
            if (vlastnich>0) return null;        //pri vyhazovani skace pres vlastni figurky
            if (oponentovych!=1) return null;    //pri vyhazovani skace pres vice nez jednu cernou nebo taky pres zadnou cernou

            return pos;
        }
        return null;
    }


    /**
     * Provede tah brani
     * @param desk    nad kterou deskou se ma brani provest
     * @param turn_p    s jakou figurkou a kam se ma tahnout
     * @return        vraci true, pokud bylo brani uspesne provedeno, jinak vraci false
     */
    public static boolean Take(Desk desk, Turn turn_p)
    {
        Turn turn=new Turn(turn_p.playerColor, turn_p.origin, turn_p.destination);
        Position origin = new Position(turn_p.origin.row, turn_p.origin.col);
        Position dest = new Position(turn_p.destination.row, turn_p.destination.col);

        Position zaloha_origin=new Position(origin.row, origin.col);
        Position zaloha_dest = new Position(dest.row, dest.col);

        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.row][origin.col];

        //zavola se fce na vyhodnoceni spravnosti brani
        Position vyhazovana=isTakeValid(desk, turn);
        if (vyhazovana==null) return false;
        else
        {   //pokud je brani validni, presune se braci figurka a brana figurka je odstranena
            this_board[zaloha_origin.row][zaloha_origin.col]=null;
            this_board[vyhazovana.row][vyhazovana.col]=null;
            this_board[zaloha_dest.row][zaloha_dest.col]=fig;
            return true;
        }
    }

    /**
     * Kontroluje jestli je tah proveditelny
     * @param turn_p    jaky tah se ma zkontrolovat
     * @param desk    nad jakou deskou se ma kontrola provest
     * @return        vraci true, pokud je tah validni, jinak vraci false
     */
    public static boolean isTurnValid(Turn turn_p, Desk desk)
    {
        Turn turn=new Turn(turn_p.playerColor, turn_p.origin, turn_p.destination);
        Figure[][] this_board=desk.getState();
        Figure fig=this_board[turn.origin.row][turn.origin.col];

        if(turn.origin.equals(turn.destination))
        {
            return false;
        }

        if (fig==null) return false;                            //zkontroluju jestli ej vubec s cim tahnout
        else if (fig.color!=turn.playerColor) return false;     //zkontroluju, jestli barva hrace na tahu odpovida barve figurky se kterou se tahne
        else if (Check.isMustTake(desk, turn.playerColor))
        {
            desk.clearMustTakeFlag();
            if (Moves.isTakeValid(desk, turn)!=null) return true;
            else return false;
        }
        else if (isMoveValid(desk, turn)==true) return true;

        return false;
    }



    /**
     * Provede test, zdali muze figurka na dane pozici nekoho brat
     * @param pos0_p    pozice testovane figurky
     * @param desk        deska nad kterou se ma test provest
     * @return            vraci true, pokud ma dana figurka koho brat, jinak vraci false
     */
    public static boolean canTake(Position pos0_p, Desk desk)
    {//barva pos0 muze poslat jen hrac stejne barvy

        Position pos0=new Position(pos0_p.row, pos0_p.col);
        Figure fig = desk.getFigureAtPosition(pos0);
        Position pos0_zaloha = new Position(pos0.row, pos0.col);

        for (int i=2; i<8; i++)
        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
            Position pos1 = new Position(pos0.row+i, pos0.col+i);
            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            Turn turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row+i, pos0.col-i);
            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row-i, pos0.col+i);
            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos1 = new Position(pos0.row-i, pos0.col-i);
            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            turn = new Turn(fig.color, pos0, pos1);
            if (Moves.isTakeValid(desk, turn)!=null) return true;

            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
            if (fig.type=='P') break;
        }
        return false;
    }


    /**
     * Provede test, jestli se figurka na dane pozici muze pohnout bez vyhazovani
     * @param pos0_p    pozice na ktere je dana figurka
     * @param desk        deska nad kterou se ma kontrola provest
     * @return            vraci true, pokud se muze figurka pohnout, jinak false
     */
    public static boolean canMove(Position pos0_p, Desk desk)
    {   //barva pos0 muze poslat jen hrac stejne barvy
        Position pos0=new Position(pos0_p.row, pos0_p.col);
        Figure[][] this_board=desk.getState();
        Figure fig = desk.getFigureAtPosition(pos0);
        Position pos0_zaloha = new Position(pos0.row, pos0.col);

        for (int i=1; i<8; i++)
        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici
            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            Position pos1 = new Position(pos0.row+i, pos0.col+i);
            Turn turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)==true) return true;

            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            pos1 = new Position(pos0.row+i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)==true) return true;

            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            pos1 = new Position(pos0.row-i, pos0.col+i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)==true) return true;

            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            pos1 = new Position(pos0.row-i, pos0.col-i);
            turn = new Turn(fig.color, pos0, pos1);
            if (isMoveValid(desk, turn)==true) return true;

            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
            if (this_board[pos0.row][pos0.col].type=='P') break;
        }
        return false;
    }




}
