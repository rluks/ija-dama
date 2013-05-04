package dama.io;

import java.io.File;
import java.io.FileInputStream;
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

import dama.Dama;
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
    /**    Fce pro prevod XML souboru na interni reprezentaci tahu
     * @param cesta     string s cestou k souboru
     * @return             vraci true pokud je xml validni a podarilo se jej konvertovat na vektor tahu a ulozit
     */
    public static boolean XmlToVector (String cesta)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();

        try
        {
            File fXmlFile = new File(cesta);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            //promenny pro setting
            String w="";
            String b="";
            int    ai=0;
            String p2pEn="";
            String p2pLoc="";
            String p2pRemIp="";
            int    p2pRemP=0;
            Settings set;

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

            if (p2pEn.equals("yes")) set=new Settings(w, b, p2pLoc, p2pRemIp, p2pRemP, ai);
            else if (p2pEn.equals("no")) set=new Settings(w, b, null, null, 0, ai);
            else return false;
            if (!Dama.setSettings(set)) return false;

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

        if (Dama.setLoadedTurns(TurnVector))return true;
        return false;
    }

    public static boolean NotToVector(String cesta)
    {
        Vector<Turn> TurnVector=new Vector<Turn>();
        //File fXmlFile = new File(cesta);
        String string="";
        FileInputStream stream;

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
        /*
        System.out.println("===============================");
        System.out.println("Obsah souboru:\n" + string);
        */
        //System.out.println("===============================");

        int pozice=0;
        int predchozi_pozice=0;

        int pocitadlo_tahu=1;
        while(pozice!=-1)
        {
            predchozi_pozice=pozice;
            pozice=string.indexOf("\n", pozice);

            //System.out.println("Pozice: " + predchozi_pozice+ "->" + pozice);


            if (pozice!=-1)
            {
                String radek=string.substring(predchozi_pozice, pozice);
                //System.out.println("Pozice: " + predchozi_pozice+ "->" + pozice + " : " + radek);
                pozice=pozice+"\n".length();

                int tecka=radek.indexOf(".");
                if (tecka==-1) return false;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);
                //System.out.println("#" + numero);
                if (pocitadlo_tahu!=Integer.parseInt(numero))return false;
                else pocitadlo_tahu++;



                radek=radek.substring(tecka);
                //System.out.println(radek);

                int mezera=radek.indexOf(" ");
                if (mezera==-1) return false;
                else mezera=mezera+1;

                String t1=radek.substring(0, mezera-1);
                String t2=radek.substring(mezera);
                if ( (t1.length()!=5)||(t2.length()!=5) ) return false;

                char natahu='W';

                for (int i=0; i<2; i++)
                {
                    String tah;
                    if (i==0) tah=t1;
                    else tah=t2;

                    char c=tah.charAt(2);
                    if (!((c=='-') ||(c=='x'))) return false;
                    String p1s=tah.substring(0,2);
                    String p2s=tah.substring(3,5);
                    //System.out.println(p1s + "->" + p2s);
                    Position p1=Position.positionStrToPositionLowerCase(p1s);
                    Position p2=Position.positionStrToPositionLowerCase(p2s);
                    if ( (p1==null)||(p2==null) ) return false;

                    Turn turn=new Turn(natahu, p1, p2);
                    TurnVector.add(turn);

                    natahu='B';
                }

            }
            else if (string.substring(predchozi_pozice).equals("")) break;
            else
            {
                String radek=string.substring(predchozi_pozice);

                int tecka=radek.indexOf(".");
                if (tecka==-1) return false;
                else tecka=tecka+2;

                String numero=radek.substring(0, tecka-2);
                //System.out.println("#" + numero);
                if (pocitadlo_tahu!=Integer.parseInt(numero))return false;

                radek=radek.substring(tecka);
                if (radek.length()!=5) return false;

                char c=radek.charAt(2);
                if (!((c=='-') ||(c=='x'))) return false;
                String p1s=radek.substring(0,2);
                String p2s=radek.substring(3,5);
                //System.out.println(p1s + "->" + p2s);
                Position p1=Position.positionStrToPositionLowerCase(p1s);
                Position p2=Position.positionStrToPositionLowerCase(p2s);
                if ( (p1==null)||(p2==null) ) return false;

                Turn turn=new Turn('W', p1, p2);
                TurnVector.add(turn);

            }
            //System.out.println("-----------------------");
        }
        //System.out.println("=============K=================");


        Settings set=new Settings("human", "human", null, null, 0, 0);
        if (!Dama.setSettings(set)) return false;
        if (!Dama.setLoadedTurns(TurnVector))return false;
        return true;
    }


}
