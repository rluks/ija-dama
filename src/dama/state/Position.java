package dama.state;

/**
 * Position je trida uchovavajici souradnice sachovnice. Kazda pozice umi vracet svuj retezec
 * v zapisu "A1". Take lze vytvorit novou instanci pozice pomoci zaslani retezce v tomto formatu
 * metode positionStrToPosition().
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class Position
{
    public int row;
    public int col;

    /**
     * Konstruktor.
     * @param row    radek
     * @param col    sloupec
     */
    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * Klonovaci funkce
     * @param position
     */
    public Position(Position position){
        this.row = position.row;
        this.col = position.col;
    }

    /**
     * Rovnou vytvori po konverzi z upper case retezce novou instanci pozice
     * @param positionStr retezec pozice v upper case, napr.: A1
     * @return instance Pozice
     */
    public static Position positionStrToPosition(String positionStr)
    {
        //kontrola delky
        if (positionStr.length()!=2) return null;

        char c_col= positionStr.charAt(0);
        //kontrola spravnosti sloupce
        if ( (c_col<'A') || (c_col>'H') ) return null;

        char c_row= positionStr.charAt(1);
        //kontrola spravnosti radku
        if ( (c_row<'1') || (c_row>'8') ) return null;

        //konverze charu na int
        int i_col= (int)c_col - 'A';
        int i_row= (int)c_row - '1';

        //vytvoreni nove pozice a jeji navrat
        Position pos= new Position(i_row, i_col);
        return pos;
    }

    /**
     * Rovnou vytvori po konverzi z lower case retezce novou instanci pozice
     * @param positionStr retezec pozice v lower case, napr.: a1
     * @return instance Pozice
     */
    public static Position positionStrToPositionLowerCase(String positionStr)
    {
        //kontrola delky
        if (positionStr.length()!=2) return null;

        char c_col= positionStr.charAt(0);
        //kontrola spravnosti sloupce
        if ( (c_col<'a') || (c_col>'h') ) return null;

        char c_row= positionStr.charAt(1);
        //kontrola spravnosti radku
        if ( (c_row<'1') || (c_row>'8') ) return null;

        //konverze charu na int
        int i_col= (int)c_col - 'a';
        int i_row= (int)c_row - '1';

        //vytvoreni nove pozice a jeji navrat
        Position pos= new Position(i_row, i_col);
        return pos;
    }


    /**
     * Vrati upper case retezec odpovidajici pozici na sachovnici
     * @return String souradnice v upper case, napr.: A1
     */
    public String getStr()
    {
        char pom1= (char) ( this.col + 'A' );
        char pom2= (char) ( this.row + '1' );
        String pom3= pom1 + "" + pom2;
        return pom3;
    }

    /**
     * Vrati lower case retezec odpovidajici pozici na sachovnici
     * @return String souradnice v lower case, napr.: a1
     */
    public String getStrLowerCase()
    {
        char pom1= (char) ( this.col + 'a' );
        char pom2= (char) ( this.row + '1' );
        String pom3= pom1 + "" + pom2;
        return pom3;
    }

    @Override
    public boolean equals(Object p){

        if(p == this){return true;} //stejne reference

        if(p == null || p.getClass() != this.getClass()){
            return false;
        }

        Position pos = (Position)p;

        if ( (this.row==pos.row) && (this.col==pos.col) ) return true;
        else return false;
    }

}
