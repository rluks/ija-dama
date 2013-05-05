package dama;

import gui.PlayerGui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Romanova main na prototypovani
 * @author Roman Lukš
 *
 */
public class Roman {

    public static void main(String[] args) {

        //TODO presun obrazku do slozky lib

        System.out.println("ROMAN");

        //Dama.startingGameThread();
        Player.startingPlayerThread();

        /*
        PlayerGui p;
        p = new PlayerGui();
        p.setVisible(true);
        p.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Okno zavreno, ukoncuji...");
                System.exit(0);
            }
        } );
        */

    }
}
