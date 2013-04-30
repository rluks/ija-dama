package dama;

import components.InternalFrameDemo;

import dama.operations.AI;
import dama.operations.Check;
import dama.state.Desk;
import dama.state.Position;
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

    //vlakno hry
    public Settings settings;
    public Desk desk;
    public boolean allDone = false; //ukonceni behu
    public boolean pleaseWait = false; //pozastaveni behu

    public boolean gameSetAndRuning = false; //hra byla nastavena a uz se provadi tahy, atd.

    /**
     * Konstruktor vlakna hry
     * @param name
     */
    Dama(String name){
        super(name);
    }

    public void run(){

        //inicializace
        InternalFrameDemo gui;
        gui = new InternalFrameDemo();
        gui.setVisible(true);
        Dama.sleepGameThread();

        //smycka nastaveni hry
        while(true){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            synchronized(this){
                while(pleaseWait){
                    try{
                        System.out.println("sleeping...");//TODO kdyz se tu zavre gui, tak tohle furt spi
                        wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //vypinaci cast
            if(allDone){System.out.println("Dama terminated, allDone!!");return;}
            if(!gui.isVisible()){
                System.out.println("GUI zavreno - vypinam program.");
                System.exit(0);
            }//vypinaci cast - konec

            this.desk = new Desk();
            gui.updateDesk(desk);

            System.out.println(this.settings.getXML());


            //smycka vlastni hry
            while(true){
                gameSetAndRuning = true;
                System.out.println("Hra nastavena, beziii hra.... bezii uz beziiii");
                System.out.println(this.settings.getSettingsString());

                gui.updateDesk(this.desk);

                //vypinaci cast
                if(allDone){System.out.println("Dama terminated, allDone!!");return;}
                if(!gui.isVisible()){
                    System.out.println("GUI zavreno - vypinam program.");
                    System.exit(0);
                }//vypinaci cast - konec

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


    //staticke metody
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

        //int i=0;
        int j=0;
        while(true)
        {
            System.out.println("====================================================================");
            boolean pom;
            if ((j%2)==0) pom=AI.AIMove(d, 'W', 6);
            else pom=AI.AIMove(d, 'B', 6);

            System.out.println(j + ": Stav booleanu: " + pom);

            if (pom==false)
            {
                System.out.println("Doslo k patu. To nedas typYco!!!!!");
                break;
            }

            Check.isPieceToDame(d);
            d.clearMustTakeFlag();
            d.printSimpleBoard();

            j++;
            //if (j==800) break;

            if (Check.isVictory(d))
            {
                System.out.println("VYHRA TYPYCO!!!!");
                break;
            }
        }
    }


    /**
     * Zastavi predchozi vlakno, spusti nove, uspi ho a ceka se na nastaveni
     */
    public static void startingGameThread(){

        Dama.stopGameThread();

        System.out.println("startingGameThread...");
        dama = new Dama("DamaVlakno");
        dama.start();

        Dama.sleepGameThread();
    }

    /**
     * Uspi vlakno hry
     */
    public static boolean sleepGameThread(){
        System.out.println("sleepGameThread: pokousim se uspat hru...");

        if(dama != null){//TODO tyhle kontroly delat v jedine fci
            synchronized (dama){
                dama.pleaseWait = true;
            }
            return true;
        }else{
            System.out.println("sleepGameThread: hra neexistuje!");
            return false;
        }
    }

    /**
     * Probudi vlakno hry
     */
    public static boolean wakeUpGameThread(){
        System.out.println("wakeUpGameThread: probouzim hru...");

        if(dama != null){
            synchronized (dama){
                dama.pleaseWait = false;
                dama.notify();
            }
            return true;
        }else{
            System.out.println("wakeUpGameThread: hra neexistuje!");
            return false;
        }
    }

    /**
     * Zastavi vlakno hry
     * @return
     */
    public static boolean stopGameThread(){
        System.out.println("stopGameThread: zastavuji vlakno hry...");

        if(dama != null){
            if(dama.isAlive()){
                dama.allDone = true;
            }
            while(dama.isAlive()){}
            return true;
        }else{
            System.out.println("stopGameThread: hra neexistuje!");
            return false;
        }
    }

    /**
     * Kontrola jestli bezi vlakno hry
     * @return
     */
    public static boolean isGameThreadRunning(){
        if(dama != null){
            if(dama.isAlive()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Kontrola jestli uz se hraje (zda bylo vse nastaveno)
     * @return
     */
    public static boolean isGameSetAndRunning(){

        if(Dama.isGameThreadRunning()){
            synchronized (dama){
                if(dama.gameSetAndRuning){
                    return true;
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }

    /**
     * Zpracuje vstup od mysi (kliky na sachovnici) a posila ho dal
     * @param pos
     */
    public static void mouseInput(Position pos){

        //if(wakeUpGameThread())
        {
            System.out.println("mouseInput:" + pos.getStr());
        }
        Dama.input(/**/);
    }

    /**
     * Zpracuje vstup od textove ramecku od notace a posila ho dal
     */
    public static void notaceInput(){

        ///zpracovani


        Dama.input(/**/);
    }

    /**
     * Jednotny vstup tahu pro vlakno hry
     */
    public static void input(){

    }

    /**
     * Uklada nastaveni do vlakna hry
     * @param s
     * @return
     */
    public static boolean setSettings(Settings settings){
        System.out.println("setSettings: ukladam nastaveni...");

        if(dama != null){
            synchronized (dama){
                dama.settings = settings;
                dama.notify();
            }
            return true;
        }else{
            System.out.println("setSettings: hra neexistuje!");
            return false;
        }
    }


}
