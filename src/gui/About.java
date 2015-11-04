package gui;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dama.state.Settings;


/**
 * Trida vytvarejici vnitrni ramec about
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class About extends JInternalFrame {

    private static final long serialVersionUID = -4326238455841218297L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    /**
     * Konstruktor.
     */
    public About() {
        super("About",
              false, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable


        setSize(300,100);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JLabel lblBlackPlayer = new JLabel("Pavel Hála & Roman Lukš (2013)", SwingConstants.CENTER);
        getContentPane().add(lblBlackPlayer);

    }
}
