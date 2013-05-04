package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.TextArea;


/**
 * Trida vytvarejici vnitrni ramec chyby
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Fail extends JInternalFrame {


    /**
    *
    */
    private static final long serialVersionUID = -3655108386637594154L;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    public Fail(String text) {
        super("Fail !",
              false, //resizable
              false, //closable
              false, //maximizable
              false);//iconifiable


        setSize(300,420);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);

        try {
            URL url;
            url = new URL("file", "localhost", "src/gui/images/bbf.gif");
            ImageIcon ii = new ImageIcon(url);

            GridBagLayout gridBagLayout = new GridBagLayout();
            gridBagLayout.columnWidths = new int[]{284, 0};
            gridBagLayout.rowHeights = new int[]{271, 0, 0};
            gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
            gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
            getContentPane().setLayout(gridBagLayout);

            JLabel label = new JLabel(ii);
            label.setAutoscrolls(true);
            GridBagConstraints gbc_label = new GridBagConstraints();
            gbc_label.insets = new Insets(0, 0, 5, 0);
            gbc_label.fill = GridBagConstraints.BOTH;
            gbc_label.gridx = 0;
            gbc_label.gridy = 0;
            getContentPane().add(label, gbc_label);

            TextArea textArea = new TextArea();
            GridBagConstraints gbc_textArea = new GridBagConstraints();
            gbc_textArea.gridx = 0;
            gbc_textArea.gridy = 1;
            getContentPane().add(textArea, gbc_textArea);
            textArea.setText(text);

        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }
}
