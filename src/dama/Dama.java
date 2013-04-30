package dama;

import dama.state.Desk;
import components.InternalFrameDemo;
import dama.operations.*;
import dama.state.Settings;

/**
 * Hra Dáma v Javě
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Dama extends Thread{

    //staticke
    public static Dama dama;

    //vlakno
    public Settings settings;
    public boolean allDone = false;

    Dama(String name, Settings s){
        super(name);

        this.settings = s;
    }

    public void run(){
        while(true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Running da run()");
            System.out.println(this.settings.getSettingsString());

            if(allDone){
                System.out.println("allDone!!");
                return;
            }
        }
    }


    //staticke
    public static void main(String[] args) throws InterruptedException {

        // projekt:
        // TODO javadoc

        /*
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                InternalFrameDemo.createAndShowGUI();
            }
        });
        */
        Desk d = new Desk();
        //System.out.println("Velka sachovnice:");
        //d.printBoard();
        System.out.println("Mala sachovnice:");
        d.printSimpleBoard();

        int i=0;
        int j=0;
        while(!Check.isVictory(d))
        {
            boolean pom;
            if (i==0) pom=AI.AIMove(d, 'W');
            else pom=AI.AIMove(d, 'B');

            System.out.println(j + ": Stav booleanu: " + pom + "; i = " + i);

            /*
            if (pom==false)
            {
                System.out.println("Neco se posralo!!!!!");
                break;
            }
            */
            Check.isPieceToDame(d);
            d.clearMustTakeFlag();

            d.printSimpleBoard();

            i++;
            i=i%2;

            j++;
            //if (j==800) break;
        }
    }


    public static void startingGameThread(Settings settings){

        System.out.println("startuju");

        //ukonceni predchozi hry
        if(dama != null){
            if(dama.isAlive()){
                dama.allDone = true;
            }
            while(dama.isAlive()){}
        }

        dama = new Dama("DamaVlakno", settings);
        dama.start();
    }


}
