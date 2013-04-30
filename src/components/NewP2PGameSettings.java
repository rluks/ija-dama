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

import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dama.state.Settings;

/* Used by InternalFrameDemo.java. */
public class NewP2PGameSettings extends JInternalFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1403878987649253394L;
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    ButtonGroup local;
    private JTextField txtRemoteip;
    private JTextField txtRemotePort;

    public NewP2PGameSettings() {
        super("New P2P Game Settings #" + (++openFrameCount),
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable

        //...Create the GUI and put it in the window...

        //...Then set the window size or call pack...
        setSize(424,429);

        //Set the window's location.
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

    public JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
          JRadioButton b = (JRadioButton) e.nextElement();
          if (b.getModel() == group.getSelection()) {
            return b;
          }
        }
        return null;
    }

    public Settings getSettings(){

        JRadioButton local = getSelection(this.local);

        if(local == null || this.txtRemoteip.getText().trim().equals("") || this.txtRemotePort.getText().trim().equals("")){return null;}

        String remoteIP = this.txtRemoteip.getText();
        String remotePortStr = this.txtRemotePort.getText();

        String localStr = local.getText();

        String whiteStr = "Human";
        String blackStr = "Human";

        try {Double.parseDouble(remotePortStr);
        }catch (NumberFormatException e) {
            return null;
        }

        int remotePort = (int)((Double.parseDouble(remotePortStr)));

        Settings sett = new Settings(whiteStr, blackStr, localStr, remoteIP, remotePort);
        return sett;
    }
}
