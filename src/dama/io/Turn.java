package dama.io;

import dama.state.Position;

public class Turn {
    public char playerColor;
    public Position origin;
    public Position destination;

    public Turn(char playerColor, Position origin, Position destination){
        this.playerColor = playerColor;
        this.origin = origin;
        this.destination = destination;
    }

    public static Turn TurnBlack(Position origin, Position destination){
        return new Turn('B', origin, destination);
    }

    public static Turn TurnWhite(Position origin, Position destination){
        return new Turn('W', origin, destination);
    }
}
