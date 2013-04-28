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
    protected NewGameSettings newGameSett;
    protected NewP2PGameSettings newP2PGameSett;
    JLabel picture;
    JButton btnNewButton;
    protected Settings settings;
    private JTextField TFPrikaz;

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


        desktop.addMouseListener(this);
        addMouseListener(this);


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
        BGSachovnice.setIcon(new ImageIcon(InternalFrameDemo.class.getResource("/components/images/sachovnice_300px.png")));
        BGSachovnice.setBounds(40, 80, 420, 420);
        desktop.add(BGSachovnice);

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
        }else{
            //quit();
        }
    }

    //Quit the application.
    protected void quit() {
        System.exit(0);
    }

    //Create a new internal frame.
    protected void createNewGameSettingsFrame() {
        this.newGameSett = new NewGameSettings();
        desktop.setLayer(newGameSett, 1);
        newGameSett.setLocation(512, 91);
        newGameSett.setVisible(true);

        //InternalFrameListener internalFrameListener = new InternalFrameIconifyListener();

        //newGameSett.addInternalFrameListener(internalFrameListener);

        JButton btnStartgame = new JButton("StartGame");
        newGameSett.getContentPane().add(btnStartgame);

        btnStartgame.setActionCommand("startgame");
        btnStartgame.addActionListener(this);

        desktop.add(newGameSett);
        try {
            newGameSett.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

    //Create a new internal frame.
    protected void createNewP2PGameSettingsFrame() {
        this.newP2PGameSett = new NewP2PGameSettings();
        desktop.setLayer(newP2PGameSett, 1);
        newP2PGameSett.setLocation(33, 11);
        newP2PGameSett.setVisible(true);

        JButton btnStartP2Pgame = new JButton("StartP2PGame");
        newP2PGameSett.getContentPane().add(btnStartP2Pgame);

        btnStartP2Pgame.setActionCommand("startP2Pgame");
        btnStartP2Pgame.addActionListener(this);

        desktop.add(newP2PGameSett);
        try {
            newP2PGameSett.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {}
    }

    protected void startgame(){

        this.settings = this.newGameSett.getSettings();

        if(this.settings != null){
            System.out.println("Starting the game...");
            this.lblNehraju.setText(this.settings.getSettingsString());
            System.out.print(this.settings.getXML());
            this.newGameSett.setVisible(false);
            this.newGameSett.dispose();
        }else{
            System.out.println("Configure settings!");
            this.lblNehraju.setText("Configure settings!");
        }
    }

    protected void startP2Pgame(){

        this.settings = this.newP2PGameSett.getSettings();

        if(this.settings != null){
            System.out.println("Starting the game...");
            this.lblNehraju.setText(this.settings.getSettingsString());
            System.out.print(this.settings.getXML());
            this.newP2PGameSett.setVisible(false);
            this.newP2PGameSett.dispose();

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
    private static void createAndShowGUI() {
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
        System.out.println("Mouse clicked (# of clicks: "
                     + e.getClickCount() + ")");
    }


    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
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
        System.out.println("Zmacklo se tlacitko!");

    }


}
