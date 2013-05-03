package dama.io;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Třída, která vypisuje informace hráči a odesílá informace protihráči.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Output {

    public static void printTurnVector(Vector<Turn> turnVector) throws Exception{

     PrintWriter pw = new PrintWriter(new FileWriter("turnvector.xml"));

     Turn tmp;
     String xml = new String();
     String turn = new String();

     xml += "\t<Turns>\n";

     for(int i = 0; i < turnVector.size(); i++)
     {
         tmp = turnVector.get(i);

         System.out.println(tmp.getTurnString());

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

     //System.out.println("Cele xml:");
     //System.out.println(xml);
     pw.print(xml);

     pw.close();

    }

    public static String getPlayerColorString(char playerColor){
        return (playerColor == 'W') ? "white" : "black";
    }

}
