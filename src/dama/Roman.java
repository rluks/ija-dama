package dama;

import dama.state.Settings;

/**
 * Romanova main na prototypovani
 * @author Roman Lukš
 *
 */
public class Roman {

    public static void main(String[] args) {

        //TODO presun obrazku do slozky lib

        System.out.println("ROMAN");
        Dama.startingGameThread();


        if(Dama.XmlToVector("C:\\eclipseworkspace\\ija-dama-2013\\turnvector.xml")){
            System.out.println("Turns nastaveny!");
        }else{
            System.out.println("Turns FAIL!");
            return;
        }

        String whiteStr = "Human";
        String blackStr = "Human";
        int aiWait = 300;
        Settings settings = new Settings(whiteStr, blackStr, null, null, 0, aiWait);

        //Dama.setLoadedTurns(lt);
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
