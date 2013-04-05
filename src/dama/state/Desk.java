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

        this.board[0][0] = Figure.FigureWhitePiece("Stan");
        this.board[0][2] = Figure.FigureWhitePiece("Randy");
        this.board[0][4] = Figure.FigureWhitePiece("Sharon");
        this.board[0][6] = Figure.FigureWhitePiece("Shelley");
        this.board[1][1] = Figure.FigureWhitePiece("Kyle");
        this.board[1][3] = Figure.FigureWhitePiece("Gerald");
        this.board[1][5] = Figure.FigureWhitePiece("Sheila");
        this.board[1][7] = Figure.FigureWhitePiece("Ike");
        this.board[2][0] = Figure.FigureWhitePiece("Kenny");
        this.board[2][2] = Figure.FigureWhitePiece("Jesus");
        this.board[2][4] = Figure.FigureWhitePiece("Ned");
        this.board[2][6] = Figure.FigureWhitePiece("Butters");

        this.board[5][1] = Figure.FigureBlackPiece("Eric");
        this.board[5][3] = Figure.FigureBlackPiece("Mysterion");
        this.board[5][5] = Figure.FigureBlackPiece("Mr. Black");
        this.board[5][7] = Figure.FigureBlackPiece("Mrs. Black");
        this.board[6][0] = Figure.FigureBlackPiece("Chef");
        this.board[6][2] = Figure.FigureBlackPiece("Mr. Slave");
        this.board[6][4] = Figure.FigureBlackPiece("Manbearpig");
        this.board[6][6] = Figure.FigureBlackPiece("Darth Chef");
        this.board[7][1] = Figure.FigureBlackPiece("Token");
        this.board[7][3] = Figure.FigureBlackPiece("Mr. Kitty");
        this.board[7][5] = Figure.FigureBlackPiece("Willzyx");
        this.board[7][7] = Figure.FigureBlackPiece("Professor Chaos");

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




















