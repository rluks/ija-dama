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

import javax.swing.JInternalFrame;

import java.awt.event.*;
import java.awt.*;
import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

/* Used by InternalFrameDemo.java. */
public class NewGameSettings extends JInternalFrame {
    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;
    JRadioButton rdbtnHumanVsHuman;
    ButtonGroup group;

    public NewGameSettings() {
        super("New Game Settings #" + (++openFrameCount),
              true, //resizable
              true, //closable
              true, //maximizable
              true);//iconifiable

        //...Create the GUI and put it in the window...

        //...Then set the window size or call pack...
        setSize(424,429);

        //Set the window's location.
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        this.rdbtnHumanVsHuman = new JRadioButton("Human vs Human");
        getContentPane().add(this.rdbtnHumanVsHuman);

        JRadioButton rdbtnHumanVsAi = new JRadioButton("Human (white) vs AI (black)");
        getContentPane().add(rdbtnHumanVsAi);

        JRadioButton rdbtnAiVsHuman = new JRadioButton("AI (white) vs Human (black)");
        getContentPane().add(rdbtnAiVsHuman);

        JRadioButton rdbtnPp = new JRadioButton("P2P");
        getContentPane().add(rdbtnPp);

        //Group the radio buttons.
        this.group = new ButtonGroup();
        group.add(rdbtnHumanVsHuman);
        group.add(rdbtnHumanVsAi);
        group.add(rdbtnAiVsHuman);
        group.add(rdbtnPp);
    }

    public JRadioButton getSelection() {
        ButtonGroup group = this.group;
        for (Enumeration e = group.getElements(); e.hasMoreElements();) {
          JRadioButton b = (JRadioButton) e.nextElement();
          if (b.getModel() == group.getSelection()) {
            return b;
          }
        }
        return null;
    }
}
