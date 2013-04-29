package dama.state;


/**
 * Instance této třidy udržuje nastavení probíhající hry (síťová, proti AI, barvy hráčů, apod.)
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Settings {

    public String white;
    public String black;

    public String local;
    public String remoteIP;
    public int remotePort;

    protected String xml;

    public Settings(){

    }

    /**
     * @param white Human nebo AI
     * @param black Human nebo AI
     * @param local barva hrace na tomto pocitaci: White nebo Black nesitova hra? null
     * @param remoteIP nesitova hra? null
     * @param remotePort nesitova hra? 0
     */
    public void setSettings(String white, String black, String local, String remoteIP, int remotePort){

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
