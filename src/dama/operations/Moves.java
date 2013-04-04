package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;


public class Moves
{

    //kontroluje, jestli je tento pohyb figurkou mozny--------------------------------------------------------------------------------------------------------------------
    public boolean isMoveValid(Desk desk, Position origin, Position dest)
    {
        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        //kontrola rozsahu pozic
        if ((origin.col>7) || (origin.col<0)) return false;
        if ((origin.row>7) || (origin.row<0)) return false;
        if ((dest.col>7) || (dest.col<0)) return false;
        if ((dest.row>7) || (dest.row<0)) return false;
        //kontrola, jestli je pozice na kterou tahne, je volna
        if (this_board[dest.col][dest.row]!=null) return false;

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
            int d_col=dest.col-origin.col;
            int d_row=dest.row-origin.row;

            //dama se pohybuje libovolne po uhlopricce - je treba zkontolovat jestli se posunula o stejny pocet sloupcu jako radku
            if ( (Math.abs(d_row))!=(Math.abs(d_col)) ) return false;

            int i_col;    //prirustky
            int i_row;
            if (d_col<0) i_col=(-1);    //nastaveni prirustku
            else i_col=(+1);
            if (d_row<0) i_row=(-1);
            else i_row=(+1);

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



    //nebude tohle nahodou potrebovat jeste turn? (to Roman: no shit sherlock)-----------------------------------------------------------------------------------------------
    public boolean Move(Desk desk, Position origin, Position dest)
    {
        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];
        //zavola fci na kontrolu validnosti pohybu
        if (isMoveValid(desk, origin, dest)==false) return false;
        else
        {    //kdyz je tah validni, presune figurku
            this_board[origin.col][origin.row]=null;
            this_board[dest.col][dest.row]=fig;
            return true;
        }
    }


    //
    public boolean Take(Desk desk, Position origin, Position dest)
    {
        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        //kontrola, jestli je pozice dest volna a jestli je na pozici origin nějaka figurka
        if ( (this_board[dest.col][dest.row]!=null) || (fig==null) ) return false;

        //bily pesak
        if ( (fig.type=='P') && (fig.color=='W') )
        {
            //kontrola, jestli je dest o dva radky nize
            if (origin.row+2!=dest.row) return false;
            //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin cerna figurka
            Figure fig2=this_board[origin.col+1][origin.row+1];
            if ( (origin.col+2==dest.col) && (fig2!=null) && (fig2.type=='B') )
            {
                this_board[origin.col+1][origin.row+1]=null;    //odstraneni vyhozene figurky
                this_board[origin.col][origin.row]=null;        //presun figurky
                this_board[dest.col][dest.row]=fig;
                return true;
            }
            //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
            fig2=this_board[origin.col-1][origin.row+1];
            if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.type=='B') )
            {
                this_board[origin.col-1][origin.row+1]=null;    //odstraneni vyhozene figurky
                this_board[origin.col][origin.row]=null;        //presun figurky
                this_board[dest.col][dest.row]=fig;
                return true;
            }
            return false;
        }
        //cerny pesak
        if ( (fig.type=='P') && (fig.color=='B') )
        {
            //kontrola, jestli je dest o dva radky vyse
            if (origin.row-2!=dest.row) return false;
            //kontrola jestli je dest o dva sloupce doprava a je mezi dest a origin cerna figurka
            Figure fig2=this_board[origin.col+1][origin.row-1];
            if ( (origin.col+2==dest.col) && (fig2!=null) && (fig2.type=='W') )
            {
                this_board[origin.col+1][origin.row-1]=null;    //odstraneni vyhozene figurky
                this_board[origin.col][origin.row]=null;        //presun figurky
                this_board[dest.col][dest.row]=fig;
                return true;
            }
            //kontrola jestli je dest o dva sloupce doleva a je mezi dest a origin cerna figurka
            fig2=this_board[origin.col-1][origin.row-1];
            if ( (origin.col-2==dest.col) && (fig2!=null) && (fig2.type=='W') )
            {
                this_board[origin.col-1][origin.row-1]=null;    //odstraneni vyhozene figurky
                this_board[origin.col][origin.row]=null;        //presun figurky
                this_board[dest.col][dest.row]=fig;
                return true;
            }
            return false;
        }
        //dama
        if (fig.type=='D')
        {
            //rozdil radku a sloupcu
            int d_col=dest.col-origin.col;
            int d_row=dest.row-origin.row;

            //dama se pohybuje libovolne po uhlopricce - je treba zkontolovat jestli se posunula o stejny pocet sloupcu jako radku
            if ( (Math.abs(d_row))!=(Math.abs(d_col)) ) return false;

            int i_col;    //prirustky
            int i_row;
            if (d_col<0) i_col=(-1);    //nastaveni prirustku
            else i_col=(+1);
            if (d_row<0) i_row=(-1);
            else i_row=(+1);

            int vlastnich=0;
            int oponentovych=0;
            Position vyhazovana = null;

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
                        vyhazovana=new Position(origin.row, origin.col);    //ulozim si pozici vyhazovane figurky
                    }
            }
            if (vlastnich>0) return false;        //pri vyhazovani skace pres vlastni figurky
            if (oponentovych!=1) return false;    //pri vyhazovani skace pres vice nez jednu cernou nebo taky pres zadnou cernou

            this_board[origin.col][origin.row]=null;
            this_board[vyhazovana.col][vyhazovana.row]=null;
            this_board[dest.col][dest.row]=fig;
            return true;
        }
        return false;
    }

    /**
     * Je tento tah proveditelný?
     * @param turn
     * @param desk
     * @return
     */
    public boolean isTurnValid(Turn turn, Desk desk){

    }



    /**
     * Může kámen na této pozici(jestli tam vůbec je nějaký můj kámen) brát nějaký soupeřův kámen?
     * @param position
     * @param desk
     * @return
     */
    public boolean canTake(Position position, Desk desk){

    }


    public boolean canMove(Position position, Desk desk){

    }




}
