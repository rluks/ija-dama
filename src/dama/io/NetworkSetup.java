package dama.io;

import gui.Gui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import dama.Dama;

public class NetworkSetup
{
    public static void startP2Pgame(Gui gui)
    {

        gui.settings = gui.inFrNewP2PGameSett.getSettings();

        if(gui.settings != null)
        {
            System.out.println("GUI:Starting the game...");
            gui.lblInfo.setText(gui.settings.getSettingsString());
            //Dama.startingGameThread(gui.settings);
            gui.inFrNewP2PGameSett.padding.setForeground(Color.GREEN);
            gui.inFrNewP2PGameSett.padding.setText("Waititing for connection!");
            gui.inFrNewP2PGameSett.setVisible(false);
            gui.inFrNewP2PGameSett.dispose();
        }
        else
        {
            System.out.println("Configure settings!");
            gui.lblInfo.setText("Configure settings!");
            return;
        }

        if (!((gui.settings.remotePort>0) && gui.settings.remotePort<=65535))
        {
            System.out.println("Port out of range (1 - 65535)");
            gui.lblInfo.setText("Port out of range (1 - 65535)");
            return;
        }

        /// -------------------------------------------------------------------------------------------------------------
        /// S E R V E R
        if ((gui.settings.local==null) && (gui.settings.remoteIP==null))
        {

            gui.lblInfo.setText("Waiting for connection...");
            try
            {
                Vector<Turn> lt=new Vector<Turn>();
                Dama.setLoadedTurns(lt);

                System.out.println("Server");
                gui.lblInfo.setText("Waiting for connection...");


                gui.server = new ServerSocket(gui.settings.remotePort);
                gui.server.setSoTimeout(60000);
                gui.connectionToTheClient = gui.server.accept();

                gui.lblInfo.setText("Server - Connection established!");
                System.out.println("Server po accept - connection established!");

                //server ceka, ze klient mu posle svoji barvu
                InputStream in = gui.connectionToTheClient.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line = br.readLine();
                if (line.equals("White")) gui.settings.local="Black";
                else gui.settings.local="White";
                System.out.println("Server - Barva klienta: \"" + line + "\"; server: \"" + gui.settings.local + "\"");

                //soubor
                line = br.readLine();
                if (line.equals("no file"))
                {
                    System.out.println("Server - nebude se nic nacitat");
                }
                else
                {
                    char typ;
                    if (line.endsWith(".txt")) typ='t';
                    else typ='x';
                    System.out.println("Server - soubor: \"" + line + "\"" + "; typ= \'" + typ + "\'");


                }






            }
            catch ( java.io.InterruptedIOException e )
            {
                gui.lblInfo.setText("Connection timeout (60sec)!");
                System.out.println("Server - Timed Out (60 sec)!");
            }
            catch (Exception e)
            {
                System.out.println("Server - Sitova chyba");
            }

        }
        /// -------------------------------------------------------------------------------------------------------------
        /// C L I E N T
        else
        {
            try
            {
                System.out.println("Klient");

                gui.lblInfo.setText("Klient - Connecting...");

                gui.connectionToTheServer = new Socket(gui.settings.remoteIP, gui.settings.remotePort);

                gui.lblInfo.setText("Klient - Connection established!");
                System.out.println("Klient se pripojil - connection established!");

                //klient posle svoji barvu
                OutputStream out = gui.connectionToTheServer.getOutputStream();
                PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
                ps.println(gui.settings.local);


                //pokud se ma navist pozice ze souboru, posle se obsah souboru
                String cesta=gui.settings.ClientCestaKSouboru;
                if ((cesta==null)||(cesta.equals("")))
                {
                    System.out.println("Klient - Nebyl zadan soubor - NP");
                    ps.println("no file");
                }
                else
                {
                    System.out.println("Klient - Nacist soubor: \"" + cesta + "\"");
                    ps.println(cesta);



                }


            }
            catch (Exception e)
            {
                System.out.println("Klient - Sitova chyba");
            }
        }

    }

}
