package dama.io;

import dama.state.Position;

/**
 * Instance této třídy pro nás představují jednotný prostředek pro předávání informací o tazích
 * v rámci naší aplikace.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Turn {
    public char playerColor;
    public Position origin;
    public Position destination;


    /**
     * Konstruktor.
     * @param playerColor    barva hrace na tahu
     * @param origin        pozice figurky se kterou hrac hraje
     * @param destination   pozice na kterou hrac s touto figurkou hraje
     */
    public Turn(char playerColor, Position origin, Position destination){
        this.playerColor = playerColor;
        //this.origin = origin;
        this.origin=new Position(origin.row, origin.col);
        //this.destination = destination;
        this.destination = new Position(destination.row, destination.col);
    }

    /**
     * Klonovaci funkce
     * @param turn
     */
    public Turn(Turn turn){
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
    public static Turn TurnBlack(Position origin, Position destination){
        return new Turn('B', origin, destination);
    }

    /**
     * Obaluje konstruktorm vytvari jen tahy bileho hrace
     * @param     origin
     * @param     destination
     * @return    vraci novou instanci tridy Turn bileho hrace
     */
    public static Turn TurnWhite(Position origin, Position destination){
        return new Turn('W', origin, destination);
    }

    public String getTurnString(){
        String s = new String();
        s += "playerColor: " + this.playerColor;
        s += " | origin: " + this.origin.getStr() + " (row: " + this.origin.row + " col: " + this.origin.col + ")";
        s += " | destination: " + this.destination.getStr() + " (row: " + this.destination.row + " col: " + this.destination.col + ")";

        return s;
    }
}
