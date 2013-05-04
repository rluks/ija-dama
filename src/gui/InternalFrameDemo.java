/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import dama.Dama;
import dama.io.Output;
import dama.io.Turn;
import dama.operations.AI;
import dama.operations.Check;
import dama.state.Desk;
import dama.state.Figure;
import dama.state.Position;
import dama.state.Settings;



/**
 * Uzivatelske rozhrani pro hru Dama
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class InternalFrameDemo extends JFrame
                               implements ActionListener, MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 7275553945417568909L;

    JDesktopPane desktop;

    //nastaveni hry
    protected NewGameSettings inFrNewGameSett;
    protected NewP2PGameSettings inFrNewP2PGameSett;
    protected Settings settings;

    //menu
    protected JMenuItem menuItemSave;

    //vnitrni ramec
    protected Victory victory;

    //loadGame
    protected LoadGame inFrLoadGame;

    //hrac
    protected Turn turn;

    //texty s informacemi
    protected JLabel lblInfo;
    protected JLabel lblPlayercolor;

    //napoveda
    protected JLabel LNapoveda;
    protected JButton BNapoveda;

    //pole labelu na obrazky  figurek
    protected JLabel[][] pole_figurek;
    protected JLabel lblAura;    //oznaceni mysi
    protected JLabel lblRuna;    //figurky s mustTakeFlag = true

    //deska
    private int x_sachovnice = 40;
    private int x_offset = 5;
    private int y_sachovnice = 80;
    private int y_offset = 50;

    //notace
    protected JTextField TFPrikaz;
    protected TextArea TAVypisNotace;
    protected Button BSubmitCommand;

    //obrazky
    protected ImageIcon imIcAura;
    protected ImageIcon imIcSachovnice;

    protected ImageIcon imIcBilyPesak;
    protected ImageIcon imIcBilaDama;

    protected ImageIcon imIcCernyPesak;
    protected ImageIcon imIcCernaDama;
    protected ImageIcon imIcRuna;

    protected ImageIcon imIcWhiteBox;
    protected ImageIcon imIcBlackBox;

    public InternalFrameDemo() {
        super("InternalFrameDemo");

        //Nastaveni okna
        int inset = 50;
        setBounds(inset, inset, 800, 600);

        //GUI
        desktop = new JDesktopPane();
        desktop.setBackground(Color.LIGHT_GRAY);
        setContentPane(desktop);

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        addMouseListener(this);

        //obrazky
        this.imIcAura = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/aura.png"));
        this.imIcSachovnice = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/sachovnice_420px_yellow.png"));
        this.imIcBilyPesak = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bily_pesak.png"));
        this.imIcBilaDama = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bila_dama.png"));
        this.imIcCernyPesak = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerny_pesak.png"));
        this.imIcCernaDama = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerna_dama.png"));
        this.imIcRuna = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/runa.png"));
        this.imIcWhiteBox = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/whitebox.png"));
        this.imIcBlackBox = new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/blackbox.png"));

        //menu
        setJMenuBar(createMenuBar());

        //createVictory();

        //texty s informacemi
        lblInfo = new JLabel("Not playing.");
        desktop.setLayer(lblInfo, 1);
        lblInfo.setBounds(105, 10, 295, 30);
        desktop.add(lblInfo);


        this.lblPlayercolor = new JLabel("");
        this.lblPlayercolor.setBounds(25, 10, 45, 30);
        desktop.add(this.lblPlayercolor);

        //napoveda
        this.LNapoveda = new JLabel("No help requested...");
        this.LNapoveda.setBounds(541, 15, 194, 20);
        desktop.add(LNapoveda);

        this.BNapoveda = new JButton("Help!");
        this.BNapoveda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                if(Dama.isGameSetAndRunning()){

                    Desk deskForHelp = new Desk(Dama.getDesk());
                    char playerColor = Dama.getPlayerColor();
                    int aiStochasticity = 10;

                    Turn helpTurn = AI.AIMove(deskForHelp, playerColor, aiStochasticity);

                    String helpText;
                    if(helpTurn != null){
                        helpText = helpTurn.origin.getStrLowerCase() + " -> " + helpTurn.destination.getStrLowerCase();
                    }else{
                        helpText = "Stalemate!";
                    }

                    LNapoveda.setText(helpText);
                }
            }
        });
        this.BNapoveda.setBounds(461, 15, 70, 20);
        this.BNapoveda.setEnabled(false);
        desktop.add(this.BNapoveda);

        //deska
        JLabel BGSachovnice = new JLabel("");
        BGSachovnice.setFocusable(false);
        BGSachovnice.setIcon(this.imIcSachovnice);

        BGSachovnice.setBounds(x_sachovnice, y_sachovnice, 420, 420);
        BGSachovnice.addMouseListener(this);
        desktop.add(BGSachovnice);
        desktop.setLayer(BGSachovnice, -1);

        String columnsLegend = new String("    A       B      C      D      E      F      G      H");
        String rowsLegend = new String("8\r\n\r\n7\r\n\r\n6\r\n\r\n5\r\n\r\n4\r\n\r\n3\r\n\r\n2\r\n\r\n1");

        Label LXosaPopisNahore = new Label(columnsLegend);
        LXosaPopisNahore.setFont(new Font("Tahoma", Font.PLAIN, 20));
        LXosaPopisNahore.setBounds(40, 55, 420, 20);
        desktop.add(LXosaPopisNahore);

        Label LXosaPopisDole = new Label(columnsLegend);
        LXosaPopisDole.setFont(new Font("Tahoma", Font.PLAIN, 20));
        LXosaPopisDole.setBounds(40, 505, 420, 20);
        desktop.add(LXosaPopisDole);

        JTextPane TPYosaLeva = new JTextPane();
        TPYosaLeva.setEditable(false);
        TPYosaLeva.setBackground(Color.LIGHT_GRAY);
        TPYosaLeva.setFont(TPYosaLeva.getFont().deriveFont(20f));
        TPYosaLeva.setText(rowsLegend);
        TPYosaLeva.setBounds(17, 95, 17, 402);
        desktop.add(TPYosaLeva);

        JTextPane TPYosaPrava = new JTextPane();
        TPYosaPrava.setEditable(false);
        TPYosaPrava.setText(rowsLegend);
        TPYosaPrava.setFont(TPYosaPrava.getFont().deriveFont(20f));
        TPYosaPrava.setBackground(Color.LIGHT_GRAY);
        TPYosaPrava.setBounds(465, 95, 17, 402);
        desktop.add(TPYosaPrava);

        //aura
        this.lblAura = new JLabel("");
        desktop.add(this.lblAura);

        this.lblRuna = new JLabel("");
        desktop.setLayer(this.lblRuna, 3);
        desktop.add(this.lblRuna);

        //notace
        this.TAVypisNotace = new TextArea();
        TAVypisNotace.setEditable(false);
        this.TAVypisNotace.setBounds(535, 80, 200, 350);
        desktop.add(this.TAVypisNotace);

        this.BSubmitCommand = new Button("OK");
        BSubmitCommand.setEnabled(false);
        BSubmitCommand.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String string = TFPrikaz.getText();
                TFPrikaz.setText("");
                if (!Check.isNotationValid(string))//TODO mel by vzit i kdyz se to tam napise v upper case
                {
                    lblInfo.setText("Invalid move");
                    return;
                }

                Position pos1=Position.positionStrToPositionLowerCase(string.substring(0, 2));
                Position pos2=Position.positionStrToPositionLowerCase(string.substring(3, 5));
                char barva=Dama.getPlayerColor();
                //neni rozehrana hra, takze se nic delat nebude
                if (!( (barva=='B') || (barva=='W') )) return;
                if ( (pos1==null) || (pos2==null) )
                {
                    lblInfo.setText("Invalid move");
                    return;
                }

                Turn turn=new Turn(barva, pos1, pos2);
                Dama.notaceInput(turn);
                return;
            }
        });
        this.BSubmitCommand.setBounds(670, 440, 65, 20);
        desktop.add(BSubmitCommand);

        this.TFPrikaz = new JTextField();
        this.TFPrikaz.setBounds(535, 440, 130, 20);
        desktop.add(this.TFPrikaz);
        this.TFPrikaz.setColumns(10);

        pole_figurek=new JLabel[8][8];
        for (int row=0; row<8; row++)
            for (int col=0; col<8; col++)
            {
                //String pom="R" + row + ":C" + col;

                this.pole_figurek[row][col]=new JLabel("");
                this.pole_figurek[row][col].setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                desktop.add(this.pole_figurek[row][col]);
                /*
                //bile figurky na sude radky a sude sloupce (sude vzhledem k indexu do pole)
                if ( ((row==0) || (row==2)) && (col%2==0) )
                    this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bily_pesak.png")));

                //bile figurky na lichy radek a liche sloupce (liche vzhledem k indexu do pole)
                else if ( (row==1) && (col%2==1) )
                    this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bily_pesak.png")));

                //cerne figurky na lichych radcich a lichych sloupcich (liche vzhledem k indexu do pole)
                else if ( ((row==5) || (row==7)) && (col%2==1) )
                    this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerny_pesak.png")));

                //cerne figurky na sudem radku a sudych sloupcich (sude vzhledem k indexu do pole)
                else if ( (row==6) && (col%2==0) )
                    this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerny_pesak.png")));

                //vsecha ostatni pole jsou prazdna (null)
                else this.pole_figurek[row][col]=null;
                */
            }

    }

    /**
     * Vytvori menu
     * @return JMenuBar Menu obsahujici polozky New game, New P2P game, Load game, Save game, Player, About a Quit
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Menu
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //nova lokalni hra
        JMenuItem menuItem = new JMenuItem("New game");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //nova sitova hra
        menuItem = new JMenuItem("New P2P game");
        menuItem.setMnemonic(KeyEvent.VK_W);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("newP2P");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //nacteni ulozene hry
        menuItem = new JMenuItem("Load game");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("loadgame");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //ulozeni probihajici hry
        menuItemSave = new JMenuItem("Save game");
        menuItemSave.setMnemonic(KeyEvent.VK_S);
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItemSave.setActionCommand("save");
        menuItemSave.addActionListener(this);
        menuItemSave.setEnabled(false);
        menu.add(menuItemSave);

        //prehravani partii
        menuItem = new JMenuItem("Player");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("player");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //about
        menuItem = new JMenuItem("About");
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("about");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //quit
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }


    //Reakce na menu a na tlacitka ve vnitrnich ramcich
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) {                    //menu
            createNewGameSettingsFrame();
        }else if ("newP2P".equals(e.getActionCommand())){
            createNewP2PGameSettingsFrame();
        }else if ("loadgame".equals(e.getActionCommand()))
        {
            System.out.println("LoadGame");
            createLoadGameFrame("Load game");
            System.out.println("/LoadGame");
            //this.inFrLoadGame=new LoadGame();

        }else if ("save".equals(e.getActionCommand()))
        {
            System.out.println("Save game");
            createLoadGameFrame("Save game");

            System.out.println("/Save game");

        }else if ("Player".equals(e.getActionCommand())){
            System.out.println("Neimplementovano");
        }else if ("About".equals(e.getActionCommand())){
            System.out.println("Neimplementovano");
        }else if ("quit".equals(e.getActionCommand())){
            quit();
        }else if ("startgame".equals(e.getActionCommand())){        //tlacitka ve vnitrnich ramcich
            startgame();
        }else if ("startP2Pgame".equals(e.getActionCommand())){
            startP2Pgame();
        }
        else if ("loadgamebutton".equals(e.getActionCommand()))
        {
            LoadGameButton();
        }
        else if ("savegamebuttonXml".equals(e.getActionCommand()))
        {
            SaveGameButtonXml();
        }else if("victoryOK".equals(e.getActionCommand())){
            Dama.wakeUpGameThread();
        }
        else if ("savegamebuttonNot".equals(e.getActionCommand()))
        {
            SaveGameButtonNot();
        }
        else{
            System.out.println("Neznama akce: " + e.getActionCommand());
        }
    }


    /**
     * Vytvori vnitrni ramec nastaveni lokalni hry
     */
    protected void createNewGameSettingsFrame() {

        this.inFrNewGameSett = new NewGameSettings();
        desktop.setLayer(inFrNewGameSett, 1);
        inFrNewGameSett.setLocation(512, 91);
        inFrNewGameSett.setVisible(true);

        JButton btnStartgame = new JButton("StartGame");
        inFrNewGameSett.getContentPane().add(btnStartgame);
        btnStartgame.setActionCommand("startgame");
        btnStartgame.addActionListener(this);

        desktop.add(inFrNewGameSett);
        try {
            inFrNewGameSett.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }


    protected void createLoadGameFrame(String nazevOkna)
    {
        System.out.println("\tcreate LG");

        this.inFrLoadGame=new LoadGame(nazevOkna);
        desktop.setLayer(inFrLoadGame, 5);
        inFrLoadGame.setLocation(100, 100);
        inFrLoadGame.setVisible(true);

        JButton btnLoadgame2;
        JButton btnLoadgame = new JButton("OK");
        btnLoadgame.setBounds(294, 39, 140, 20);

        inFrLoadGame.getContentPane().add(btnLoadgame);
        if (nazevOkna=="Load game")
        {
            btnLoadgame.setText("Load");
            btnLoadgame.setActionCommand("loadgamebutton");
        }
        else
        {
            btnLoadgame.setText("Save in XML");
            btnLoadgame.setActionCommand("savegamebuttonXml");

            System.out.println("Tlacitko");
            btnLoadgame2 = new JButton("Save in notation");
            btnLoadgame2.setBounds(294, 60, 140, 20);
            btnLoadgame2.setActionCommand("savegamebuttonNot");
            btnLoadgame2.addActionListener(this);
            inFrLoadGame.getContentPane().add(btnLoadgame2);
            System.out.println("/Tlacitko");

        }
        btnLoadgame.addActionListener(this);

        desktop.add(inFrLoadGame);
        try
        {
            inFrLoadGame.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e) {}

        //System.out.println("\t/create LG");
    }

    public void SaveGameButtonXml()
    {
        System.out.println("Tlacitko save!");
        String cesta=inFrLoadGame.GetPath();
        if (!cesta.endsWith(".xml")) cesta=cesta+".xml";
        System.out.println("Cesta: \"" + cesta + "\"");
        inFrLoadGame.dispose();
        try
        {
            String xml = Output.getXmlFromSettingsAndTurnVector(Dama.getSettings(), Dama.getOutputTurns());
            Output.printStringToFile(xml, cesta);
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            System.out.println("Nepodarilo se ulozit hru");
            e1.printStackTrace();
        }
    }
    public void SaveGameButtonNot()
    {
        String notace=TAVypisNotace.getText();
        String cesta=inFrLoadGame.GetPath();
        if (!cesta.endsWith(".txt")) cesta=cesta+".txt";
        System.out.println("Cesta: \"" + cesta + "\"");
        inFrLoadGame.dispose();
        try
        {
            Output.printStringToFile(notace, cesta);
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            System.out.println("Nepodarilo se ulozit hru");
            e1.printStackTrace();
        }
    }
    public void LoadGameButton()
    {
        System.out.println("Tlacitko load!");
        String cesta=inFrLoadGame.GetPath();
        System.out.println("Cesta: \"" + cesta + "\"");
        inFrLoadGame.dispose();
        if (cesta.endsWith(".xml"))
        {
            System.out.println("XML MOTAFAKA!!");
            if (!Dama.XmlToVector(cesta)) return;
        }
        else if (cesta.endsWith(".txt"))
        {
            System.out.println("NOTACE BIATCH!!");
            if (!Dama.NotToVector(cesta)) return;
        }
        else
        {
            System.out.println("Da fuk is dis sith?! (extention not txt of xml)");
            return;
        }
        Dama.wakeUpGameThread();
        this.menuItemSave.setEnabled(true);
    }



    /**
     * Vytvori vnitrni ramec nastaveni P2P hry
     */
    protected void createNewP2PGameSettingsFrame() {

        this.inFrNewP2PGameSett = new NewP2PGameSettings();
        desktop.setLayer(inFrNewP2PGameSett, 1);
        inFrNewP2PGameSett.setLocation(33, 11);
        inFrNewP2PGameSett.setVisible(true);

        JButton btnStartP2Pgame = new JButton("StartP2PGame");
        inFrNewP2PGameSett.getContentPane().add(btnStartP2Pgame);
        btnStartP2Pgame.setActionCommand("startP2Pgame");
        btnStartP2Pgame.addActionListener(this);

        desktop.add(inFrNewP2PGameSett);
        try {
            inFrNewP2PGameSett.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

    public void createVictory(){

        this.victory = new Victory();
        desktop.setLayer(this.victory, 1);
        this.victory.setLocation(200, 200);
        this.victory.setVisible(true);

        JButton btnOK = new JButton("OK");
        this.victory.getContentPane().add(btnOK);
        btnOK.setActionCommand("victoryOK");
        btnOK.addActionListener(this);

        JButton btnSave = new JButton("Save game");
        this.victory.getContentPane().add(btnSave);
        btnSave.setActionCommand("save");
        btnSave.addActionListener(this);

        desktop.add(this.victory);
        try {
            this.victory.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}

    }


    /**
     * Ziska settings z vnitrniho ramce inFrNewGameSett
     * vlozi nastaveni do vlakna hry a probudi ho
     */
    protected void startgame(){

        this.settings = this.inFrNewGameSett.getSettings();

        if(this.settings != null){
            System.out.println("GUI:Starting the game...");
            this.lblInfo.setText(this.settings.getSettingsString());

            if(Dama.isGameSetAndRunning()){
                System.out.println("hra BEZELA spoustim...");
                Dama.stopGameThread();

                try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

                Dama.startingGameThread();

                try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

                Dama.setSettings(this.settings);

                try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

                Dama.wakeUpGameThread();

                this.dispose();
            }else{
                System.out.println("gui: ukladanim nastaveni do NOVE...");
                Dama.setSettings(this.settings);

                System.out.println("gui: povoluji akce...");
                this.BSubmitCommand.setEnabled(true);
                this.BNapoveda.setEnabled(true);
                this.menuItemSave.setEnabled(true);
                Dama.wakeUpGameThread();

                this.inFrNewGameSett.setVisible(false);
                this.inFrNewGameSett.dispose();
            }
        }else{
            System.out.println("Configure settings!");
            this.lblInfo.setText("Configure settings!");
        }
    }


    /**
     * prozatim neupdatovana funkce pro nastaveni a spousteni P2P hry
     */
    @Deprecated
    protected void startP2Pgame(){

        this.settings = this.inFrNewP2PGameSett.getSettings();

        if(this.settings != null){
            System.out.println("GUI:Starting the game...");
            this.lblInfo.setText(this.settings.getSettingsString());

            //Dama.startingGameThread(this.settings);

            this.inFrNewP2PGameSett.setVisible(false);
            this.inFrNewP2PGameSett.dispose();

        }else{
            System.out.println("Configure settings!");
            this.lblInfo.setText("Configure settings!");
        }
    }


    /**
     * Ukonceni hry tlacitkem v menu
     */
    protected void quit() {

        this.setVisible(false);

        Dama.wakeUpGameThread();//probudi aby vedelo ze je zastaveno
        //Dama.stopGameThread();
        //System.exit(0);
        pullThePlug();
        this.dispose();
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        InternalFrameDemo frame = new InternalFrameDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    //Zpracuje klik tlacitka a posle zvolenou pozici na desce dale
    public void mouseClicked(MouseEvent e)
    {
        switch(e.getModifiers()) {
            case InputEvent.BUTTON1_MASK: {
                System.out.println("That's the LEFT button");
                Point p = getLocationOnScreen();
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();

                int x = (int) b.getX() - p.x - x_offset;
                int y = (int) b.getY() - p.y - y_offset;
                if ( (x>=(x_sachovnice+10)) && (y>=y_sachovnice+11) && (x<=(x_sachovnice+409)) && (y<=(y_sachovnice+410)) )
                {
                    int sx = (x - 10 - x_sachovnice);
                    int sy = y - 11 - y_sachovnice;
                     sx = sx/50;
                     sy = 7 - sy/50;

                     System.out.println(sx + ":" + sy);

                Position pos = new Position(sy, sx);
                Dama.mouseInput(pos);

                this.lblAura.setIcon(this.imIcAura);
                this.lblAura.setBounds(x_sachovnice+10+(sx*50), y_sachovnice+10+350-(sy*50),50,50);

                }
            break;
            }
            case InputEvent.BUTTON2_MASK: {
                System.out.println("That's the MIDDLE button");
                break;
            }
            case InputEvent.BUTTON3_MASK: {
                System.out.println("That's the RIGHT button");
                Dama.setClickCount(0);
                this.clearAura();
                break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mouseReleased(MouseEvent arg0) {
        // TODO Auto-generated method stub
    }
    @Override
    public void mousePressed(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
    }

    /**
     * Vykresli stav desky
     * @param desk
     */
    public void updateDesk(Desk desk)
    {
        this.lblInfo.setText("Refreshing playboard, please wait...");
        if (!Check.isMustTake(desk, Dama.getPlayerColor())) this.lblRuna.setIcon(null);

        Figure[][] this_board=desk.getState();
        for (int row=0; row<8; row++)
        {
            for (int col=0; col<8; col++)
            {
                if (this_board[row][col]==null)
                {
                    this.pole_figurek[row][col].setIcon(null);
                    continue;
                }
                if (this_board[row][col].color=='B')
                {
                    if (this_board[row][col].type=='P')
                        this.pole_figurek[row][col].setIcon(this.imIcCernyPesak);
                    else
                        this.pole_figurek[row][col].setIcon(this.imIcCernaDama);
                }
                else if (this_board[row][col].color=='W')
                {
                    if (this_board[row][col].type=='P')
                        this.pole_figurek[row][col].setIcon(this.imIcBilyPesak);
                    else
                        this.pole_figurek[row][col].setIcon(this.imIcBilaDama);
                }

                if(this_board[row][col].mustTakeFlag == true)
                {
                    this.lblRuna.setIcon(this.imIcRuna);
                    this.lblRuna.setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                    desktop.add(this.lblRuna);
                }
            }
        }

        desk.clearMustTakeFlag();
        String barva;
        char playerColor = Dama.getPlayerColor();
        if (playerColor=='W') barva="WHITE's";
        else barva="BLACK's";
        this.lblInfo.setText("Playboard is ready for " + barva + " turn.");
        ImageIcon colorbox = (playerColor=='W') ? this.imIcWhiteBox : this.imIcBlackBox;
        this.lblPlayercolor.setIcon(colorbox);

    }

    /**
     * Fce prida to textarea dalsi tah v notaci
     * @param wturn    tah bileho
     * @param wtake    indikator jestli bily bral
     * @param bturn    tah cerneho
     * @param btake    indikator jestli cerny bral
     * @param tah      poradove cislo tahu
     */
    public void NotationNewLine(Turn wturn, boolean wtake, Turn bturn, boolean btake, int tah)
    {
        String predchozi_obsah=TAVypisNotace.getText();

        String p0=wturn.origin.getStrLowerCase();
        String p1=wturn.destination.getStrLowerCase();
        String w;
        if (wtake) w="x";
        else w="-";

        String p2=bturn.origin.getStrLowerCase();
        String p3=bturn.destination.getStrLowerCase();
        String b;
        if (btake) b="x";
        else b="-";

        TAVypisNotace.setText(predchozi_obsah + tah + ". " + p0 + w + p1 + " " + p2 + b + p3 + "\n");
    }

    /**
     * Fce prida to textarea tah bileho v notaci
     * pouziti: bily vyhraje a cerny uz netahne
     * @param wturn    tah bileho
     * @param wtake    indikator jestli bily bral
     * @param tah      poradove cislo tahu
     */
    public void NotationNewLineLastWhite(Turn wturn, boolean wtake, int tah)
    {
        String predchozi_obsah=TAVypisNotace.getText();

        String p0=wturn.origin.getStrLowerCase();
        String p1=wturn.destination.getStrLowerCase();
        String w;
        if (wtake) w="x";
        else w="-";

        TAVypisNotace.setText(predchozi_obsah + tah + ". " + p0 + w + p1);
    }

    /**
     * Fce prida to textarea tah cerneho v notaci
     * pouziti: cerny tahnul
     * @param bturn    tah cerneho
     * @param btake    indikator jestli cerny bral
     */
    public void NotationNewLineLastBlack(Turn bturn, boolean btake)
    {
        String predchozi_obsah=TAVypisNotace.getText();

        String p0=bturn.origin.getStrLowerCase();
        String p1=bturn.destination.getStrLowerCase();
        String w;
        if (btake) w="x";
        else w="-";

        TAVypisNotace.setText(predchozi_obsah + " " + p0 + w + p1 + "\n");
    }

    public void pullThePlug() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    public void setInfoText(String text){
        this.lblInfo.setText(text);
    }

    public void clearAura(){
        this.lblAura.setIcon(null);
    }

    public void clearHelp(){
        this.LNapoveda.setIcon(null);
    }




    /**
     * Pomocna funkce na zisk zvolenych radio buttonu
     * @param group skupina radio buttonu
     * @return zvoleny radio button
     */
    public static JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
          JRadioButton b = (JRadioButton) e.nextElement();
          if (b.getModel() == group.getSelection()) {
            return b;
          }
        }
        return null;
    }
}
