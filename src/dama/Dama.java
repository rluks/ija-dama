package dama;

import dama.state.Desk;
import components.InternalFrameDemo;;

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

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InternalFrameDemo.createAndShowGUI();
            }
        });

        Desk d = new Desk();
        System.out.println("Velka sachovnice:");
        d.printBoard();
        System.out.println("Mala sachovnice:");
        d.printSimpleBoard();

    }

}
