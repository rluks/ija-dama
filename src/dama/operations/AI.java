package dama.operations;

import dama.state.*;
import dama.io.Turn;
import dama.operations.Check;
import dama.operations.Moves;

public class AI
{
    public static boolean AIMove (Desk desk, char natahu)
    {
        //Figure[][] this_board=desk.getState();

        if ( Check.isMustTake(desk, natahu) )
        {
            System.out.println("Musi se brat!0");
            Figure[][] this_board=desk.getState();
            int col,row;
            for (row=0; row<Desk.BOARD_SIZE; row++)
                for (col=0; col<Desk.BOARD_SIZE; col++)
                    if ( (this_board[row][col]!=null) )
                    {    //najde figurku hrace, ktery je na tahu
                        if (this_board[row][col].color==natahu)
                        {
                            //System.out.println("row: " + row + "; col: " +  col);

                            Position pos0 = new Position(row, col);
                            Position pos0_zaloha = new Position(row, col);
                            int i;
                            for (i=2; i<8; i++)
                            {    //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici pohybem vyhozeni
                                 //System.out.println("\t" + i + ".");

                                //System.out.println("for!!!!!!!!!!!!!!!!!!!!");

                                Position pos1 = new Position(row+i, col+i);
                                Turn turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!1: " + pom);
                                    if (pom==true) return true;
                                }
                                pos0 = new Position(pos0_zaloha.row, pos0_zaloha.col);
                                pos1 = new Position(row+i, col-i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!2: " + pom);
                                    if (pom==true) return true;
                                }
                                pos1 = new Position(row-i, col+i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!3: " + pom);
                                    if (pom==true) return true;
                                }
                                pos1 = new Position(row-i, col-i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!4: " + pom);
                                    if (pom==true) return true;
                                }
                                //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                                if (this_board[row][col].type=='P') break;
                            }
                        }

                    }
            //System.out.println("false");
            return false;
        }

        System.out.println("Bude se tahnout!0");

        Figure[][] this_board=desk.getState();

        int col,row;
        //Boolean priznak_brani=false;
        for (row=0; row<Desk.BOARD_SIZE; row++)
            for (col=0; col<Desk.BOARD_SIZE; col++)
                if ( (this_board[row][col]!=null) && (this_board[row][col].color==natahu) )
                {
                    Position pos0 = new Position(row, col);;
                    Figure fig = desk.getFigureAtPosition(pos0);
                    Position pos0_zaloha = new Position(pos0.row, pos0.col);


                    for (int i=1; i<8; i++)
                    {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici

                        pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                        Position pos1 = new Position(pos0.row+i, pos0.col+i);
                        Turn turn = new Turn(fig.color, pos0, pos1);
                        if (Moves.isMoveValid(desk, turn)==true)
                        {
                            boolean pom=Moves.Move(desk, turn);
                            System.out.println("\tBude se tahnout!1" + pom);
                            if (pom==true) return true;

                        }

                        //System.out.println("Pesak 2");

                        pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                        pos1 = new Position(pos0.row+i, pos0.col-i);
                        turn = new Turn(fig.color, pos0, pos1);
                        if (Moves.isMoveValid(desk, turn)==true)
                        {
                            boolean pom=Moves.Move(desk, turn);
                            System.out.println("\tBude se tahnout!2" + pom);
                            if (pom==true) return true;

                        }

                        //System.out.println("Pesak 3");

                        pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                        pos1 = new Position(pos0.row-i, pos0.col+i);
                        turn = new Turn(fig.color, pos0, pos1);
                        if (Moves.isMoveValid(desk, turn)==true)
                        {
                            boolean pom=Moves.Move(desk, turn);
                            System.out.println("\tBude se tahnout!3" + pom);
                            if (pom==true) return true;

                        }

                        //System.out.println("Pesak 4");

                        pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                        pos1 = new Position(pos0.row-i, pos0.col-i);
                        turn = new Turn(fig.color, pos0, pos1);
                        if (Moves.isMoveValid(desk, turn)==true)
                        {
                            boolean pom=Moves.Move(desk, turn);
                            System.out.println("\tBude se tahnout!4" + pom);
                            if (pom==true) return true;

                        }

                        //System.out.println("Pesak 5");

                        pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                        //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                        if (this_board[pos0.row][pos0.col].type=='P')
                        {
                            //System.out.println("Pesak end");
                            break;
                        }
                    }
                }

        return false;
   }

}
