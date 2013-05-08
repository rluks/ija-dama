package dama.operations;

import dama.state.*;
import dama.io.Turn;
import dama.operations.Check;
import dama.operations.Moves;
import java.util.Random;

/**
 * Trida ma jen jednu statickou metodu a tou je jed tah AI
 * @author Pavel Hala
 * @author Roman Luks
 */
public class AI
{
    /**
     * Provede jeden nahodny tah hrace (velice hloupa a jednoducha umela inteligence)
     * @param desk        odkaz na desku na ktere se provede tah AI
     * @param natahu    barva hrace, ktery je na tahu
     * @param nahodnost    mira jak chceme, aby byl tah nahodny
     * @return pokud vrati null, doslo k patu, jinak vraci Turn ktery vykonal
     */
    public static Turn AIMove (Desk desk, char natahu, int nahodnost)
    {
        //projistotu se omezi nahodnost na rozumne meze
        if (nahodnost<2) nahodnost=2;
        if (nahodnost>10) nahodnost=10;
        int preskoceni=0;    //indikator kolikrat se preskocilo kvuli mire nahodnosti

        int col,row;
        Figure[][] this_board=desk.getState();

        //nejdriv se poresi, jestli se musi brat - copypasta isMustTake
        if ( Check.isMustTake(desk, natahu) )
        {
            for (row=0; row<Desk.BOARD_SIZE; row++)
                for (col=0; col<Desk.BOARD_SIZE; col++)
                    if ( (this_board[row][col]!=null) )
                    {    //najde figurku hrace, ktery je na tahu
                        if (this_board[row][col].color==natahu)
                        {
                            Position pos0 = new Position(row, col);
                            Position pos0_zaloha = new Position(row, col);
                            int i;
                            for (i=2; i<8; i++)
                            {    //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
                                Position pos1 = new Position(row+i, col+i);
                                Position pos1_zaloha=new Position(row+i, col+i);
                                Turn turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    Moves.Take(desk, turn);
                                    return turn;
                                }

                                pos0 = new Position(pos0_zaloha.row, pos0_zaloha.col);
                                pos1 = new Position(pos0_zaloha.row+i, pos0_zaloha.col-i);
                                pos1_zaloha = new Position(pos0_zaloha.row+i, pos0_zaloha.col-i);
                                turn = new Turn(natahu, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    Moves.Take(desk, turn);
                                    return turn;
                                }

                                pos1 = new Position(row-i, col+i);
                                pos1_zaloha = new Position(row-i, col+i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    Moves.Take(desk, turn);
                                    return turn;
                                }

                                pos1 = new Position(row-i, col-i);
                                pos1_zaloha = new Position(row-i, col-i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    Moves.Take(desk, turn);
                                    return turn;
                                }

                                //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                                if (this_board[row][col].type=='P') break;
                            }
                        }

                    }
            return null;
        }

        //tady se nahodne tahne s nejakou fugurkou - copypasta canMove
        while(true)
        {
            for (row=0; row<Desk.BOARD_SIZE; row++)
                for (col=0; col<Desk.BOARD_SIZE; col++)
                    if ( (this_board[row][col]!=null) && (this_board[row][col].color==natahu) )
                    {
                        Position pos0 = new Position(row, col);;
                        Figure fig = desk.getFigureAtPosition(pos0);
                        Position pos0_zaloha = new Position(pos0.row, pos0.col);

                        Random generator = new Random();
                        int r = generator.nextInt()%nahodnost;
                        if (preskoceni<25)
                        {
                            if (r==1)
                            {
                                preskoceni++;
                                continue;
                            }
                        }
                        else if (preskoceni>1500) return null; //doslo k patu

                        for (int i=1; i<8; i++)
                        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici
                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            Position pos1 = new Position(pos0.row+i, pos0.col+i);
                            Position pos1_zaloha = new Position(pos0.row+i, pos0.col+i);
                            Turn turn = new Turn(natahu, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return turn;
                            }

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row+i, pos0.col-i);
                            pos1_zaloha = new Position(pos0.row+i, pos0.col-i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return turn;
                            }

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row-i, pos0.col+i);
                            pos1_zaloha = new Position(pos0.row-i, pos0.col+i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return turn;
                            }

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row-i, pos0.col-i);
                            pos1_zaloha = new Position(pos0.row-i, pos0.col-i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return turn;
                            }
                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);

                            //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                            if (this_board[pos0.row][pos0.col].type=='P') break;
                        }
                    }
            preskoceni=preskoceni+5;
        }
   } //konec metody

} //konec tridy
