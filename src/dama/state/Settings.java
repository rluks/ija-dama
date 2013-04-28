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

    public Settings(String white, String black){
        this.white = white;
        this.black = black;
    }

    public String getSettingsString(){
        return "White Player: " + this.white + "| Black Player: " + this.black;
    }

}
