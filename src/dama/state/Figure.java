package dama.state;

public class Figure {
    public char color;
    public char type;
    public String name;
    public boolean mustTakeFlag;    //true-musi brat; false-nemusi brat

    //Static factory method (statická tovární metoda)
    private Figure(char color, char type, String name)
    {
        this.color = color;
        this.type = type;
        this.name = name;
        this.mustTakeFlag = false;
    }

    public static Figure FigureBlackPiece(String name){
        return new Figure('B', 'P', name);
    }

    public static Figure FigureWhitePiece(String name){
        return new Figure('W', 'P', name);
    }
}
