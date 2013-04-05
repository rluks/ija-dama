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
    public static final int BOARD_SIZE = 8;

    //konstruktor---------------------------------------------------------------------------------------------------------------------------------------------------------
    public Desk()
    {
        this.board = new Figure[BOARD_SIZE][BOARD_SIZE];
        int col, row;

        //this.board[0][0] = Figure.FigureWhitePiece("Franta");
        //this.board[0][1] = Figure.FigureWhitePiece("Pepa");

//        for (row=0; row<BOARD_SIZE; row++)
//        {
//            for (col=0; col<BOARD_SIZE; col++)
//            {
//                //bile figurky na sude radky a sude sloupce (sude vzhledem k indexu do pole)
//                if ( ((row==0) || (row==2)) && (col%2==0) )
//                    this.board[row][col]=Figure.FigureWhitePiece("Pepa");
//
//                //bile figurky na lichy radek a liche sloupce (liche vzhledem k indexu do pole)
//                else if ( (row==1) && (col%2==1) )
//                    this.board[row][col]=Figure.FigureWhitePiece("Pepa");
//
//                //cerne figurky na lichych radcich a lichych sloupcich (liche vzhledem k indexu do pole)
//                else if ( ((row==5) || (row==7)) && (col%2==1) )
//                    this.board[row][col]=Figure.FigureBlackPiece("Darth Pepa");
//
//                //cerne figurky na sudem radku a sudych sloupcich (sude vzhledem k indexu do pole)
//                else if ( (row==6) && (col%2==0) )
//                    this.board[row][col]=Figure.FigureBlackPiece("Darth Pepa");
//
//                //vsecha ostatni pole jsou prazdna (null)
//                else this.board[row][col]=null;
//            }
//        }
    }

    //vraci ukazatel na hraci plochu (board)-----------------------------------------------------------------------------------------------------------------------------
    public Figure[][] getState()
    {
        return this.board;
    }

    //zmeni stav hraci plochy podle parametru----------------------------------------------------------------------------------------------------------------------------
    public void setState(Figure[][] board)
    {
        this.board=board;
    }

    //odstrani flag mustTake u vsech figur na hraci plose----------------------------------------------------------------------------------------------------------------
    public void clearMustTakeFlag()
    {
        int col,row;
        for (row=0; row<BOARD_SIZE; row++)
            for (col=0; col<BOARD_SIZE; col++)
                if (this.board[row][col]!=null) this.board[row][col].mustTakeFlag=false;
    }

  //TODO public void printBoard()

    public Figure getFigureAtPosition(Position position){
        if((position.row < 0) || (position.row > 7) || (position.col < 0) || (position.col > 7)){return null;}
        return this.board[position.row][position.col];
    }
}




















