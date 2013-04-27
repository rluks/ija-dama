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

import javax.swing.JLabel;
import javax.swing.event.InternalFrameListener;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.ImageIcon;

/*
 * InternalFrameDemo.java requires:
 *   MyInternalFrame.java
 */
public class InternalFrameDemo extends JFrame
                               implements ActionListener {
    JDesktopPane desktop;
    protected JLabel lblNehraju;
    protected NewGameSettings newGameSett;
    JLabel picture;
    JButton btnNewButton;

    public InternalFrameDemo() {
        super("InternalFrameDemo");

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                 screenSize.width-screenSize.width/2,
                  screenSize.height-screenSize.height/2);


        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        createFrame(); //create first "window"
        setContentPane(desktop);
        setJMenuBar(createMenuBar());

        //Make dragging a little faster but perhaps uglier.
        desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

        lblNehraju = new JLabel("Nehraju");
        lblNehraju.setBounds(562, 77, 230, 41);

        desktop.add(lblNehraju);

        btnNewButton = new JButton("New button");
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
        btnNewButton.setBounds(633, 261, 159, 145);
        desktop.add(btnNewButton);

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
        if ("new".equals(e.getActionCommand())) { //new
            createFrame();
        }else if ("startgame".equals(e.getActionCommand())){
            startgame();
        } else { //quit
            //quit();
        }
    }

    //Quit the application.
    protected void quit() {
        System.exit(0);
    }

    //Create a new internal frame.
    protected void createFrame() {
        this.newGameSett = new NewGameSettings();
        newGameSett.setLocation(48, 77);
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



    protected void startgame(){
        System.out.println("Zacinam hru");
        System.out.println(this.newGameSett.getSelection().getText());
        this.lblNehraju.setText(this.newGameSett.getSelection().getText());
        this.newGameSett.setVisible(false);
        this.newGameSett.dispose();
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

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

/*
class InternalFrameIconifyListener extends InternalFrameAdapter {
      public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
        JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
        System.out.println("Iconified: " + source.getTitle());
      }

      public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
        JInternalFrame source = (JInternalFrame) internalFrameEvent.getSource();
        System.out.println("Deiconified: " + source.getTitle());
      }
}*/
