package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import dama.state.Settings;
import java.awt.Color;

/**
 * Trida vytvarejici vnitrni ramec s nastavenim nove P2P hry
 * @author Roman Lukš
 * @author Pavel Hála
 */
public class NewP2PGameSettings extends JInternalFrame {

    private static final long serialVersionUID = 1403878987649253394L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    ButtonGroup typ;
    ButtonGroup local;
    private JTextField txtRemoteip;
    private JTextField txtRemotePort;
    private JTextField txtCesta;
    private JButton    btnCesta;
    public JLabel        padding;

    /**
     * Konstruktor. 
     */
    public NewP2PGameSettings() {
        super("New P2P Game Settings",
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable

        setSize(425,430);

        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        JLabel lblPopisekPeer=new JLabel("Type of peer:");
        getContentPane().add(lblPopisekPeer);
        JRadioButton rdbtnListen = new JRadioButton("Listen on port (fill only port)");
        getContentPane().add(rdbtnListen);
        JRadioButton rdbtnConnectTo = new JRadioButton("Connect to other PC (fill all)");
        getContentPane().add(rdbtnConnectTo);
        this.typ = new ButtonGroup();
        this.typ.add(rdbtnConnectTo);
        this.typ.add(rdbtnListen);

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

        JLabel lblRemoteMachinePort = new JLabel("Remote Machine Port/Listening port:");
        getContentPane().add(lblRemoteMachinePort);

        txtRemotePort = new JTextField();
        txtRemotePort.setText("55555");//49152 – 65535: Dynamic and/or Private Ports
        getContentPane().add(txtRemotePort);
        txtRemotePort.setColumns(10);

        JLabel lblPopisekCesta=new JLabel("Path to saved game (optional):");
        getContentPane().add(lblPopisekCesta);

        this.txtCesta=new JTextField();
        this.txtCesta.setText("");
        getContentPane().add(txtCesta);

        this.btnCesta=new JButton();
        this.btnCesta.setText("Browse");
        getContentPane().add(btnCesta);
        btnCesta.addActionListener(new OpenL());

        this.padding=new JLabel("");
        this.padding.setForeground(Color.RED);
        getContentPane().add(this.padding);

    }

    /**
     * Zajisti to, ze kdyz se klikne na tlacitko browse, otevre se okno s vyberem souboru
     * @author Pavel Hála
     * @author Roman Lukš
     */
    class OpenL implements ActionListener
    {
          public void actionPerformed(ActionEvent e)
          {
                //System.out.println("Action listener:" + e.getActionCommand());
                JFileChooser c = new JFileChooser(".\\examples");
                FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter("xml files (*.xml)", "xml");
                c.setFileFilter(xmlfilter);
                FileNameExtensionFilter xmlfilter2 = new FileNameExtensionFilter("standart notation (.txt)", "txt");
                c.setFileFilter(xmlfilter2);

                int rVal = c.showOpenDialog(NewP2PGameSettings.this);
                if (rVal == JFileChooser.APPROVE_OPTION)
                {
                      //TFCesta.setText(c.getCurrentDirectory().toString() + "\\" + c.getSelectedFile().getName());
                    txtCesta.setText((String)c.getSelectedFile().getAbsolutePath());
                }
          }
    }

    /**
     * Vraci nastaveni zvolene v dialogu nove P2P hry
     * @return Instance tridy Settings
     */
    public Settings getSettings(){

        JRadioButton typ= Gui.getSelection(this.typ);

        if (typ==null)
        {
            //System.out.println("Nebyl vybran typ sitoveho klienta");
            padding.setText("Choose peer type!");
            return null;
        }

        JRadioButton local = Gui.getSelection(this.local);


        if (this.txtRemotePort.getText().trim().equals(""))
        {
            padding.setText("Fill port number!");
            return null;
        }
        String remotePortStr = this.txtRemotePort.getText();

        try
        {
            Double.parseDouble(remotePortStr);
        }
        catch (NumberFormatException e)
        {
            padding.setText("Invalid port number");
            return null;
        }

        int remotePort = (int)((Double.parseDouble(remotePortStr)));

        Settings sett;
        if (typ.getText().equals("Listen on port (fill only port)"))
        {
            sett=new Settings("Human", "Human", null, null, remotePort, 0, null);
        }
        else if (typ.getText().equals("Connect to other PC (fill all)"))
        {
            if(local == null || this.txtRemoteip.getText().trim().equals(""))
            {
                padding.setText("Choose your color and/or fill remote IP!");
                return null;
            }
            String remoteIP = this.txtRemoteip.getText();
            String localStr = local.getText();
            String cesta=txtCesta.getText();
            if (cesta.equals(""))cesta=null;
            else
            {
                if (!((cesta.endsWith(".txt") || cesta.endsWith(".xml"))))
                {
                    padding.setText("Choose \".txt\", \".xml\" or no file at all!");
                    return null;
                }
                File f = new File(cesta);
                if(!f.exists())
                {
                    padding.setText("Unable to open file(probably doesnt exist)!");
                    return null;
                }

            }

            sett= new Settings("Human", "Human", localStr, remoteIP, remotePort, 0, cesta);
        }
        else
        {
            padding.setText("Unexpected error.");
            return null;
        }

        return sett;
    }
}
