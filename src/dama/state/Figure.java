package dama.state;

/**
 * Instance třídy Figure je abstrakcí fyzické figurky na šachovnici. Známe tedy barvu a typ.
 * V našem projektu jsme také každé figurce přiřadili jméno.
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
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

    @Override
    public boolean equals(Object p){

        if(p == this){return true;}

        if(p == null || p.getClass() != this.getClass()){
            return false;
        }

        Figure f = (Figure)p;

        if((this.color != f.color) || (this.type != f.type) || (this.name != f.name) || (this.mustTakeFlag != f.mustTakeFlag)){
            return false;
        }else{
            return true;
        }

    }
}
