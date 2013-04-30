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

package components;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import java.awt.event.*;
import java.awt.*;
import java.io.*;


import javax.imageio.*;

import javax.swing.JLabel;
import javax.swing.event.InternalFrameListener;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.ImageIcon;

import dama.Dama;
import dama.state.Desk;
import dama.state.Position;
import dama.state.Settings;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * InternalFrameDemo.java requires:
 *   MyInternalFrame.java
 */
public class InternalFrameDemo extends JFrame
                               implements ActionListener, MouseListener {
    JDesktopPane desktop;
    protected JLabel lblNehraju;
    protected NewGameSettings inFrNewGameSett;
    protected NewP2PGameSettings inFrNewP2PGameSett;
    JLabel picture;
    JButton btnNewButton;
    protected Settings settings;
    private JTextField TFPrikaz;

    public JLabel zMain;

    public int x_sachovnice=40;
    public int x_offset=5;
    public int y_sachovnice=80;
    public int y_offset=50;

    public InternalFrameDemo() {
        super("InternalFrameDemo");

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                 802,
                  590);


        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        desktop.setBackground(Color.LIGHT_GRAY);
        //createFrame(); //create first "window"
        setContentPane(desktop);
        setJMenuBar(createMenuBar());


        //desktop.addMouseListener(this);
        //addMouseListener(this);

        zMain = new JLabel("Manchester United");
        desktop.setLayer(zMain, 1);
        zMain.setBounds(10, 9, 161, 80);
        desktop.add(zMain);


        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        lblNehraju = new JLabel("Not playing.");
        desktop.setLayer(lblNehraju, 1);
        lblNehraju.setBounds(469, 9, 161, 32);
        desktop.add(lblNehraju);

        btnNewButton = new JButton("New button");
        desktop.setLayer(btnNewButton, 1);
        btnNewButton.setContentAreaFilled(false);
        btnNewButton.setBorderPainted(false);

        btnNewButton.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/spray.png")));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("SUP");
                btnNewButton.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/Bird.gif")));
            }
        });
        btnNewButton.setOpaque(false);
        btnNewButton.setBounds(589, 9, 104, 62);
        desktop.add(btnNewButton);

        JLabel BGSachovnice = new JLabel("");
        desktop.setLayer(BGSachovnice, -1);
        BGSachovnice.setFocusable(false);
        BGSachovnice.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/sachovnice_420px_yellow.png")));
        BGSachovnice.setBounds(x_sachovnice, y_sachovnice, 420, 420);
        desktop.add(BGSachovnice);
        BGSachovnice.addMouseListener(this);
        addMouseListener(this);


        JButton BNapoveda = new JButton("Help!");
        BNapoveda.setBounds(386, 15, 70, 20);
        desktop.add(BNapoveda);

        TextArea TAVypisNotace = new TextArea();
        TAVypisNotace.setBounds(535, 80, 200, 350);
        desktop.add(TAVypisNotace);

        Button BSubmitCommand = new Button("OK");
        BSubmitCommand.setBounds(672, 436, 65, 20);
        desktop.add(BSubmitCommand);

        TFPrikaz = new JTextField();
        TFPrikaz.setBounds(535, 436, 131, 20);
        desktop.add(TFPrikaz);
        TFPrikaz.setColumns(10);

        JLabel LNapoveda = new JLabel("Napoveda, whatever, huleni, chalst, drogy");
        LNapoveda.setBounds(40, 15, 336, 20);
        desktop.add(LNapoveda);

        Label LXosaPopisNahore = new Label("    A       B      C      D      E      F      G      H");
        LXosaPopisNahore.setFont(new Font("Tahoma", Font.PLAIN, 20));
        LXosaPopisNahore.setBounds(40, 58, 420, 20);
        desktop.add(LXosaPopisNahore);

        Label LXosaPopisDole = new Label("    A       B      C      D      E      F      G      H");
        LXosaPopisDole.setFont(new Font("Tahoma", Font.PLAIN, 20));
        LXosaPopisDole.setBounds(40, 505, 420, 20);
        desktop.add(LXosaPopisDole);

        JTextPane TPYosaLeva = new JTextPane();
        TPYosaLeva.setEditable(false);
        TPYosaLeva.setBackground(Color.LIGHT_GRAY);
        TPYosaLeva.setFont(TPYosaLeva.getFont().deriveFont(20f));
        TPYosaLeva.setText("8\r\n\r\n7\r\n\r\n6\r\n\r\n5\r\n\r\n4\r\n\r\n3\r\n\r\n2\r\n\r\n1");
        TPYosaLeva.setBounds(17, 95, 17, 402);
        desktop.add(TPYosaLeva);

        JTextPane TPYosaPrava = new JTextPane();
        TPYosaPrava.setEditable(false);
        TPYosaPrava.setText("8\r\n\r\n7\r\n\r\n6\r\n\r\n5\r\n\r\n4\r\n\r\n3\r\n\r\n2\r\n\r\n1");
        TPYosaPrava.setFont(TPYosaPrava.getFont().deriveFont(20f));
        TPYosaPrava.setBackground(Color.LIGHT_GRAY);
        TPYosaPrava.setBounds(465, 95, 17, 402);
        desktop.add(TPYosaPrava);

        JLabel LFigurkaPokus1 = new JLabel("");
        LFigurkaPokus1.setBounds(50, 440, 50, 50);
        desktop.add(LFigurkaPokus1);
        LFigurkaPokus1.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/figurky/bily_pesak.png")));

        JLabel LFigurkaPokus2 = new JLabel("");
        LFigurkaPokus2.setBounds(100, 391, 50, 50);
        desktop.add(LFigurkaPokus2);
        LFigurkaPokus2.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/figurky/cerny_pesak.png")));
        JLabel LFigurkaPokus3 = new JLabel("");
        LFigurkaPokus3.setBounds(150, 440, 50, 50);
        desktop.add(LFigurkaPokus3);
        LFigurkaPokus3.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/figurky/bila_dama.png")));

        JLabel LFigurkaPokus4 = new JLabel("");
        LFigurkaPokus4.setBounds(200, 391, 50, 50);
        desktop.add(LFigurkaPokus4);
        LFigurkaPokus4.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/figurky/cerna_dama.png")));



    }

    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //Set up the lone menu.
        JMenu menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_D);
        menuBar.add(menu);

        //Set up the first menu item.
        JMenuItem menuItem = new JMenuItem("New game");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("new");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //sitova hra
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

        //Set up the second menu item.
        menuItem = new JMenuItem("Quit");
        menuItem.setMnemonic(KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("quit");
        menuItem.addActionListener(this);
        menu.add(menuItem);

        return menuBar;
    }

    //React to menu selections.
    public void actionPerformed(ActionEvent e) {
        if ("new".equals(e.getActionCommand())) {//menu
            createNewGameSettingsFrame();
        }else if ("newP2P".equals(e.getActionCommand())){
            createNewP2PGameSettingsFrame();
        }else if ("startgame".equals(e.getActionCommand())){//tlacitka
            startgame();
        }else if ("startP2Pgame".equals(e.getActionCommand())){
            startP2Pgame();
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
        }else{
            System.out.println("Neznama akce");
        }
    }

    //Quit the application.
    protected void quit() {
        Dama.stopGameThread();
        System.exit(0);
    }

    //Create a new internal frame.
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

    //Create a new internal frame.
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

    protected void startgame(){

        this.settings = this.inFrNewGameSett.getSettings();

        if(this.settings != null){
            System.out.println("GUI:Starting the game...");
            this.lblNehraju.setText(this.settings.getSettingsString());

            if(Dama.isGameSetAndRunning()){
                Dama.stopGameThread();
                Dama.startingGameThread();

                Dama.setSettings(this.settings);
                Dama.wakeUpGameThread();

                this.dispose();
            }else{
                Dama.setSettings(this.settings);
                Dama.wakeUpGameThread();

                this.inFrNewGameSett.setVisible(false);
                this.inFrNewGameSett.dispose();
            }
        }else{
            System.out.println("Configure settings!");
            this.lblNehraju.setText("Configure settings!");
        }
    }

    protected void startP2Pgame(){

        this.settings = this.inFrNewP2PGameSett.getSettings();

        if(this.settings != null){
            System.out.println("GUI:Starting the game...");
            this.lblNehraju.setText(this.settings.getSettingsString());

            //Dama.startingGameThread(this.settings);

            this.inFrNewP2PGameSett.setVisible(false);
            this.inFrNewP2PGameSett.dispose();

        }else{
            System.out.println("Configure settings!");
            this.lblNehraju.setText("Configure settings!");
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
        InternalFrameDemo frame = new InternalFrameDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    public void mouseClicked(MouseEvent e)
    {
        //System.out.println("Mouse clicked (# of clicks: " + e.getClickCount() + ")");

        Point p=getLocationOnScreen();

        PointerInfo a = MouseInfo.getPointerInfo();
        Point b = a.getLocation();
        int x = (int) b.getX() - p.x - x_offset;
        int y = (int) b.getY() - p.y - y_offset;
        if ( (x>=(x_sachovnice+10)) && (y>=y_sachovnice+11) && (x<=(x_sachovnice+409)) && (y<=(y_sachovnice+410)) )
        {
            //System.out.println(x + ":" + y);
            int sx = (x - 10 - x_sachovnice);
            int sy = y - 11 - y_sachovnice;
            //System.out.println(sx + ":" + sy);
            sx=sx/50;
            sy=7 - sy/50;
            System.out.println(sx + ":" + sy);
            //System.out.println("\tKliknul si na sachovnici");
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

    public void setLabelHelp(String string){
        this.zMain.setText(string);
    }

    public void updateDesk(Desk desk){
        this.zMain.setText("Obnovuju labely figurek na desce");
    }
}
