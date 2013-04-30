package dama.operations;

import dama.state.*;
import dama.io.Turn;
import dama.operations.Check;
import dama.operations.Moves;
import java.util.Random;

public class AI
{
    //pokud vrati false, doslo k patu
    public static boolean AIMove (Desk desk, char natahu, int nahodnost)
    {
        //Figure[][] this_board=desk.getState();
        if (nahodnost<2) nahodnost=2;
        if (nahodnost>8) nahodnost=8;
        int preskoceni=0;

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
                                Position pos1_zaloha=new Position(row+i, col+i);
                                Turn turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    /*
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!1: " + pom);
                                    if (pom==true) return true;
                                    */
                                    Moves.Take(desk, turn);
                                    return true;
                                }
                                pos0 = new Position(pos0_zaloha.row, pos0_zaloha.col);
                                pos1 = new Position(pos0_zaloha.row+i, pos0_zaloha.col-i);
                                pos1_zaloha = new Position(pos0_zaloha.row+i, pos0_zaloha.col-i);
                                turn = new Turn(natahu, pos0, pos1);
                                //System.out.println("Pos0: " + pos0.row + ":" + pos0.col);
                                //System.out.println("Pos1: " + pos1.row + ":" + pos1.col);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    /*
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!2: " + pom);
                                    if (pom==true) return true;
                                    */
                                    Moves.Take(desk, turn);
                                    return true;
                                }
                                pos1 = new Position(row-i, col+i);
                                pos1_zaloha = new Position(row-i, col+i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    /*
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!3: " + pom);
                                    if (pom==true) return true;
                                    */
                                    Moves.Take(desk, turn);
                                    return true;
                                }
                                pos1 = new Position(row-i, col-i);
                                pos1_zaloha = new Position(row-i, col-i);
                                turn = new Turn(this_board[row][col].color, pos0, pos1);
                                if (Moves.isTakeValid(desk, turn)!=null)
                                {
                                    turn = new Turn(natahu, pos0_zaloha, pos1_zaloha);
                                    /*
                                    boolean pom=Moves.Take(desk, turn);
                                    System.out.println("\tMusi se brat!4: " + pom);
                                    if (pom==true) return true;
                                    */
                                    Moves.Take(desk, turn);
                                    return true;
                                }
                                //pokud je figurka pesak, tak se nemusi kontrolovat cela uhlopricka
                                if (this_board[row][col].type=='P') break;
                            }
                        }

                    }
            //System.out.println("\tMusi se brat !false!");
            return false;
        }

        System.out.println("Bude se tahnout!0");

        Figure[][] this_board=desk.getState();
        while(true)
        {
            int col,row;
            //Boolean priznak_brani=false;
            for (row=0; row<Desk.BOARD_SIZE; row++)
                for (col=0; col<Desk.BOARD_SIZE; col++)
                    if ( (this_board[row][col]!=null) && (this_board[row][col].color==natahu) )
                    {
                        Position pos0 = new Position(row, col);;
                        Figure fig = desk.getFigureAtPosition(pos0);
                        Position pos0_zaloha = new Position(pos0.row, pos0.col);

                        Random generator = new Random();
                        int r = generator.nextInt()%nahodnost;
                        if (preskoceni<24)
                        {
                            if (r==1)
                            {
                                preskoceni++;
                                continue;
                            }
                        }
                        else if (preskoceni>1000) return false; //doslo k patu

                        for (int i=1; i<8; i++)
                        {   //po obou uhloprickach jede v obouch smerech a kontroluje jestli muze figurka skocit na danou pozici

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            Position pos1 = new Position(pos0.row+i, pos0.col+i);
                            Position pos1_zaloha = new Position(pos0.row+i, pos0.col+i);
                            Turn turn = new Turn(natahu, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                /*
                                boolean pom=Moves.Move(desk, turn);
                                System.out.println("\tBude se tahnout!1" + pom);
                                if (pom==true) return true;
                                */
                                //System.out.println("\tBude se tahnout!1");
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return true;
                            }

                            //System.out.println("Pesak 2");

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row+i, pos0.col-i);
                            pos1_zaloha = new Position(pos0.row+i, pos0.col-i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                /*
                                boolean pom=Moves.Move(desk, turn);
                                System.out.println("\tBude se tahnout!2" + pom);
                                if (pom==true) return true;
                                */
                                //System.out.println("\tBude se tahnout!2");
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return true;
                            }

                            //System.out.println("Pesak 3");

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row-i, pos0.col+i);
                            pos1_zaloha = new Position(pos0.row-i, pos0.col+i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                /*
                                boolean pom=Moves.Move(desk, turn);
                                System.out.println("\tBude se tahnout!3" + pom);
                                if (pom==true) return true;
                                */
                                //System.out.println("\tBude se tahnout!3");
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return true;
                            }

                            //System.out.println("Pesak 4");

                            pos0=new Position(pos0_zaloha.row, pos0_zaloha.col);
                            pos1 = new Position(pos0.row-i, pos0.col-i);
                            pos1_zaloha = new Position(pos0.row-i, pos0.col-i);
                            turn = new Turn(fig.color, pos0, pos1);
                            if (Moves.isMoveValid(desk, turn)==true)
                            {
                                /*
                                boolean pom=Moves.Move(desk, turn);
                                System.out.println("\tBude se tahnout!4" + pom);
                                if (pom==true) return true;
                                */
                                //System.out.println("\tBude se tahnout!4");
                                turn=new Turn (natahu, pos0_zaloha, pos1_zaloha);
                                Moves.Move(desk, turn);
                                return true;
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

            //System.out.println("\tBude se tahnout!5 false" + preskoceni);
            preskoceni=preskoceni+5;
        }
        //return false;
   }

}
