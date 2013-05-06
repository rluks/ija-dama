package dama.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import dama.Dama;
import dama.operations.Moves;
import dama.state.Position;
import dama.state.Settings;

/**
 * Tato třída zastřešuje příjem informací od hráče/soupeře.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Input
{
    /**
     * Nacte ulozenou hru v XML a prevede nastaveni a tahy ulozoene v souboru na interni reprezentaci.
     * @param cesta            cesta k souboru ktery se ma nacist
     * @param setSettings    boolean urcujici jestli ma fce nastavit settings
     * @param obsahSouboru    obsah souboru ulozeny ve stringu (v pripade ze se nanacita soubor z disku)
     * @return                pokud vse probehlo v poradku (soubor byl validni), vraci se true, jinak false
     */
    public static boolean XmlToVector (String cesta, boolean setSettings, String obsahSouboru)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();
        Settings set;

        try
        {
            File fXmlFile;
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;
            if (obsahSouboru==null)
            {
                fXmlFile = new File(cesta);
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(fXmlFile);
            }
            else
            {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(obsahSouboru));
                doc=dBuilder.parse(is);
            }


            doc.getDocumentElement().normalize();

            //promenny pro setting
            String w="";
            String b="";
            int    ai=0;
            String p2pEn="";
            String p2pLoc="";
            String p2pRemIp="";
            int    p2pRemP=0;


            /// nacteni settings
            NodeList nSettings = doc.getElementsByTagName("settings");
            if (nSettings.getLength()!=1)
            {
                System.out.println("!!setings nenalezeny!!");
                return false;
            }
            Node nNodeS = nSettings.item(0);
            if (nNodeS.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElementS = (Element) nNodeS;
                w=eElementS.getElementsByTagName("white").item(0).getTextContent();
                b=eElementS.getElementsByTagName("black").item(0).getTextContent();
                String ais=eElementS.getElementsByTagName("AI").item(0).getTextContent();
                ai = Integer.parseInt(ais);
                System.out.println("Bily: " + w +"; cerny: " + b + "; ai speed: " + ai);
            }

            NodeList nSettingsP = doc.getElementsByTagName("P2P");
            if (nSettingsP.getLength()!=1)
            {
                System.out.println("!!setings P2P nenalezeny!!");
                return false;
            }
            Node nNodeSP = nSettingsP.item(0);
            if (nNodeSP.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElementSP = (Element) nNodeSP;
                p2pEn=eElementSP.getAttribute("enabled");
                if (p2pEn.equals("yes"))
                {
                    p2pLoc=eElementSP.getElementsByTagName("local").item(0).getTextContent();
                    p2pRemIp=eElementSP.getElementsByTagName("remoteIP").item(0).getTextContent();
                    String p2pRemPS=eElementSP.getElementsByTagName("remotePort").item(0).getTextContent();
                    p2pRemP = Integer.parseInt(p2pRemPS);
                }
                System.out.println("P2P enable:" + p2pEn + "; local: " + p2pLoc + "; remIP: " + p2pRemIp + "; remPort: " + p2pRemP);

            }

            if ( (p2pEn.equals("no")) || (p2pEn.equals("yes")) ) set=new Settings(w, b, null, null, 0, ai, null);
            else return false;


            /// nacteni jednotlivych tahu
            NodeList nList = doc.getElementsByTagName("turn");
            char natahu = 'W';
            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;

                    //barva hrace na tahu
                    String pom=eElement.getAttribute("player_color");
                    if (pom.length()!=1) return false;
                    char p=pom.charAt(0);
                    if (natahu==p)
                    {
                        if (natahu=='W') natahu='B';
                        /*if (natahu=='B') natahu='W';
                        else return false;
                        */
                        else natahu='W';
                    }
                    else
                    {
                        System.out.println("Spatny poradi hracu");
                        return false;
                    }

                    //origin pozice
                    pom=eElement.getElementsByTagName("origin_row").item(0).getTextContent();
                    if (pom.length()!=1) return false;
                    int o_r = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (o_r>7)||(o_r<0) ) return false;

                    pom=eElement.getElementsByTagName("origin_col").item(0).getTextContent();
                    if (pom.length()!=1) return false;
                    int o_c = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (o_c>7)||(o_c<0) ) return false;

                    Position o=new Position(o_r, o_c);

                    //destination pozice
                    pom=eElement.getElementsByTagName("destination_row").item(0).getTextContent();
                    if (pom.length()!=1) return false;
                    int d_r = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (d_r>7)||(d_r<0) ) return false;

                    pom=eElement.getElementsByTagName("destination_col").item(0).getTextContent();
                    if (pom.length()!=1) return false;
                    int d_c = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (d_c>7)||(d_c<0) ) return false;

                    Position d=new Position(d_r, d_c);

                    Turn turn = new Turn(p, o, d);

                    TurnVector.add(turn);

                    /*
                    System.out.println("Hrac : " + p);
                    System.out.println("Origin: " + o_r + ":" + o_c);
                    System.out.println("Destination: " + d_r + ":" + d_c);
                    System.out.println("-----------------------------------");
                    */
                }

            }
            //System.out.println("===============================================");
        }
        catch (Exception e)
        {
            System.out.println("!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            return false;
        }

        if (setSettings)if (!Dama.setSettings(set)) return false;
        if (!Dama.setLoadedTurns(TurnVector))return false;
        return true;
    }

    /**
     * Fce ktera nacte hru ulozenou v zakladni notaci a uvede desku hry do stavu ve kterem byl v notaci ulozen
     * @param cesta            cesta k souboru ze ktereho se ma cist
     * @param setSettings    boolean jestli ma fce nastavovat settings
     * @param obsahSouboru    pripadny obsah souboru (v tom pripade se nenacita z disku pomoci cesty)
     * @return                vraci true pokud se vsechno v poradku provedlo (validni soubor, nastaveni tahu, settingu,...)
     */
    public static boolean NotToVector(String cesta, boolean setSettings, String obsahSouboru)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();        //sem se ukladaji nactene tahy
        //File fXmlFile = new File(cesta);
        String string="";
        FileInputStream stream;

        if (obsahSouboru!=null) string=obsahSouboru;    //necte se z disku, notace byla predana ve stringu jako parametr
        else        //nacita se notace ze souboru ulozenem na disku
        {
            try
            {
                stream = new FileInputStream(new File(cesta));
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                string = Charset.defaultCharset().decode(bb).toString();
                stream.close();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }

        int pozice=0;
        int predchozi_pozice=0;

        int pocitadlo_tahu=1;
        while(pozice!=-1)        //soubor se cte po radcich
        {
            predchozi_pozice=pozice;
            pozice=string.indexOf("\n", pozice);

            if (pozice!=-1)        //radek s dvema (pul)tahy (bily, cerny)
            {
                String radek=string.substring(predchozi_pozice, pozice);
                pozice=pozice+"\n".length();

                int tecka=radek.indexOf(".");
                if (tecka==-1) return false;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);    //ziska se poradove cislo tahu
                //System.out.println("#" + numero);
                if (pocitadlo_tahu!=Integer.parseInt(numero))return false;    //kontrola, jestli jdou poradova cisla spravne za sebou
                else pocitadlo_tahu++;

                radek=radek.substring(tecka);
                //System.out.println(radek);

                int mezera=radek.indexOf(" ");
                if (mezera==-1) return false;
                else mezera=mezera+1;

                String t1=radek.substring(0, mezera-1);    //ziska se prvni (pul)tah (bily)
                String t2=radek.substring(mezera);        //a druhy (cerny)

                char natahu='W';
                for (int i=0; i<2; i++)
                {
                    String tah;
                    if (i==0) tah=t1;
                    else tah=t2;
                    Turn turn=Turn.NotStringToTurn(tah, natahu);    //oba tahy se prevedou ze stringu na tridu Turn
                    TurnVector.add(turn);                        //a ulozi se do zasobniku
                    natahu='B';
                }
            }
            else if (string.substring(predchozi_pozice).equals("")) break;    //posledni prazdny radek
            else    //posledni radek s jednim (pul)tahem (jen bily)
            {
                String radek=string.substring(predchozi_pozice);

                int tecka=radek.indexOf(".");
                if (tecka==-1) return false;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);    //ziska se poradove cislo tahu a zkontrolije se jestli je dobre
                if (pocitadlo_tahu!=Integer.parseInt(numero))return false;

                radek=radek.substring(tecka);    //string tahu se prevede na Turn a ulozi se na zasobnik
                Turn turn=Turn.NotStringToTurn(radek, 'W');
                TurnVector.add(turn);
            }
        }

        Settings set=new Settings("human", "human", null, null, 0, 0, null);//pri nacteni z notace se pouzije implicitni nastaveni
        if (setSettings) if (!Dama.setSettings(set)) return false;    //nastavi se settings
        if (!Dama.setLoadedTurns(TurnVector))return false;    //dame se preda vektor tahu
        return true;
    }

    /**
     * Vraci obsah notace jako Vector<Turn>. Castecna copypasta XmlToVector.
     * @param cesta            cesta k souboru
     * @param obsahSouboru    pripadne obsah souboru ve stringu
     * @return                v pripade ze je soubor validni, tak vraci vektor tahu, v pripade chyby vraci null
     */
    public static Vector<Turn> notToVector(String cesta, String obsahSouboru)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();
        String string="";
        FileInputStream stream;

        if (obsahSouboru!=null)string=obsahSouboru;
        else
        {
            try
            {
                stream = new FileInputStream(new File(cesta));
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                /* Instead of using default, pass in a decoder. */
                string = Charset.defaultCharset().decode(bb).toString();
                stream.close();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        }

        int pozice=0;
        int predchozi_pozice=0;

        int pocitadlo_tahu=1;
        while(pozice!=-1)
        {
            predchozi_pozice=pozice;
            pozice=string.indexOf("\n", pozice);


            if (pozice!=-1)
            {
                String radek=string.substring(predchozi_pozice, pozice);
                pozice=pozice+"\n".length();

                int tecka=radek.indexOf(".");
                if (tecka==-1) return null;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);
                if (pocitadlo_tahu!=Integer.parseInt(numero))return null;
                else pocitadlo_tahu++;

                radek=radek.substring(tecka);

                int mezera=radek.indexOf(" ");
                if (mezera==-1) return null;
                else mezera=mezera+1;

                String t1=radek.substring(0, mezera-1);
                String t2=radek.substring(mezera);

                char natahu='W';

                for (int i=0; i<2; i++)
                {
                    String tah;
                    if (i==0) tah=t1;
                    else tah=t2;
                    Turn turn=Turn.NotStringToTurn(tah, natahu);
                    TurnVector.add(turn);

                    natahu='B';
                }

            }
            else if (string.substring(predchozi_pozice).equals("")) break;
            else
            {
                String radek=string.substring(predchozi_pozice);

                int tecka=radek.indexOf(".");
                if (tecka==-1) return null;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);
                if (pocitadlo_tahu!=Integer.parseInt(numero))return null;

                radek=radek.substring(tecka);
                Turn turn=Turn.NotStringToTurn(radek, 'W');
                TurnVector.add(turn);

            }
        }

        return TurnVector;
    }


    /**
     * Vraci z xml instanci Settings. Castecna copypasta NotToVector.
     * @param cesta            cesta k souboru
     * @param obsahSouboru    pripadne obsah souboru ulozeny ve stringu
     * @return                vraci instanci settings pokud je soubor validni, jinak vraci null
     */
    public static Settings xmlToSettings (String cesta, String obsahSouboru)
    {
        Settings set;

        try
        {
            File fXmlFile;
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;
            if (obsahSouboru==null)
            {
                fXmlFile = new File(cesta);
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(fXmlFile);
            }
            else
            {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(obsahSouboru));
                doc=dBuilder.parse(is);
            }


            doc.getDocumentElement().normalize();

            //promenny pro setting
            String w="";
            String b="";
            int    ai=0;
            String p2pEn="";
            String p2pLoc="";
            String p2pRemIp="";
            int    p2pRemP=0;


            /// nacteni settings
            NodeList nSettings = doc.getElementsByTagName("settings");
            if (nSettings.getLength()!=1)
            {
                System.out.println("!!setings nenalezeny!!");
                return null;
            }
            Node nNodeS = nSettings.item(0);
            if (nNodeS.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElementS = (Element) nNodeS;
                w=eElementS.getElementsByTagName("white").item(0).getTextContent();
                b=eElementS.getElementsByTagName("black").item(0).getTextContent();
                String ais=eElementS.getElementsByTagName("AI").item(0).getTextContent();
                ai = Integer.parseInt(ais);
                System.out.println("Bily: " + w +"; cerny: " + b + "; ai speed: " + ai);
            }

            NodeList nSettingsP = doc.getElementsByTagName("P2P");
            if (nSettingsP.getLength()!=1)
            {
                System.out.println("!!setings P2P nenalezeny!!");
                return null;
            }
            Node nNodeSP = nSettingsP.item(0);
            if (nNodeSP.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElementSP = (Element) nNodeSP;
                p2pEn=eElementSP.getAttribute("enabled");
                if (p2pEn.equals("yes"))
                {
                    p2pLoc=eElementSP.getElementsByTagName("local").item(0).getTextContent();
                    p2pRemIp=eElementSP.getElementsByTagName("remoteIP").item(0).getTextContent();
                    String p2pRemPS=eElementSP.getElementsByTagName("remotePort").item(0).getTextContent();
                    p2pRemP = Integer.parseInt(p2pRemPS);
                }
                System.out.println("P2P enable:" + p2pEn + "; local: " + p2pLoc + "; remIP: " + p2pRemIp + "; remPort: " + p2pRemP);

            }

            if ( (p2pEn.equals("no")) || (p2pEn.equals("yes")) ) set=new Settings(w, b, null, null, 0, ai, null);
            else return null;

            return set;
        }
        catch (Exception e)
        {
                System.out.println("!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!");
                e.printStackTrace();
                return null;
        }
    }

    /**
     * Vraci ze xml souboru vektor tahu Vector<Turn>. Castecna copypasta XmlToVector
     * @param cesta            cesta k souboru
     * @param obsahSouboru    pripadny obsah souboru
     * @return                vrati vektor tahu
     */
    public static Vector<Turn> xmlToVector (String cesta, String obsahSouboru)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();

        try
        {
            File fXmlFile;
            DocumentBuilderFactory dbFactory;
            DocumentBuilder dBuilder;
            Document doc;
            if (obsahSouboru==null)
            {
                fXmlFile = new File(cesta);
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                doc = dBuilder.parse(fXmlFile);
            }
            else
            {
                dbFactory = DocumentBuilderFactory.newInstance();
                dBuilder = dbFactory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(obsahSouboru));
                doc=dBuilder.parse(is);
            }


            doc.getDocumentElement().normalize();

            /// nacteni jednotlivych tahu
            NodeList nList = doc.getElementsByTagName("turn");
            char natahu = 'W';
            for (int temp = 0; temp < nList.getLength(); temp++)
            {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;

                    //barva hrace na tahu
                    String pom=eElement.getAttribute("player_color");
                    if (pom.length()!=1) return null;
                    char p=pom.charAt(0);
                    if (natahu==p)
                    {
                        if (natahu=='W') natahu='B';
                        /*if (natahu=='B') natahu='W';
                        else return false;
                        */
                        else natahu='W';
                    }
                    else
                    {
                        System.out.println("Spatny poradi hracu");
                        return null;
                    }

                    //origin pozice
                    pom=eElement.getElementsByTagName("origin_row").item(0).getTextContent();
                    if (pom.length()!=1) return null;
                    int o_r = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (o_r>7)||(o_r<0) ) return null;

                    pom=eElement.getElementsByTagName("origin_col").item(0).getTextContent();
                    if (pom.length()!=1) return null;
                    int o_c = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (o_c>7)||(o_c<0) ) return null;

                    Position o=new Position(o_r, o_c);

                    //destination pozice
                    pom=eElement.getElementsByTagName("destination_row").item(0).getTextContent();
                    if (pom.length()!=1) return null;
                    int d_r = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (d_r>7)||(d_r<0) ) return null;

                    pom=eElement.getElementsByTagName("destination_col").item(0).getTextContent();
                    if (pom.length()!=1) return null;
                    int d_c = (int)((char)pom.charAt(0)-(char)'0');
                    if ( (d_c>7)||(d_c<0) ) return null;

                    Position d=new Position(d_r, d_c);

                    Turn turn = new Turn(p, o, d);

                    TurnVector.add(turn);
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("!!!!!!!!!!!Exception!!!!!!!!!!!!!!!!!");
            e.printStackTrace();
            return null;
        }

        return TurnVector;
    }

    /**
     * V pripade ze fce dostane validni tah, vzbudi tridu dama a preda ji validni tah.
     * Pokud je tah nevalidni, nikoho nebudi.
     * @param turn    tah ktery se ma validovat a pripadne predat dame
     */
    public static void inputTurn(Turn turn)
    {

        if(Moves.isTurnValid(turn, Dama.getDesk()))
        {
            Dama.setTurn(turn);
            Dama.wakeUpGameThread();
        }
        else
        {
            System.out.println("inputTurn: nevalidni tah, hra MUSI nastavit napovedu");
            Dama.setTurn(turn);
            //try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}
            Dama.wakeUpGameThread();
        }
    }


}
