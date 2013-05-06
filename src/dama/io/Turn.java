package dama.io;

import dama.state.Position;

/**
 * Instance této třídy pro nás představují jednotný prostředek pro předávání informací o tazích
 * v rámci naší aplikace.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Turn
{
    public char playerColor;
    public Position origin;
    public Position destination;


    /**
     * Konstruktor.
     * @param playerColor    barva hrace na tahu
     * @param origin        pozice figurky se kterou hrac hraje
     * @param destination   pozice na kterou hrac s touto figurkou hraje
     */
    public Turn(char playerColor, Position origin, Position destination)
    {
        this.playerColor = playerColor;
        this.origin=new Position(origin.row, origin.col);
        this.destination = new Position(destination.row, destination.col);
    }

    /**
     * Klonovaci funkce
     * @param turn    tah ktery kopirujeme
     */
    public Turn(Turn turn)
    {
        this.playerColor = turn.playerColor;
        Position origin = new Position(turn.origin);
        Position destination = new Position(turn.destination);

        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Obaluje konstruktor, vytvari jen tahy cerneho hrace
     * @param     origin
     * @param     destination
     * @return    vraci novou instanci tridy Turn cerneho hrace
     */
    public static Turn TurnBlack(Position origin, Position destination)
    {
        return new Turn('B', origin, destination);
    }

    /**
     * Obaluje konstruktorm vytvari jen tahy bileho hrace
     * @param     origin
     * @param     destination
     * @return    vraci novou instanci tridy Turn bileho hrace
     */
    public static Turn TurnWhite(Position origin, Position destination)
    {
        return new Turn('W', origin, destination);
    }

    /**
     * Z tahu vytvori string. Vyuziva se jen u pomocnych vypisu do konzole.
     * @return    vraci string ve kterem jsou obsazeny vsechny informace o danem tahu
     */
    public String getTurnString()
    {
        String s = new String();
        s += "playerColor: " + this.playerColor;
        s += " | origin: " + this.origin.getStr() + " (row: " + this.origin.row + " col: " + this.origin.col + ")";
        s += " | destination: " + this.destination.getStr() + " (row: " + this.destination.row + " col: " + this.destination.col + ")";

        return s;
    }

    /**
     * Prevede string v zakladni notaci na instanci tridy Turn
     * @param string    tah v zakladni notaci (pr. a4-b3)
     * @param natahu    barva hrace, ktery tah provadi ('B' nebo 'W')
     * @return            vraci instanci tridyTurn vytvorenou podle zadanych parametru
     */
    public static Turn NotStringToTurn(String string, char natahu)
    {
        if (string.length()!=5) return null;    //kontrola delky
        char c=string.charAt(2);
        if (!((c=='-') ||(c=='x'))) return null;    //jestli je 3. znak 'x'(vyhazovani) nebo '-'(pohyb)

        String p1s=string.substring(0,2);    //puvodni pozice figurky (1. a 2. znak)
        String p2s=string.substring(3,5);    //pozice na kterou se figurka dostane timto tahem (4. a 5. znak)
        Position p1=Position.positionStrToPositionLowerCase(p1s); //konverze stringu na instance trid Position
        Position p2=Position.positionStrToPositionLowerCase(p2s);
        if ( (p1==null)||(p2==null) ) return null;    //kontrola jestli jsou pozice validni

        Turn turn=new Turn(natahu, p1, p2);    //tah se vytvori a vrati
        return turn;
    }



}
