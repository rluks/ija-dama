package dama.io;

import gui.Gui;

//import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Vector;

import dama.Dama;
import dama.state.Settings;

/**
 * Trida ktera obsahuje staticke metody pro sitovou hru.
 * @author Pavel Hála
 * @author  Roman Lukš
 */
public class NetworkSetup
{
    /**
     * Fce pro zahajeni sitove hry. Vezme nastaveni z internal frame a podle nej ceka na pripojeni nebo se pripoji k nekomu.
     * Obe strany si vymeni zakladni informace jako kdo ma jakou barvu a pripadne si predaji informace o nactene hre
     * a uvedou desku do stavu jako je v ulozene hre.
     * @param gui odkaz na graficke rozhrani ze ktereho se ziska nastaveni a na kterem se taky hra vykonava
     */
    public static void startP2Pgame(Gui gui)
    {
        killTheSockets(gui.settings);    //zrusi vsechna predchozi spojeni

        Vector<Turn> lt = new Vector<Turn>();
        Settings sett=gui.inFrNewP2PGameSett.getSettings();

        gui.settings = sett;    //ziska setting z internal framu, ktere si zkolil uzivatel
        char typ;        //typ spouboru ve kterem je ulozena hra (txt nebo xml)
        String cesta;    //sesta k souboru (dulezita je jen koncovka)
        //String obsahSouboru;

        if(gui.settings != null)    //pokud je co nastavit (hrac zadal validni settings)
        {
            System.out.println("GUI:Starting the game...");
            gui.lblInfo.setText(gui.settings.getSettingsString());
            //Dama.startingGameThread(gui.settings);
            //gui.inFrNewP2PGameSett.padding.setForeground(Color.GREEN);
            //gui.inFrNewP2PGameSett.padding.setText("Waititing for connection!");

            //gui.inFrNewP2PGameSett.setVisible(false);    //odstani se internal frame s nastavenim settingu
            //gui.inFrNewP2PGameSett.dispose();
        }
        else    //hrac zadal spatne settingy, je mu to sdeleno
        {
            System.out.println("Configure settings!");
            gui.lblInfo.setText("Configure settings!");
            return;
        }

        if (!((gui.settings.remotePort>0) && gui.settings.remotePort<=65535))    //spatne zadany port
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
                //Vector<Turn> lt=new Vector<Turn>();    //vektor s tahy, tady se nuluje
                //Dama.setLoadedTurns(lt);

                System.out.println("Server");
                gui.lblInfo.setText("Waiting for connection...");

                sett.server = new ServerSocket(gui.settings.remotePort);    //hostuje se hra, ceka se na pripojeni hrace max 60sec
                sett.server.setSoTimeout(60000);

                sett.connectionToTheClient = sett.server.accept();    //tady se nekdo konecne pripoji

                gui.lblInfo.setText("Server - Connection established!");
                System.out.println("Server po accept - connection established!");

                //server ceka, ze klient mu posle svoji barvu
                InputStream in = sett.connectionToTheClient.getInputStream();        //nastavi se input stream
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String line = br.readLine();        //prijde string s barvou klienta
                if (line.equals("White")) gui.settings.local="Black";
                else gui.settings.local="White";
                System.out.println("Server - Barva klienta: \"" + line + "\"; server: \"" + gui.settings.local + "\"");

                //soubor
                cesta = br.readLine();
                if (cesta.equals("no file"))    //nebude se nacitat ulozena hry
                {
                    System.out.println("Server - nebude se nic nacitat");
                }
                else    //bude se nacitat ulozena hra
                {
                    if (cesta.endsWith(".txt")) typ='t';    //jedna se o soubor se zakladni notaci
                    else typ='x';                            //nebo je to xml
                    System.out.println("Server - soubor: \"" + cesta + "\"" + "; typ= \'" + typ + "\'");

                    String obsahSouboru="";

                    //System.out.println("Server - pred nactenim souboru");
                    while((line=br.readLine()) != null)        //cely obsah souboru klient posl serveru, tady si to server nacte do stringu
                    {
                        obsahSouboru+=line;
                        if (obsahSouboru.endsWith("KONEC"))
                        {
                            obsahSouboru=obsahSouboru.replace("KONEC", "");
                            break;
                        }
                        obsahSouboru+="\n";
                    }
                    /*
                    System.out.println("\nServer - PO! nactenim souboru");
                    System.out.println("Server ================");
                    System.out.println(obsahSouboru);
                    System.out.println("Server ================");
                    */

                    boolean pom=false;    //podle toho jaky typ souboru dosel se provede nacteni tahu do pameti
                    if (typ=='t')
                    {
                        lt=Input.notToVector(null, obsahSouboru);
                        //sett=gui.settings;
                        if (lt!=null) pom=true;
                    }
                    else
                    {
                        pom=Input.XmlToVector(null, true, obsahSouboru);
                        lt=Input.xmlToVector(null, obsahSouboru);
                        sett=Input.xmlToSettings(null, obsahSouboru);
                        if ((lt!=null) &&(sett!=null)) pom=true;
                    }
                    System.out.println("Server - vysledek nacteni hry ze souboru: " + pom);

                    if (pom==false)    //soubor byl nevalidni
                    {
                        gui.lblInfo.setText("Server - error on loading saved game!");
                        return;
                    }
                }
            }
            catch ( java.io.InterruptedIOException e )    //timeout
            {
                e.printStackTrace();
                gui.lblInfo.setText("Connection timeout (60sec)!");
                System.out.println("Server - Timed Out (60 sec)!");
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                gui.lblInfo.setText("Server network IOException! (port is probably allready binded)");
                System.out.println("Server - Sitova chyba");
                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                gui.lblInfo.setText("Server network error!");
                System.out.println("Server - Sitova chyba");
                return;
            }

        } ///end of server


        /// -------------------------------------------------------------------------------------------------------------
        /// C L I E N T
        else
        {
            try
            {
                System.out.println("Klient");
                gui.lblInfo.setText("Klient - Connecting...");

                sett.connectionToTheServer = new Socket(gui.settings.remoteIP, gui.settings.remotePort);    //pripoji se na zadanou adresu a port

                gui.lblInfo.setText("Klient - Connection established!");
                System.out.println("Klient se pripojil - connection established!");

                //klient posle svoji barvu a nastavi si ji
                OutputStream out = sett.connectionToTheServer.getOutputStream();
                PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
                ps.println(gui.settings.local);


                //pokud se ma nacist hra ze souboru, posle se obsah souboru
                cesta=gui.settings.ClientCestaKSouboru;
                if ((cesta==null)||(cesta.equals("")))    //tady se nebude nacitat hra
                {
                    System.out.println("Klient - Nebyl zadan soubor - NP");
                    ps.println("no file");
                }
                else        //tady se hra ulozena hra posle serveru
                {
                    System.out.println("Klient - Nacist soubor: \"" + cesta + "\"");
                    ps.println(cesta);

                    FileInputStream  stream = new FileInputStream(new File(cesta));    //soubor se prevede na string
                    FileChannel fc = stream.getChannel();
                    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                    /* Instead of using default, pass in a decoder. */
                    String obsahSouboru = Charset.defaultCharset().decode(bb).toString();
                    stream.close();

                    ps.println(obsahSouboru+"KONEC");    //cely string se posle serveru
                    /*
                    System.out.println("Klient ================");
                    System.out.println(obsahSouboru);
                    System.out.println("Klient ================");
                    */

                    if (cesta.endsWith(".txt")) typ='t';    //nastavi se typ souboru
                    else typ='x';

                    obsahSouboru=obsahSouboru.replace("KONEC", "\n");
                    boolean pom=false;
                    if (typ=='t')
                    {
                        lt=Input.notToVector(null, obsahSouboru);
                        //sett=gui.settings;
                        if (lt!=null) pom=true;
                    }
                    else
                    {
                        pom=Input.XmlToVector(null, true, obsahSouboru);
                        lt=Input.xmlToVector(null, obsahSouboru);
                        //sett=Input.xmlToSettings(null, obsahSouboru);
                        if (lt!=null)  pom=true;
                    }
                    System.out.println("Klient - vysledek nacteni hry ze souboru: " + pom);

                    if (pom==false)    //soubor byl nevalidni
                    {
                        gui.lblInfo.setText("Klient - error on loading saved game!");
                        return;
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println("Klient - Sitova chyba");
            }
        } ///end of client

        gui.inFrNewP2PGameSett.setVisible(false);    //odstani se internal frame s nastavenim settingu
        gui.inFrNewP2PGameSett.dispose();

        if(Dama.isGameSetAndRunning())
        {

            Dama.stopGameThread();
            Dama.interruptGameThread();

            int x=gui.getBounds().x;
            int y=gui.getBounds().y;

            gui.dispose();

            Dama.startingGameThread(x,y);
            try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
        }

        Dama.setLoadedTurns(lt);
        sett.p2penabled = true;
        Dama.setSettings(sett);
        try {Thread.sleep(50);} catch (InterruptedException e) {e.printStackTrace();}
        Dama.startGame();
        Dama.wakeUpGameThread();
        //gui.menuItemSave.setEnabled(true);
        //gui.BSubmitCommand.setEnabled(true);

    }


    /**
     * Fce co ukonci vsechna spojeni
     * @param gui odkaz na gui, kde jsou ulozeny sitove promenne
     */
    public static void killTheSockets(Settings sett)
    {
        if (sett==null) return;
        try
        {

            if (sett.connectionToTheClient!=null)
            {
                OutputStream out = sett.connectionToTheClient.getOutputStream();
                PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
                ps.println("SPOJENI UKONCENO");
                ps.close();
                sett.connectionToTheClient.close();
            }
            if (sett.connectionToTheServer!=null)
            {
                OutputStream out = sett.connectionToTheServer.getOutputStream();
                PrintStream ps = new PrintStream(out, true); // Second param: auto-flush on write = true
                ps.println("SPOJENI UKONCENO");
                ps.close();
                sett.connectionToTheServer.close();
            }
            if (sett.server!=null) sett.server.close();

        }
        catch (IOException e)
        {
            System.out.println("Nepodarilo se zavrit sockety!");
            e.printStackTrace();
        }

    }

}
