package dama;

import dama.io.Output;
import dama.io.Turn;
import dama.state.Position;

/**
 * Romanova main na prototypovani
 * @author Roman Lukš
 *
 */
public class Roman {

    public static void main(String[] args) {

        //TODO presun obrazku do slozky lib

        System.out.println("ROMAN");

        //Position o = new Position(0, 0);
        //Position d = new Position(4, 5);
        //Turn t = new Turn('W', o, d);
        //System.out.println(Output.getTurnXmlString(t));
        Dama.startingGameThread(50,50);
        //Player.startingPlayerThread();

    }
}
