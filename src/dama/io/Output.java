package dama.io;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

import dama.state.Settings;

/**
 * Třída, která vypisuje informace hráči a odesílá informace protihráči.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Output {

    /**
     * Ulozi string do souboru.
     * @param string    obsah souboru
     * @param filepath    cesta k souboru
     * @throws Exception    muze hazet vyjimky pokud se mu nepodari soubor ulozit/zavrit
     */
    public static void printStringToFile(String string, String filepath) throws Exception{

         PrintWriter pw = new PrintWriter(new FileWriter(filepath));
         pw.print(string);
         pw.close();
    }

    /**
     * Vytvori kompletni xml se settings a tahy
     * @param settings        instance tridy setting, ktera se ma ulozit do xml
     * @param turnVector      vektor tahu, ktery se ma ulozit do xml
     * @return                xml obsahujici nastaveni a tahy
     */
    public static String getXmlFromSettingsAndTurnVector(Settings settings, Vector<Turn> turnVector)
    {

        String xml = new String();

        String turnsXml = Output.getTurnVectorString(turnVector);
        String settingsXml = settings.getXML();

        xml = "<Dama>\n" + settingsXml + "\n" + turnsXml + "\n</Dama>";
        return xml;
    }

    /**
     * Vytvori string s tahy ulozenymi v xml
     * @param turnVector    vektor tahu podle ktereho se tvori xml
     * @return    vrati string s vytvorenym xml
     */
    public static String getTurnVectorString(Vector<Turn> turnVector){

     Turn tmp;
     String xml = new String();
     String turn = new String();

     xml += "\t<Turns>\n";

     for(int i = 0; i < turnVector.size(); i++)
     {
         tmp = turnVector.get(i);

         //System.out.println(tmp.getTurnString());

         turn = "";

         turn += "\t\t<turn player_color=\"";turn += tmp.playerColor;turn += "\">\n";

         turn += "\t\t\t<origin_row>";turn += tmp.origin.row;turn += "</origin_row>\n";
         turn += "\t\t\t<origin_col>";turn += tmp.origin.col;turn += "</origin_col>\n";

         turn += "\t\t\t<destination_row>";turn += tmp.destination.row;;turn += "</destination_row>\n";
         turn += "\t\t\t<destination_col>";turn += tmp.destination.col;turn += "</destination_col>\n";

         turn += "\t\t</turn>\n";

         xml += turn;
     }

     xml += "\t</Turns>";

     return xml;
    }

    /**
     * Podle charu s barvou vrati string s barvou hrace
     * @param playerColor    char s barvou ('B' nebo 'W')
     * @return                vrati string s barvou
     */
    public static String getPlayerColorString(char playerColor)
    {
        return (playerColor == 'W') ? "white" : "black";
    }

}
