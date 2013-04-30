package dama;

import java.awt.event.MouseEvent;

import dama.state.Desk;
import dama.state.Settings;
import components.InternalFrameDemo;;

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
    public boolean allDone = false; //ukonceni behu
    public boolean pleaseWait = false; //pozastaveni behu

    Dama(String name/*, Settings s*/){
        super(name);

        //this.settings = s;
    }

    public void run(){

        //inicializace
        InternalFrameDemo gui;
        gui = new InternalFrameDemo();
        gui.setVisible(true);
        Dama.sleepGameThread();

        //smycka hry
        while(true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            if(allDone){System.out.println("Dama terminated, allDone!!");return;}
            if(!gui.isVisible()){Dama.stopGameThread();System.exit(0);}

            synchronized(this){
                while(pleaseWait){
                    try{
                        System.out.println("waiting...");
                        wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Running da run()");
            gui.setLabelHelp("Baník pYčo");
            System.out.println(this.settings.getSettingsString());
            System.out.println(this.settings.getXML());
        }
    }


    //staticke
    public static void main(String[] args) throws InterruptedException {

        // projekt:
        // TODO javadoc

        Dama.startingGameThread();

/*
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //InternalFrameDemo.createAndShowGUI();
                InternalFrameDemo gui;
                gui = new InternalFrameDemo();
                gui.setVisible(true);
                gui.setLabelHelp("Baník pYčo");
            }
        });
*/
        Desk d = new Desk();
        //System.out.println("Velka sachovnice:");
        //d.printBoard();
        System.out.println("Mala sachovnice:");
        d.printSimpleBoard();
    }

    public static void startingGameThread(/*Settings settings*/){

        Dama.stopGameThread();

        System.out.println("startuju");
        dama = new Dama("DamaVlakno"/*, settings*/);
        dama.start();

        //Dama.sleepGameThread();
    }

    public static boolean sleepGameThread(){
        System.out.println("uspavam");

        if(dama != null){
            synchronized (dama){
                dama.pleaseWait = true;
            }
            return true;
        }else{
            System.out.println("hra neexistuje");
            return false;
        }
    }

    public static boolean wakeUpGameThread(){
        System.out.println("probouzim");

        if(dama != null){
            synchronized (dama){
                dama.pleaseWait = false;
                dama.notify();
            }
            return true;
        }else{
            System.out.println("hra neexistuje");
            return false;
        }
    }

    public static void stopGameThread(){
        System.out.println("zastavuji vlakno hry");

        if(dama != null){
            if(dama.isAlive()){
                dama.allDone = true;
            }
            while(dama.isAlive()){}
        }else{
            System.out.println("hra neexistuje");
        }
    }

    public static void mouseInput(MouseEvent e){
        if(wakeUpGameThread()){
            System.out.println("myska me vzbudila");
        }
    }

    public static boolean setSettings(Settings s){
        System.out.println("ukladam nastaveni");

        if(dama != null){
            synchronized (dama){
                dama.settings = s;
                dama.notify();
            }
            return true;
        }else{
            System.out.println("hra neexistuje");
            return false;
        }
    }


}
