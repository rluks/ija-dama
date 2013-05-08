package gui;
import java.awt.GridLayout;
import javax.swing.JInternalFrame;
import javax.swing.JTextArea;


/**
 * Trida vytvarejici vnitrni ramec pro vlozeni notace
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class InsertNotation extends JInternalFrame {

    private static final long serialVersionUID = -5780383886523331250L;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;


    public JTextArea txtrNotation;
    /**
     * Konstruktor.
     */
    public InsertNotation() {
        super("Insert notation",
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable


        setSize(425,430);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(2, 1, 0, 0));

        txtrNotation = new JTextArea();
        txtrNotation.setText("1. e3-f4 f6-e5\n");
        getContentPane().add(txtrNotation);

    }
}
