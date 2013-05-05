package gui;

import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dama.state.Settings;

/**
 * Trida vytvarejici vnitrni ramec s nastavenim nove P2P hry
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class NewP2PGameSettings extends JInternalFrame {

    private static final long serialVersionUID = 1403878987649253394L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    ButtonGroup local;
    private JTextField txtRemoteip;
    private JTextField txtRemotePort;

    public NewP2PGameSettings() {
        super("New P2P Game Settings",
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable

        setSize(425,430);

        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        //local
        JLabel lblLocalMachine = new JLabel("Player on Local Machine:");
        getContentPane().add(lblLocalMachine);

        JRadioButton rdbtnWhite = new JRadioButton("White");
        getContentPane().add(rdbtnWhite);

        JRadioButton rdbtnBlack = new JRadioButton("Black");
        getContentPane().add(rdbtnBlack);

        this.local = new ButtonGroup();
        this.local.add(rdbtnWhite);
        this.local.add(rdbtnBlack);

        //remote
        JLabel lblRemoteMachine = new JLabel("Remote Machine IP:");
        getContentPane().add(lblRemoteMachine);

        txtRemoteip = new JTextField();
        txtRemoteip.setText("localhost");
        getContentPane().add(txtRemoteip);
        txtRemoteip.setColumns(10);

        JLabel lblRemoteMachinePort = new JLabel("Remote Machine Port:");
        getContentPane().add(lblRemoteMachinePort);


        txtRemotePort = new JTextField();
        txtRemotePort.setText("55555");//49152 – 65535: Dynamic and/or Private Ports
        getContentPane().add(txtRemotePort);
        txtRemotePort.setColumns(10);
    }

    /**
     * Vraci nastaveni zvolene v dialogu nove P2P hry
     * @return Instance tridy Settings
     */
    public Settings getSettings(){

        JRadioButton local = Gui.getSelection(this.local);

        if(local == null || this.txtRemoteip.getText().trim().equals("") || this.txtRemotePort.getText().trim().equals("")){return null;}

        String remoteIP = this.txtRemoteip.getText();
        String remotePortStr = this.txtRemotePort.getText();

        String localStr = local.getText();

        String whiteStr = "Human";
        String blackStr = "Human";

        //TODO check hodnoty filter v Input

        try {Double.parseDouble(remotePortStr);
        }catch (NumberFormatException e) {
            return null;
        }

        int remotePort = (int)((Double.parseDouble(remotePortStr)));

        Settings sett = new Settings(whiteStr, blackStr, localStr, remoteIP, remotePort, 0);
        return sett;
    }
}
