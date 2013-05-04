package gui;

import java.awt.GridLayout;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * Trida vytvarejici vnitrni ramec vitezstvi
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Victory extends JInternalFrame {
    /**
     *
     */
    private static final long serialVersionUID = -1301563713986945975L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    private JLabel lblText;

    public Victory() {
        super("Victory",
              false, //resizable
              false, //closable
              false, //maximizable
              false);//iconifiable


        setSize(180,100);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        lblText = new JLabel("!!! Victory !!!");
        lblText.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblText);

    }
}
