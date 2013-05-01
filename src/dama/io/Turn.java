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

    public Turn(char playerColor, Position origin, Position destination){
        this.playerColor = playerColor;
        //this.origin = origin;
        this.origin=new Position(origin.row, origin.col);
        //this.destination = destination;
        this.destination = new Position(destination.row, destination.col);
    }

    public static Turn TurnBlack(Position origin, Position destination){
        return new Turn('B', origin, destination);
    }

    public static Turn TurnWhite(Position origin, Position destination){
        return new Turn('W', origin, destination);
    }
}
