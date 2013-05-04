package dama;

import java.util.Vector;

import dama.io.Turn;
import dama.state.Position;
import dama.state.Settings;

/**
 * Romanova main na prototypovani
 * @author Roman Lukš
 *
 */
public class Roman {

    public static void main(String[] args) {

        //TODO presun obrazku do slozky lib
    	//TODO prejmenovat internalframedemo na gui
        System.out.println("ROMAN");
        
        Dama.startingGameThread();
    	
    	Vector<Turn> lt = new Vector<Turn>();
    	Position o = Position.positionStrToPositionLowerCase("c3");
    	Position d = Position.positionStrToPositionLowerCase("b4");
    	Turn t = new Turn('W', o, d);
    	System.out.println(t.getTurnString());
    	lt.add(t);

    	o = Position.positionStrToPositionLowerCase("b6");
    	d = Position.positionStrToPositionLowerCase("c5");
    	t = new Turn('B', o, d);
    	System.out.println(t.getTurnString());
    	lt.add(t);

    	o = Position.positionStrToPositionLowerCase("b4");
    	d = Position.positionStrToPositionLowerCase("a5");
    	t = new Turn('W', o, d);
    	System.out.println(t.getTurnString());
    	lt.add(t);
    	
    	o = Position.positionStrToPositionLowerCase("e4");
    	d = Position.positionStrToPositionLowerCase("e4");
    	t = new Turn('B', o, d);
    	System.out.println(t.getTurnString());
    	lt.add(t);

        String whiteStr = "Human";
        String blackStr = "Human";
        int aiWait = 300;
        Settings settings = new Settings(whiteStr, blackStr, null, null, 0, aiWait);

        Dama.setLoadedTurns(lt);
        Dama.setSettings(settings);

        try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
        Dama.wakeUpGameThread();

    }
}

/*
Vector<Turn> lt = new Vector<Turn>();
Position o = Position.positionStrToPositionLowerCase("c3");
Position d = Position.positionStrToPositionLowerCase("b4");
Turn t = new Turn('W', o, d);
System.out.println(t.getTurnString());
lt.add(t);

o = Position.positionStrToPositionLowerCase("b6");
d = Position.positionStrToPositionLowerCase("c5");
t = new Turn('B', o, d);
System.out.println(t.getTurnString());
lt.add(t);

o = Position.positionStrToPositionLowerCase("b4");
d = Position.positionStrToPositionLowerCase("a5");
t = new Turn('W', o, d);
System.out.println(t.getTurnString());
lt.add(t);

*/
