package dama.operations;

import dama.state.Desk;

public class Moves {


    public void Move(Desk desk){
//nebude tohle nahodou potrebovat jeste turn?
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
