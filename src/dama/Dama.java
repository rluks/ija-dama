package dama;

import dama.state.Desk;
import components.InternalFrameDemo;
import dama.operations.*;

/**
 * Hra Dáma v Javě
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Dama {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // projekt:
        // TODO javadoc

        /*
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InternalFrameDemo.createAndShowGUI();
            }
        });
        */
        Desk d = new Desk();
        System.out.println("Velka sachovnice:");
        d.printBoard();
        System.out.println("Mala sachovnice:");
        d.printSimpleBoard();


        int i=0;
        int j=0;
        while(!Check.isVictory(d))
        {
            boolean pom;
            if (i==0) pom=AI.AIMove(d, 'W');
            else pom=AI.AIMove(d, 'B');

            System.out.println(j + ": Stav booleanu: " + pom + "; i = " + i);

            /*
            if (pom==false)
            {
                System.out.println("Neco se posralo!!!!!");
                break;
            }
            */
            Check.isPieceToDame(d);
            d.clearMustTakeFlag();

            d.printSimpleBoard();

            i++;
            i=i%2;

            j++;
            //if (j==800) break;
        }



    }

}
