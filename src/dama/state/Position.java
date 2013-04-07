package dama.state;

public class Position {
    public int row;
    public int col;

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    //static metoda tridy rovnou vytvori po konverzi z retezce novou instanci pozice---------------------------------------------------------------------------------------
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

    //pozice bude umet vracet svuj retezec----------------------------------------------------------------------------------------------------------------------------------
    public String getStr()
    {
        char pom1= (char) ( this.col + 'A' );
        char pom2= (char) ( this.row + '1' );
        String pom3= pom1 + "" + pom2;
        return pom3;
    }

    public boolean equals(Position pos)
    {
        if (this==pos) return true;    //stejne reference
        else if ( (this.row==pos.row) && (this.col==pos.col) ) return true;
        else return false;
    }

}
