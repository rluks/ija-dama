package dama.state;

/**
 * Instance tridy Figure je abstrakci fyzicke figurky na sachovnici. Zname tedy barvu a typ.
 * V nasem projektu jsme take kazde figurce priradili prezdivku.
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class Figure {
    public char color;
    public char type;
    public String name;
    public boolean mustTakeFlag;    //true-musi brat; false-nemusi brat


    /**
     *   Statická tovární metoda pro tvorbu figurky
     * @param color barva figurky 'B' nebo 'W'
     * @param type typ figurky 'P' nebo 'D'
     * @param name prezdivka pro figurku
     */
    private Figure(char color, char type, String name)
    {
        this.color = color;
        this.type = type;
        this.name = name;
        this.mustTakeFlag = false;
    }

    /**
     * Verejna metoda pro tvorbu cerneho pesaka
     * @param name prezdivka pro figurku
     * @return Instance figurky
     */
    public static Figure FigureBlackPiece(String name){
        return new Figure('B', 'P', name);
    }

    /**
     * Verejna metoda pro tvorbu bileho pesaka
     * @param name prezdivka pro figurku
     * @return Instance figurky
     */
    public static Figure FigureWhitePiece(String name){
        return new Figure('W', 'P', name);
    }

    /**
     * Klonovaci funkce
     * @param f
     */
    public Figure(Figure f){
        this.color = f.color;
        this.mustTakeFlag = f.mustTakeFlag;
        this.name = f.name;
        this.type = f.type;
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
