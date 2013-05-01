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

import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import dama.state.Settings;


/**
 * Trida vytvarejici vnitrni ramec s nastavenim nove hry
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class NewGameSettings extends JInternalFrame {
    /**
     *
     */
    private static final long serialVersionUID = -4326238455841218297L;

    static int openFrameCount = 0;
    static final int xOffset = 30, yOffset = 30;

    ButtonGroup white;
    ButtonGroup black;

    public NewGameSettings() {
        super("New Game Settings #" + (++openFrameCount),
              true, //resizable
              true, //closable
              false, //maximizable
              false);//iconifiable


        setSize(425,430);
        setLocation(xOffset*openFrameCount, yOffset*openFrameCount);
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

        //white
        JLabel lblWhitePlayer = new JLabel("White Player:");
        getContentPane().add(lblWhitePlayer);

        JRadioButton rdbtnWhiteHuman = new JRadioButton("Human");
        getContentPane().add(rdbtnWhiteHuman);

        JRadioButton rdbtnWhiteAI = new JRadioButton("AI");
        getContentPane().add(rdbtnWhiteAI);

        this.white = new ButtonGroup();
        this.white.add(rdbtnWhiteHuman);
        this.white.add(rdbtnWhiteAI);

        //black
        JLabel lblBlackPlayer = new JLabel("Black Player:");
        getContentPane().add(lblBlackPlayer);

        JRadioButton rdbtnBlackHuman = new JRadioButton("Human");
        getContentPane().add(rdbtnBlackHuman);

        JRadioButton rdbtnBlackAI = new JRadioButton("AI");
        getContentPane().add(rdbtnBlackAI);

        this.black = new ButtonGroup();
        this.black.add(rdbtnBlackHuman);
        this.black.add(rdbtnBlackAI);

    }

    /**
     * Pomocna funkce na zisk zvolenych radio buttonu
     * @param group skupina radio buttonu
     * @return zvoleny radio button
     */
    public JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();) {
          JRadioButton b = (JRadioButton) e.nextElement();
          if (b.getModel() == group.getSelection()) {
            return b;
          }
        }
        return null;
    }

    /**
     * Vraci nastaveni zvolene v dialogu nove hry
     * @return Instance tridy Settings
     */
    public Settings getSettings(){

        JRadioButton white = getSelection(this.white);
        JRadioButton black = getSelection(this.black);
        if(white == null || black == null){return null;}

        String whiteStr = white.getText();
        String blackStr = black.getText();

        Settings sett = new Settings(whiteStr, blackStr, null, null, 0);
        return sett;
    }
}
