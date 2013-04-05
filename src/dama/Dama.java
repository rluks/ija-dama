package dama;

import dama.state.Desk;

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
        // ukol3:
        // TODO junit testy
        // TODO ant

        // projekt:
        // TODO javadoc

        Desk d = new Desk();
        System.out.println("Velka sachovnice:");
        d.printBoard();
        System.out.println("Mala sachovnice:");
        d.printSimpleBoard();

    }

}
