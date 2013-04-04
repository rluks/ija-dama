package dama.operations;

import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;


public class Moves {

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
            while(origin!=dest)
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



    //nebude tohle nahodou potrebovat jeste turn? (to Roman: no shit sherlock)
    public void Move(Desk desk, Position origin, Position dest)
    {
        Figure[][] this_board=desk.getState();
        Figure fig=this_board[origin.col][origin.row];

        //pesak



    }

    public void Take(Desk desk){
//taky
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
