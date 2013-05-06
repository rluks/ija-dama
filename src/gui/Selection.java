package gui;

import java.awt.GridLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;


/**
 * Trida vytvarejici vnitrni ramec vybreme nacteni ulozene hry nebo zadani v notaci
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Selection extends JInternalFrame {

    /**
    *
    */
    private static final long serialVersionUID = 3697168454857220736L;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;


    /**
     * Konstruktor.
     */
    public Selection() {
        super("Select",
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable


        setSize(425,430);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));



    }
}
