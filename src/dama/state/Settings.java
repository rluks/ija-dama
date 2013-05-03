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

    public int aiWait;

    protected String xml;

    /**
     * Konstruktor
     * @param white String obsahujici 'Human' nebo 'AI' - urcuje kdo bude hrat za bile
     * @param black String obsahujici 'Human' nebo 'AI' - urcuje kdo bude hrat za cerne
     * @param local String barva hrace na tomto pocitaci: 'White' nebo 'Black', nesitova hra? null
     * @param remoteIP String s IP adresou vzdaleneho klienta, nesitova hra? null
     * @param remotePort int s cislem portu vzdaleneho klienta, nesitova hra? 0
     * @param aiWait pocet milisekund jak dlouho bude AI cekat pred svym tahem, nesitova hra? 0
     */
    public Settings(String white, String black, String local, String remoteIP, int remotePort, int aiWait){

        this.white = white;
        this.black = black;

        this.local = local;
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;

        this.aiWait = aiWait;

    }

    /**
     * Vraci retezec s nastavenim hry
     * @return String obsahujici polozky nastaveni
     */
    public String getSettingsString(){
        if(this.local != null && this.remoteIP != null && this.remotePort != 0){//sitova hra
            System.out.println("White Player: " + this.white + "| Black Player: " + this.black + " (LocalPlayer: " + this.local + ", RemotePlayer: " + this.remoteIP + ":" + this.remotePort + " |aiWait: " + this.aiWait + ")");
            return "White Player: " + this.white + "| Black Player: " + this.black + " (LocalPlayer: " + this.local + ", RemotePlayer: " + this.remoteIP + ":" + this.remotePort + " |aiWait: " + this.aiWait + ")";
        }else{//lokalni hra
            System.out.println("White Player: " + this.white + "| Black Player: " + this.black + " |aiWait: " + this.aiWait + ")");
            return "White Player: " + this.white + "| Black Player: " + this.black + " |aiWait: " + this.aiWait + ")";
        }
    }

    /**
     * Vraci retezec strukturovany jako XML s nastavenim hry
     * @return String v xml formatu
     */
    public String getXML(){
        this.xml = new String();
        xml += "\t<settings>\n";
            xml += "\t\t<white>";
                xml += this.white;
            xml += "</white>\n";
            xml += "\t\t<black>";
                xml += this.black;
            xml += "</black>\n";
            xml += "\t\t<AI>";
                xml += this.aiWait;
            xml += "</AI>\n";
            xml += "\t\t<P2P enabled=\""; xml += (this.local != null)? "yes" : "no"; xml += "\">\n";
                xml += "\t\t\t<local>"; xml += (this.local != null)? this.local : ""; xml += "</local>\n";
                xml += "\t\t\t<remoteIP>"; xml += (this.remoteIP != null)? this.remoteIP : ""; xml += "</remoteIP>\n";
                xml += "\t\t\t<remotePort>"; xml += (this.remotePort != 0)? this.remotePort : ""; xml += "</remotePort>\n";
            xml += "\t\t</P2P>\n";
        xml += "\t</settings>";
        return this.xml;
    }

}
