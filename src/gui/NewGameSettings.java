package gui;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dama.state.Settings;


/**
 * Trida vytvarejici vnitrni ramec s nastavenim nove hry
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class NewGameSettings extends JInternalFrame {
    /**
     *
     */
    private static final long serialVersionUID = -4326238455841218297L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    ButtonGroup white;
    ButtonGroup black;

    private JTextField txtAiWait;

    public NewGameSettings() {
        super("New Game Settings",
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable


        setSize(425,430);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        //white
        JLabel lblWhitePlayer = new JLabel("White Player:");
        getContentPane().add(lblWhitePlayer);

        JRadioButton rdbtnWhiteHuman = new JRadioButton("Human");
        getContentPane().add(rdbtnWhiteHuman);

        JRadioButton rdbtnWhiteAI = new JRadioButton("AI");
        getContentPane().add(rdbtnWhiteAI);

        this.white = new ButtonGroup();
        this.white.add(rdbtnWhiteHuman);
        this.white.add(rdbtnWhiteAI);

        //black
        JLabel lblBlackPlayer = new JLabel("Black Player:");
        getContentPane().add(lblBlackPlayer);

        JRadioButton rdbtnBlackHuman = new JRadioButton("Human");
        getContentPane().add(rdbtnBlackHuman);

        JRadioButton rdbtnBlackAI = new JRadioButton("AI");
        getContentPane().add(rdbtnBlackAI);

        this.black = new ButtonGroup();
        this.black.add(rdbtnBlackHuman);
        this.black.add(rdbtnBlackAI);

        JLabel lblAiWait = new JLabel("AI waiting time:");
        getContentPane().add(lblAiWait);

        txtAiWait = new JTextField();
        txtAiWait.setText("300");//49152 – 65535: Dynamic and/or Private Ports
        getContentPane().add(txtAiWait);
        txtAiWait.setColumns(10);

    }

    /**
     * Vraci nastaveni zvolene v dialogu nove hry
     * @return Instance tridy Settings
     */
    public Settings getSettings(){

        JRadioButton white = Gui.getSelection(this.white);
        JRadioButton black = Gui.getSelection(this.black);
        if(white == null || black == null){return null;}

        if(this.txtAiWait.getText().trim().equals("")){return null;}

        String whiteStr = white.getText();
        String blackStr = black.getText();

        String aiWaitStr = this.txtAiWait.getText();

        try {Double.parseDouble(aiWaitStr);
        }catch (NumberFormatException e) {
            return null;
        }

        int aiWait = (int)((Double.parseDouble(aiWaitStr)));

        Settings sett = new Settings(whiteStr, blackStr, null, null, 0, aiWait);
        return sett;
    }
}
