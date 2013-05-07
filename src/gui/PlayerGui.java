package gui;

import java.awt.Color;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Label;

import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import java.util.Vector;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import dama.Player;
import dama.io.Input;
import dama.io.Turn;
import dama.operations.Check;
import dama.operations.Moves;
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
public class PlayerGui extends JFrame
                               implements ActionListener {

    /**
    *
    */
    private static final long serialVersionUID = -7462267993661194622L;

    JDesktopPane desktop;

    //vnitrni ramce
    protected Fail fail;
    protected LoadGame inFrLoadGame;
    protected Selection inFrSelection;
    protected InsertNotation inFrInsertNot;

    //menu
    protected JMenu menu;

    //tahy
    protected Turn turn;

    //nastaveni
    protected Settings settings;

    //vlakno prehravace ktere vytvorilo instanci tohoto gui
    protected Player thread;

    //texty s informacemi
    protected JLabel lblInfo;
    protected JLabel lblPlayercolor;

    //pole labelu na obrazky  figurek
    protected JLabel[][] pole_figurek;
    protected JLabel[][] pole_napovedy;
    protected JLabel lblAura;    //oznaceni mysi
    protected JLabel lblRuna;    //figurky s mustTakeFlag = true

    //deska
    private int x_sachovnice = 40;
    private int y_sachovnice = 80;

    //notace
    protected TextArea TAVypisNotace;

    //obrazky
    protected ImageIcon imIcSachovnice;

    protected ImageIcon imIcBilyPesak;
    protected ImageIcon imIcBilaDama;

    protected ImageIcon imIcCernyPesak;
    protected ImageIcon imIcCernaDama;
    protected ImageIcon imIcRuna;

    protected ImageIcon imIcWhiteBox;
    protected ImageIcon imIcBlackBox;

    protected ImageIcon imIcPlay;
    protected ImageIcon imIcStop;
    protected ImageIcon imIcBack;
    protected ImageIcon imIcForward;

    protected ImageIcon imIcSeal;    //lachtan

    //ovladani
    protected JTextField txtWaittime;
    protected JLabel lblWantturn;

    protected JButton btnPlay;
    protected JButton btnStop;
    protected JButton btnStepback;
    protected JButton btnStepforward;
    protected JButton btnOkspeed;

    public PlayerGui(final Player thread) {
        super("Player: " + thread.getName());

        this.thread = thread;

        //Nastaveni okna
        setBounds(100, 100, 800, 600);
        this.setResizable(false);

        //GUI
        desktop = new JDesktopPane();
        desktop.setBackground(Color.LIGHT_GRAY);
        setContentPane(desktop);

        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        //menu
        setJMenuBar(createMenuBar());

        //obrazky
        java.net.URL imageURL = PlayerGui.class.getResource("images/sachovnice_420px_yellow.png");
        if (imageURL != null) {this.imIcSachovnice = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/bily_pesak.png");
        if (imageURL != null) {this.imIcBilyPesak = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/bila_dama.png");
        if (imageURL != null) {this.imIcBilaDama = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/cerny_pesak.png");
        if (imageURL != null) {this.imIcCernyPesak = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/cerna_dama.png");
        if (imageURL != null) {this.imIcCernaDama = new ImageIcon(imageURL);}

        imageURL = PlayerGui.class.getResource("images/runa.png");
        if (imageURL != null) {this.imIcRuna = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/whitebox.png");
        if (imageURL != null) {this.imIcWhiteBox = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/blackbox.png");
        if (imageURL != null) {this.imIcBlackBox = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/seal.png");
        if (imageURL != null) {this.imIcSeal = new ImageIcon(imageURL);}

        imageURL = PlayerGui.class.getResource("images/play.png");
        if (imageURL != null) {this.imIcPlay = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/stop.png");
        if (imageURL != null) {this.imIcStop = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/back.png");
        if (imageURL != null) {this.imIcBack = new ImageIcon(imageURL);}
        imageURL = PlayerGui.class.getResource("images/forward.png");
        if (imageURL != null) {this.imIcForward = new ImageIcon(imageURL);}

        //texty s informacemi
        lblInfo = new JLabel("Not playing.");
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        desktop.setLayer(lblInfo, 1);
        lblInfo.setBounds(105, 10, 295, 30);
        desktop.add(lblInfo);

        this.lblPlayercolor = new JLabel("");
        this.lblPlayercolor.setBounds(25, 10, 45, 30);
        desktop.add(this.lblPlayercolor);

        //deska
        JLabel BGSachovnice = new JLabel("");
        BGSachovnice.setFocusable(false);
        BGSachovnice.setIcon(this.imIcSachovnice);

        BGSachovnice.setBounds(x_sachovnice, y_sachovnice, 420, 420);
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

        this.lblRuna = new JLabel("");
        desktop.setLayer(this.lblRuna, 3);
        desktop.add(this.lblRuna);

        //notace
        this.TAVypisNotace = new TextArea();
        TAVypisNotace.setEditable(false);
        this.TAVypisNotace.setBounds(535, 124, 200, 350);
        desktop.add(this.TAVypisNotace);

        //ovladani prehravani
        btnPlay = new JButton("");
        btnPlay.setEnabled(false);
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                Player.interruptPlayerThread(thread);
                Player.wakeUpPlayerThread(thread);
            }
        });
        btnPlay.setBounds(515, 35, 30, 20);
        btnPlay.setIcon(this.imIcPlay);
        desktop.add(btnPlay);

        btnStop = new JButton("");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Player.sleepPlayerThread(thread);
            }
        });
        btnStop.setBounds(555, 35, 30, 20);
        btnStop.setIcon(this.imIcStop);
        desktop.add(btnStop);

        btnStepback = new JButton("");
        btnStepback.setEnabled(false);
        btnStepback.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Player.setIndexChange(thread, -1);
                Player.interruptPlayerThread(thread);
                Player.wakeUpPlayerThread(thread);

            }
        });
        btnStepback.setBounds(621, 35, 30, 20);
        btnStepback.setIcon(this.imIcBack);
        desktop.add(btnStepback);

        btnStepforward = new JButton("");
        btnStepforward.setEnabled(false);
        btnStepforward.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                Player.setWaitTime(thread, 1000);
                Player.interruptPlayerThread(thread);
                Player.wakeUpPlayerThread(thread);
                try {Thread.sleep(200);} catch (InterruptedException e) {
                    System.out.println("PlayerGui: stepforward interrupt");
                }
                Player.sleepPlayerThread(thread);
            }
        });
        btnStepforward.setBounds(730, 35, 30, 20);
        btnStepforward.setIcon(this.imIcForward);
        desktop.add(btnStepforward);

        btnOkspeed = new JButton("ok");
        btnOkspeed.setEnabled(false);
        btnOkspeed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {

                if(txtWaittime.getText().trim().equals("")){return;}

                String waittimeStr = txtWaittime.getText();

                try {Integer.parseInt(waittimeStr);
                }catch (NumberFormatException e) {
                    return;
                }

                int waittime = Integer.parseInt(waittimeStr);
                Player.setWaitTime(thread, waittime);
            }
        });
        btnOkspeed.setBounds(695, 67, 65, 20);
        desktop.add(btnOkspeed);

        //rychlost prehravani
        JLabel lblWaitingTime = new JLabel("Wait time (ms)");
        lblWaitingTime.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblWaitingTime.setBounds(515, 67, 80, 20);
        desktop.add(lblWaitingTime);

        txtWaittime = new JTextField();
        txtWaittime.setText("1000");
        txtWaittime.setBounds(600, 67, 65, 20);
        desktop.setLayer(txtWaittime, 0);
        desktop.add(txtWaittime);
        txtWaittime.setColumns(10);

        lblWantturn = new JLabel("Target");
        lblWantturn.setHorizontalAlignment(SwingConstants.CENTER);
        lblWantturn.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblWantturn.setBounds(661, 35, 59, 21);
        desktop.add(lblWantturn);

        pole_figurek=new JLabel[8][8];
        pole_napovedy=new JLabel[8][8];
        for (int row=0; row<8; row++){
            for (int col=0; col<8; col++){
                this.pole_figurek[row][col]=new JLabel("");
                this.pole_figurek[row][col].setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                desktop.add(this.pole_figurek[row][col]);

                this.pole_napovedy[row][col]=new JLabel("");
                this.pole_napovedy[row][col].setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                desktop.add(this.pole_napovedy[row][col]);
                desktop.setLayer(this.pole_napovedy[row][col], 3);
            }
        }
    }

    /**
     * Vytvori menu prehravace
     * @return JMenuBar Menu
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Menu
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //nacteni ulozene hry
        JMenuItem menuItem = new JMenuItem("Selection");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("selection");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }


    //Reakce na menu a na tlacitka ve vnitrnich ramcich
    public void actionPerformed(ActionEvent e) {
        if("loadgame".equals(e.getActionCommand())){
            this.inFrSelection.dispose();
            createLoadGameFrame("Load game");
        }else if("loadgamebutton".equals(e.getActionCommand())){
            LoadGameButton();
        }else if("selection".equals(e.getActionCommand())){
            createSelectionFrame();
        }else if("insertnotation".equals(e.getActionCommand())){
            createInsertNotationFrame();
        }else if("notationOK".equals(e.getActionCommand())){
            LoadGameNotation();
        }else if("victoryOK".equals(e.getActionCommand())){
            this.setVisible(false);
            Player.wakeUpPlayerThread(this.thread);
            Player.startingPlayerThread();
            this.dispose();
        }else if("failOK".equals(e.getActionCommand())){
            this.setVisible(false);
            Player.wakeUpPlayerThread(this.thread);
            Player.startingPlayerThread();
            this.dispose();
        }
        else{
            System.out.println("PlayerGui: Neznama akce: " + e.getActionCommand());
        }
    }


    /**
     * Vytvori vnitrni ramec pro vyber zda nacitat hru nebo zadat v notaci
     */
    protected void createSelectionFrame()
    {
        this.inFrSelection = new Selection();
        desktop.setLayer(inFrSelection, 5);
        inFrSelection.setLocation(100, 100);
        inFrSelection.setVisible(true);

        JButton btnLoad = new JButton("Loadgame");
        this.inFrSelection.getContentPane().add(btnLoad);
        btnLoad.setActionCommand("loadgame");
        btnLoad.addActionListener(this);

        JButton btnNotation = new JButton("Insert notation");
        this.inFrSelection.getContentPane().add(btnNotation);
        btnNotation.setActionCommand("insertnotation");
        btnNotation.addActionListener(this);

        desktop.add(inFrSelection);
        try
        {
            inFrSelection.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e) {}

    }

    /**
     * Vytvori vnitrni ramec pro vlozeni notace (textarea a tlactiko, ktere startuje prehravani)
     */
    protected void createInsertNotationFrame(){

        this.inFrSelection.dispose();
        this.inFrInsertNot = new InsertNotation();
        desktop.setLayer(inFrInsertNot, 5);
        inFrInsertNot.setLocation(100, 100);
        inFrInsertNot.setVisible(true);

        JButton btnNotation = new JButton("Insert notation");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 2;
        this.inFrInsertNot.getContentPane().add(btnNotation,  gbc_btnNewButton);
        btnNotation.setActionCommand("notationOK");
        btnNotation.addActionListener(this);

        desktop.add(inFrInsertNot);
        try
        {
            inFrInsertNot.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e) {}
    }

    /**
     * Vytvori vnitrni ramec pro nacitani drive ulozene hry
     * @param nazevOkna
     */
    protected void createLoadGameFrame(String nazevOkna)
    {

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

            System.out.println("PlayerGui: Tlacitko");
            btnLoadgame2 = new JButton("Save in notation");
            btnLoadgame2.setBounds(294, 60, 140, 20);
            btnLoadgame2.setActionCommand("savegamebuttonNot");
            btnLoadgame2.addActionListener(this);
            inFrLoadGame.getContentPane().add(btnLoadgame2);
            System.out.println("PlayerGui: /Tlacitko");

        }
        btnLoadgame.addActionListener(this);

        desktop.add(inFrLoadGame);
        try
        {
            inFrLoadGame.setSelected(true);
        }
        catch (java.beans.PropertyVetoException e) {}

    }

    /**
     * Vytvori vnitrni ramec s hlaskou danou parametrem
     * @param string
     */
    public void createFail(String string) {

        this.fail = new Fail(string);
        desktop.setLayer(this.fail, 5);
        this.fail.setLocation(200, 100);
        this.fail.setVisible(true);

        JButton btnNewButton = new JButton("OK");
        GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
        gbc_btnNewButton.gridx = 0;
        gbc_btnNewButton.gridy = 2;
        this.fail.getContentPane().add(btnNewButton, gbc_btnNewButton);
        btnNewButton.setActionCommand("failOK");
        btnNewButton.addActionListener(this);

        desktop.add(this.fail);
        try {
            this.fail.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}

    }

    /**
     * Reakce na tlacitko v inFrInsertNot
     */
    public void LoadGameNotation(){
        String notationStr = new String(this.inFrInsertNot.txtrNotation.getText());
        if(!notationStr.endsWith("\n")){
            notationStr += "\n";
        }

        Vector<Turn> loadedTurns = Input.notToVector(null, notationStr);

        this.settings = new Settings("human", "human", null, null, 0, 0, null);//pri nacteni z notace se pouzije implicitni nastaveni

        if(this.settings == null || loadedTurns == null){
            System.out.println("settings nebo loadedturns null");
            return;
        }

        this.inFrInsertNot.dispose();

        if(Player.isGameSetAndRunning(this.thread)){

            Player.stopThread(this.thread);
            Player.interruptPlayerThread(this.thread);

            this.dispose();

            Player.startingPlayerThread();
            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

            Player.setLoadedTurns(this.thread, loadedTurns);
            Player.setSettings(this.thread, this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer(this.thread);
            Player.wakeUpPlayerThread(this.thread);

        }else{

            Player.setLoadedTurns(this.thread, loadedTurns);
            Player.setSettings(this.thread, this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer(this.thread);
            Player.wakeUpPlayerThread(this.thread);
        }
    }

    /**
     * Reakce na tlacitko v inFrLoadGame
     */
    public void LoadGameButton()
    {
        Vector<Turn> loadedTurns = null;

        System.out.println("PlayerGui: Tlacitko load!");
        String cesta=inFrLoadGame.GetPath();
        System.out.println("PlayerGui: Cesta: \"" + cesta + "\"");
        inFrLoadGame.dispose();

        if (cesta.endsWith(".xml"))
        {
            System.out.println("PlayerGui: XML!!");
            this.settings = Input.xmlToSettings(cesta, null);
            loadedTurns = Input.xmlToVector(cesta, null);
        }
        else if (cesta.endsWith(".txt"))
        {
            System.out.println("PlayerGui: NOTAC!!");
            loadedTurns = Input.notToVector(cesta, null);
            this.settings = new Settings("human", "human", null, null, 0, 0, null);//pri nacteni z notace se pouzije implicitni nastaveni
        }
        else
        {
            System.out.println("PlayerGui: Da fjuk is dis sith?! (extention not txt of xml)");
            return;
        }

        if(this.settings == null || loadedTurns == null){
            return;
        }

        if(Player.isGameSetAndRunning(this.thread)){

            Player.stopThread(this.thread);
            Player.interruptPlayerThread(this.thread);

            this.dispose();

            Player.startingPlayerThread();
            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

            Player.setLoadedTurns(this.thread, loadedTurns);
            Player.setSettings(this.thread, this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer(this.thread);
            Player.wakeUpPlayerThread(this.thread);

        }else{

            Player.setLoadedTurns(this.thread, loadedTurns);
            Player.setSettings(this.thread, this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer(this.thread);
            Player.wakeUpPlayerThread(this.thread);

        }
    }

    /*
     * Vytvori GUI a zobrazi ho
     *
    public static void createAndShowGUI() {

        JFrame.setDefaultLookAndFeelDecorated(true);

        PlayerGui frame = new PlayerGui();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
    }*/

    /**
     * Vykresli stav desky
     * @param desk
     */
    public void updateDesk(Desk desk)
    {
        this.lblInfo.setText("Refreshing playboard, please wait...");
        if (!Check.isMustTake(desk, Player.getPlayerColor(this.thread))) this.lblRuna.setIcon(null);
        SmazNapovedu();

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
        char playerColor = Player.getPlayerColor(this.thread);
        if (playerColor=='W') barva="WHITE's";
        else barva="BLACK's";
        this.lblInfo.setText("Playboard is ready for " + barva + " turn.");
        ImageIcon colorbox = (playerColor=='W') ? this.imIcWhiteBox : this.imIcBlackBox;
        this.lblPlayercolor.setIcon(colorbox);

    }

    /**
     * Zobrazi na desce hry napovedu. Pokud dany hrac nemusi brat, vsechny figurky co se muzou pohnout jsou modre oznaceny.
     * Pokud musi dany hrac brat, cervene se mu oznaci vsechny figurky co mohou brat.
     */
    public void ZobrazNapovedu()
    {
        Desk desk = Player.getDesk(this.thread);
        char natahu=Player.getPlayerColor(this.thread);
        Figure[][] this_board=desk.getState();
        boolean brani=Check.isMustTake(desk, natahu);
        desk.clearMustTakeFlag();

        for(int row=0; row<8; row++)
            for(int col=0; col<8; col++)
                if ((this_board[row][col]!=null) && (this_board[row][col].color==natahu))
                {
                    Position pos=new Position(row, col);
                    if ((brani)&&(Moves.canTake(pos, desk)))
                        this.pole_napovedy[row][col].setIcon(imIcRuna);
                    else if ((Moves.canMove(pos, desk))&&(!brani))
                        this.pole_napovedy[row][col].setIcon(imIcSeal);

                }
    }

    /**
     * Na desce smaze vizualni napovedu (modre a cervene oznaceni figurek).
     */
    public void SmazNapovedu()
    {
        for(int row=0; row<8; row++)
            for(int col=0; col<8; col++)
                this.pole_napovedy[row][col].setIcon(null);
    }


    /**
     * Vycisti pole s vypisem notaci
     */
    public void NotationClear(){
        this.TAVypisNotace.setText("");
    }

    /**
     * Fce prida to textarea tah bileho v notaci
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

    /**
     * Funkce na testovani zavreni okna
     */
    public void pullThePlug() {
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
    }

    /**
     * Nastavi informativni text na text dany parametrem
     * @param text
     */
    public void setInfoText(String text){
        this.lblInfo.setText(text);
    }

    /**
     * Povoluje/zakazuje menu
     * @param value
     */
    public void setMenu(boolean value){
        this.menu.setEnabled(value);
    }

    /**
     * Nastavuje label s poctem tahu
     * @param text
     */
    public void setWantturn(String text){
        this.lblWantturn.setText(text);
    }

    /**
     * Povoluje/zakazuje tlacitka prehravani
     * @param value
     */
    public void setPlayerButtons(boolean value){
        this.btnPlay.setEnabled(value);
        this.btnStop.setEnabled(value);
        this.btnStepback.setEnabled(value);
        this.btnStepforward.setEnabled(value);
        this.btnOkspeed.setEnabled(value);
    }

    /**
     * Povoluje/zakazuje tlacitka pro pohyb vpred
     * @param value
     */
    public void setForward(boolean value){
        this.btnPlay.setEnabled(value);
        this.btnStepforward.setEnabled(value);
    }
}
