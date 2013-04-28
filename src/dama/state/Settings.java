package dama.state;


/**
 * Instance této třidy udržuje nastavení probíhající hry (síťová, proti AI, barvy hráčů, apod.)
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Settings {

    String white;
    String black;

    String local;
    String remoteIP;
    int remotePort;

    protected String xml;

    public Settings(String white, String black, String local, String remoteIP, int remotePort){

        this.white = white;
        this.black = black;

        this.local = local;
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
    }

    public String getSettingsString(){
        if(this.local != null && this.remoteIP != null && this.remotePort != 0){
            System.out.println("White Player: " + this.white + "| Black Player: " + this.black + " (LocalPlayer: " + this.local + ", RemotePlayer: " + this.remoteIP + ":" + this.remotePort + ")");
            return "White Player: " + this.white + "| Black Player: " + this.black + " (LocalPlayer: " + this.local + ", RemotePlayer: " + this.remoteIP + ":" + this.remotePort + ")";
        }else{
            System.out.println("White Player: " + this.white + "| Black Player: " + this.black);
            return "White Player: " + this.white + "| Black Player: " + this.black;
        }
    }

    public String getXML(){
        this.xml = new String();
        xml += "<settings>\n";
            xml += "\t<white>";
                xml += this.white;
            xml += "</white>\n";
            xml += "\t<black>";
                xml += this.black;
            xml += "</black>\n";
            xml += "\t<P2P enabled=\""; xml += (this.local != null)? "yes" : "no"; xml += "\">\n";
                xml += "\t\t<local>"; xml += (this.local != null)? this.local : ""; xml += "</local>\n";
                xml += "\t\t<remoteIP>"; xml += (this.remoteIP != null)? this.remoteIP : ""; xml += "</remoteIP>\n";
                xml += "\t\t<remotePort>"; xml += (this.remotePort != 0)? this.remotePort : ""; xml += "</remotePort>\n";
            xml += "\t</P2P>\n";
        xml += "</settings>";
        return this.xml;
    }

}
