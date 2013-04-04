package dama.state;

import dama.state.Figure;;

/**
 * Třída Desk uchovává stav hry na šachovnici
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Desk {
    protected Figure[][] board;
    protected static final int BOARD_SIZE = 8;

    public Desk()
    {
        this.board = new Figure[BOARD_SIZE][BOARD_SIZE];
        int col,row;
        for (row=0; row<BOARD_SIZE; row++)
            for (col=0; col<BOARD_SIZE; col++)
            {
                //bile figurky na sude radky a sude sloupce (sude vzhledem k indexu do pole)
                if ( ((row==0) || (row==2)) && (col%2==0) )
                    this.board[col][row]=FigureWhitePiece("Pepa");

                //bile figurky na lichy radek a liche sloupce (liche vzhledem k indexu do pole)
                else if ( (row==1) && (col%2==1) )
                    this.board[col][row]=FigureWhitePiece("Pepa");

                //cerne figurky na lichych radcich a lichych sloupcich (liche vzhledem k indexu do pole)
                else if ( ((row==5) || (row==7)) && (col%2==1) )
                    this.board[col][row]=FigureBlackPiece("Darth Pepa");

                //cerne figurky na sudem radku a sudych sloupcich (sude vzhledem k indexu do pole)
                else if ( (row==6) && (col%2==0) )
                    this.board[col][row]=FigureBlackPiece("Darth Pepa");

                //vsecha ostatni pole jsou prazdna (null)
                else this.board[col][row]=null;
            }
    }

    public Figure[][] getState()
    {
        return board;
    }

    public void setState(Figure[][] figure)
    {
        this.board=figure;
    }

    public int getBoardSize()
    {
        return BOARD_SIZE;
    }

    public void clearMustTakeFlag()
    {
        int col,row;
        for (row=0; row<BOARD_SIZE; row++)
            for (col=0; col<BOARD_SIZE; col++)
            {
                if (this.board[col][row]!=null)
                    this.board[col][row].mustTakeFlag=false;
            }
    }
}





























