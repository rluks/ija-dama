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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

import dama.Dama;
import dama.io.Turn;
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

    //hrac
    protected Turn turn;

    //texty s informacemi
    protected JLabel lblInfo;

    //pole labelu na obrazky  figurek
    protected JLabel[][] pole_figurek;

    //deska
    private int x_sachovnice = 40;
    private int x_offset = 5;
    private int y_sachovnice = 80;
    private int y_offset = 50;

    //notace
    protected JTextField TFPrikaz;
    protected TextArea TAVypisNotace;
    protected Button BSubmitCommand;

    //testovani
    protected JButton btnTlacitkoLambda;

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

        //menu
        setJMenuBar(createMenuBar());

        //texty s informacemi
        lblInfo = new JLabel("Not playing.");
        desktop.setLayer(lblInfo, 1);
        lblInfo.setBounds(470, 10, 295, 30);
        desktop.add(lblInfo);

        /*
        btnTlacitkoLambda = new JButton("New button");
        btnTlacitkoLambda.setOpaque(false);
        btnTlacitkoLambda.setBounds(590, 10, 100, 60);
        btnTlacitkoLambda.setContentAreaFilled(false);
        btnTlacitkoLambda.setBorderPainted(false);
        btnTlacitkoLambda.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/spray.png")));
        btnTlacitkoLambda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("SUP");
                btnTlacitkoLambda.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/Bird.gif")));
            }
        });
        desktop.add(btnTlacitkoLambda);
        desktop.setLayer(btnTlacitkoLambda, 1);
        */

        //napoveda
        JLabel LNapoveda = new JLabel("No help requested...");
        LNapoveda.setBounds(40, 15, 336, 20);
        desktop.add(LNapoveda);

        JButton BNapoveda = new JButton("Help!");
        BNapoveda.setBounds(390, 15, 70, 20);
        desktop.add(BNapoveda);

        //deska
        JLabel BGSachovnice = new JLabel("");
        BGSachovnice.setFocusable(false);
        BGSachovnice.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/sachovnice_420px_yellow.png")));
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

        //notace
        this.TAVypisNotace = new TextArea();
        this.TAVypisNotace.setBounds(535, 80, 200, 350);
        desktop.add(this.TAVypisNotace);

        this.BSubmitCommand = new Button("OK");
        this.BSubmitCommand.setBounds(670, 435, 65, 20);
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
                //this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/aura.png")));
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
        menuItem.setActionCommand("load");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //ulozeni probihajici hry
        menuItem = new JMenuItem("Save game");
        menuItem.setMnemonic(KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("save");
        menuItem.addActionListener(this);
        menu.add(menuItem);

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
        }else if ("Load game".equals(e.getActionCommand())){
            System.out.println("Neimplementovano");
        }else if ("Save game".equals(e.getActionCommand())){
            System.out.println("Neimplementovano");
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
        }else{
            System.out.println("Neznama akce");
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
                Dama.wakeUpGameThread();//probudi aby vedelo ze je zastaveno

                Dama.startingGameThread();

                try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}

                Dama.setSettings(this.settings);
                Dama.wakeUpGameThread();

                this.dispose();
            }else{
                System.out.println("ukladanim nastaveni do NOVE...");
                Dama.setSettings(this.settings);
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
        this.dispose();
        Dama.stopGameThread();
        Dama.wakeUpGameThread();//probudi aby vedelo ze je zastaveno
        System.exit(0);
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
        this.lblInfo.setText("Obnovuju labely figurek na desce");

        Figure[][] this_board=desk.getState();
        for (int row=0; row<8; row++)
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
                        this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerny_pesak.png")));
                    else
                        this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/cerna_dama.png")));
                }
                else if (this_board[row][col].color=='W')
                {
                    if (this_board[row][col].type=='P')
                        this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bily_pesak.png")));
                    else
                        this.pole_figurek[row][col].setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/gui/images/figurky/bila_dama.png")));
                }
            }

    }
}
