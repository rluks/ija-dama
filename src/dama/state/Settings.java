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

}
