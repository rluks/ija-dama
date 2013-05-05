package gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
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
import java.util.Vector;

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
                               implements ActionListener, MouseListener {

    /**
    *
    */
    private static final long serialVersionUID = -7623421489879214535L;

    JDesktopPane desktop;

    //mys
    protected boolean mouseEnabled = true;
    protected static int clickCount = 0; //po dvou klicich posilame tah hre

    //vnitrni ramec
    protected Victory victory;
    protected Fail fail;
    protected LoadGame inFrLoadGame;

    //menu
    protected JMenu menu;

    //hrac
    protected Turn turn;

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


    protected ImageIcon imIcPlay;
    protected ImageIcon imIcStop;
    protected ImageIcon imIcBack;
    protected ImageIcon imIcForward;

    protected ImageIcon imIcSeal;    //lachtan

    private Settings settings;
    private JTextField txtWaittime;


    public PlayerGui() {
        super("PlayerGui");

        //Nastaveni okna
        int inset = 50;
        setBounds(inset, inset, 800, 600);

        //GUI
        desktop = new JDesktopPane();
        desktop.setBackground(Color.LIGHT_GRAY);
        setContentPane(desktop);

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        this.setMouse(false);
        addMouseListener(this);

        //menu
        setJMenuBar(createMenuBar());

        //obrazky
        this.imIcAura = new ImageIcon(PlayerGui.class.getResource("/gui/images/aura.png"));
        this.imIcSachovnice = new ImageIcon(PlayerGui.class.getResource("/gui/images/sachovnice_420px_yellow.png"));
        this.imIcBilyPesak = new ImageIcon(PlayerGui.class.getResource("/gui/images/figurky/bily_pesak.png"));
        this.imIcBilaDama = new ImageIcon(PlayerGui.class.getResource("/gui/images/figurky/bila_dama.png"));
        this.imIcCernyPesak = new ImageIcon(PlayerGui.class.getResource("/gui/images/figurky/cerny_pesak.png"));
        this.imIcCernaDama = new ImageIcon(PlayerGui.class.getResource("/gui/images/figurky/cerna_dama.png"));
        this.imIcRuna = new ImageIcon(PlayerGui.class.getResource("/gui/images/runa.png"));
        this.imIcWhiteBox = new ImageIcon(PlayerGui.class.getResource("/gui/images/whitebox.png"));
        this.imIcBlackBox = new ImageIcon(PlayerGui.class.getResource("/gui/images/blackbox.png"));
        this.imIcSeal = new ImageIcon(PlayerGui.class.getResource("/gui/images/seal.png"));

        this.imIcPlay = new ImageIcon(Gui.class.getResource("/gui/images/play.png"));
        this.imIcStop = new ImageIcon(Gui.class.getResource("/gui/images/stop.png"));
        this.imIcBack = new ImageIcon(Gui.class.getResource("/gui/images/back.png"));
        this.imIcForward = new ImageIcon(Gui.class.getResource("/gui/images/forward.png"));

                JButton btnOkspeed = new JButton("ok");
                btnOkspeed.setBounds(670, 67, 65, 20);
                desktop.add(btnOkspeed);

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
        this.TAVypisNotace.setBounds(535, 124, 200, 350);
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
                char barva=Player.getPlayerColor();
                //neni rozehrana hra, takze se nic delat nebude
                if (!( (barva=='B') || (barva=='W') )) return;
                if ( (pos1==null) || (pos2==null) )
                {
                    lblInfo.setText("Invalid move");
                    return;
                }

                Turn turn=new Turn(barva, pos1, pos2);
                Input.inputTurn(turn);
                return;
            }
        });
        this.BSubmitCommand.setBounds(671, 480, 65, 20);
        desktop.add(BSubmitCommand);

        this.TFPrikaz = new JTextField();
        this.TFPrikaz.setBounds(535, 480, 130, 20);
        desktop.add(this.TFPrikaz);
        this.TFPrikaz.setColumns(10);

        JButton btnPlay = new JButton("");
        btnPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Player.wakeUpPlayerThread();
            }
        });
        btnPlay.setBounds(535, 35, 30, 20);
        btnPlay.setIcon(this.imIcPlay);
        desktop.add(btnPlay);

        JButton btnStop = new JButton("");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player.sleepPlayerThread();
            }
        });
        btnStop.setBounds(580, 35, 30, 20);
        btnStop.setIcon(this.imIcStop);
        desktop.add(btnStop);

        JButton btnStepback = new JButton("");
        btnStepback.setBounds(665, 36, 30, 20);
        btnStepback.setIcon(this.imIcBack);
        desktop.add(btnStepback);

        JButton btnStepforward = new JButton("");
        btnStepforward.setBounds(705, 35, 30, 20);
        btnStepforward.setIcon(this.imIcForward);
        desktop.add(btnStepforward);

        JLabel lblWaitingTime = new JLabel("Wait time:");
        lblWaitingTime.setBounds(515, 67, 80, 20);
        desktop.add(lblWaitingTime);

        txtWaittime = new JTextField();
        txtWaittime.setText("waitTime");
        txtWaittime.setBounds(580, 67, 80, 20);
        desktop.setLayer(txtWaittime, 0);
        desktop.add(txtWaittime);
        txtWaittime.setColumns(10);

        pole_figurek=new JLabel[8][8];
        pole_napovedy=new JLabel[8][8];
        for (int row=0; row<8; row++)
            for (int col=0; col<8; col++)
            {
                this.pole_figurek[row][col]=new JLabel("");
                this.pole_figurek[row][col].setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                desktop.add(this.pole_figurek[row][col]);

                this.pole_napovedy[row][col]=new JLabel("");
                this.pole_napovedy[row][col].setBounds(x_sachovnice+10+(col*50), y_sachovnice+10+350-(row*50),50,50);
                desktop.add(this.pole_napovedy[row][col]);
                desktop.setLayer(this.pole_napovedy[row][col], 3);
            }

    }

    /**
     * Vytvori menu
     * @return JMenuBar Menu obsahujici polozky New game, New P2P game, Load game, Save game, Player, About a Quit
     */
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Menu
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //nacteni ulozene hry
        JMenuItem menuItem = new JMenuItem("Load game");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("loadgame");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }


    //Reakce na menu a na tlacitka ve vnitrnich ramcich
    public void actionPerformed(ActionEvent e) {
        if("loadgame".equals(e.getActionCommand())){
            createLoadGameFrame("Load game");
        }else if("loadgamebutton".equals(e.getActionCommand())){
            LoadGameButton();
        }else if("victoryOK".equals(e.getActionCommand())){
            this.setVisible(false);
            Player.wakeUpPlayerThread();
            Player.startingPlayerThread();
            this.dispose();
        }else if("failOK".equals(e.getActionCommand())){
            this.setVisible(false);
            Player.wakeUpPlayerThread();
            Player.startingPlayerThread();
            this.dispose();
        }
        else{
            System.out.println("PlayerGui: Neznama akce: " + e.getActionCommand());
        }
    }

    /**
     * Vytvori vnitrni ramec pro nacitani drive ulozene hry
     * @param nazevOkna
     */
    protected void createLoadGameFrame(String nazevOkna)
    {
        System.out.println("PlayerGui: \tcreate LG");

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

        //System.out.println("PlayerGui: \t/create LG");
    }

    public void createVictory(){

        this.victory = new Victory();
        desktop.setLayer(this.victory, 5);
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
     * Reakce na tlacitko v inFrLoadGame
     */
    public void LoadGameButton()
    {
        Vector<Turn> loadedTurns = null;
        if(Player.isGameSetAndRunning()){

            System.out.println("PlayerGui: Tlacitko load!");
            String cesta=inFrLoadGame.GetPath();
            System.out.println("PlayerGui: Cesta: \"" + cesta + "\"");
            inFrLoadGame.dispose();

            Player.stopThread();
            Player.interruptGameThread();

            this.dispose();


            Player.startingPlayerThread();
            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}

            if (cesta.endsWith(".xml"))
            {
                System.out.println("PlayerGui: XML MOTAFAKA!!");
                //if (!Input.XmlToVector(cesta, true, null)) return;
                this.settings = Input.xmlToSettings(cesta, null);
                loadedTurns = Input.xmlToVector(cesta, null);

                if(this.settings == null || loadedTurns == null){
                    return;
                }
            }
            else if (cesta.endsWith(".txt"))
            {
                System.out.println("PlayerGui: NOTACE BIATCH!!");
                //boolean test = Input.NotToVector(cesta, true, null);
                //if (!test) return;
                loadedTurns = Input.notToVector(cesta, null);
                this.settings = new Settings("human", "human", null, null, 0, 0, null);//pri nacteni z notace se pouzije implicitni nastaveni

                if(this.settings == null || loadedTurns == null){
                    return;
                }
            }
            else
            {
                System.out.println("PlayerGui: Da fuk is dis sith?! (extention not txt of xml)");
                return;
            }

            Player.setLoadedTurns(loadedTurns);
            Player.setSettings(this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer();
            Player.wakeUpPlayerThread();

        }else{
            System.out.println("PlayerGui: Tlacitko load!");
            String cesta=inFrLoadGame.GetPath();
            System.out.println("PlayerGui: Cesta: \"" + cesta + "\"");
            inFrLoadGame.dispose();
            if (cesta.endsWith(".xml"))
            {
                System.out.println("PlayerGui: XML MOTAFAKA!!");
                //if (!Input.XmlToVector(cesta, true, null)) return;
                this.settings = Input.xmlToSettings(cesta, null);
                loadedTurns = Input.xmlToVector(cesta, null);

                if(this.settings == null || loadedTurns == null){
                    return;
                }
            }
            else if (cesta.endsWith(".txt"))
            {
                System.out.println("PlayerGui: NOTACE BIATCH!!");
                //boolean test = Input.NotToVector(cesta, true, null);
                //System.out.println("PlayerGui: NotToVector vraci " + test);
                //if (!test) return;
                this.settings = new Settings("human", "human", null, null, 0, 0, null);//pri nacteni z notace se pouzije implicitni nastaveni
                loadedTurns = Input.notToVector(cesta, null);

                if(this.settings == null || loadedTurns == null){
                    return;
                }
            }
            else
            {
                System.out.println("PlayerGui: Da fuk is dis sith?! (extention not txt of xml)");
                return;
            }

            Player.setLoadedTurns(loadedTurns);
            Player.setSettings(this.settings);

            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
            Player.startPlayer();
            Player.wakeUpPlayerThread();
            this.BSubmitCommand.setEnabled(true);
        }
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
        PlayerGui frame = new PlayerGui();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    /**
     * Povoluje a zakazuje kliknuti mysi
     * @param value boolean
     */
    public void setMouse(boolean value){
        this.mouseEnabled = value;
    }

    //Zpracuje klik tlacitka a posle zvolenou pozici na desce dale
    public void mouseClicked(MouseEvent e)
    {
        if(this.mouseEnabled){
            switch(e.getModifiers()) {
                case InputEvent.BUTTON1_MASK: {
                    System.out.println("PlayerGui: That's the LEFT button");
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
                    this.mouseInput(pos);

                    this.lblAura.setIcon(this.imIcAura);
                    this.lblAura.setBounds(x_sachovnice+10+(sx*50), y_sachovnice+10+350-(sy*50),50,50);

                    }
                break;
                }
                case InputEvent.BUTTON2_MASK: {
                    System.out.println("PlayerGui: That's the MIDDLE button");
                    break;
                }
                case InputEvent.BUTTON3_MASK: {
                    System.out.println("PlayerGui: That's the RIGHT button");
                    PlayerGui.setClickCount(0);
                    this.clearAura();
                    break;
                }
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
     * Zpracuje vstup od mysi (kliky na sachovnici) a posila ho dal
     * @param pos
     */
    public void mouseInput(Position pos){

        if(Player.isGameSetAndRunning()){

            PlayerGui.clickCount++;
            System.out.println("PlayerGui: mouseInput:" + pos.getStr());

            if(PlayerGui.clickCount == 1){
                this.turn = new Turn(Player.getTurn());

                this.turn.origin = new Position(pos);
            }

            if(PlayerGui.clickCount == 2){
                this.turn.destination = new Position(pos);

                Input.inputTurn(this.turn);

                PlayerGui.clickCount = 0;

                this.clearAura();
            }
        }
    }

    public static void setClickCount(int count){
        PlayerGui.clickCount = count;
        System.out.println("PlayerGui: setClickCount:" + count);
    }

    /**
     * Vykresli stav desky
     * @param desk
     */
    public void updateDesk(Desk desk)
    {
        this.lblInfo.setText("Refreshing playboard, please wait...");
        if (!Check.isMustTake(desk, Player.getPlayerColor())) this.lblRuna.setIcon(null);
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
        char playerColor = Player.getPlayerColor();
        if (playerColor=='W') barva="WHITE's";
        else barva="BLACK's";
        this.lblInfo.setText("Playboard is ready for " + barva + " turn.");
        ImageIcon colorbox = (playerColor=='W') ? this.imIcWhiteBox : this.imIcBlackBox;
        this.lblPlayercolor.setIcon(colorbox);

    }

    public void ZobrazNapovedu()
    {
        Desk desk = Player.getDesk();
        char natahu=Player.getPlayerColor();
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

    public void SmazNapovedu()
    {
        for(int row=0; row<8; row++)
            for(int col=0; col<8; col++)
                this.pole_napovedy[row][col].setIcon(null);
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

    /**
     * Vycisti hraci desku od zeleneho oznaceni
     */
    public void clearAura(){
        this.lblAura.setIcon(null);
    }

    public void setMenu(boolean value){
        this.menu.setEnabled(value);
    }

    public void setSubmitButton(boolean value){
        this.BSubmitCommand.setEnabled(value);
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
