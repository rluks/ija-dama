package dama;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import gui.InternalFrameDemo;

import dama.io.Turn;
import dama.operations.AI;
import dama.operations.Check;
import dama.operations.Moves;
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
    private static int clickCount = 0; //po dvou klicich posilame tah hre
    protected static Turn turnInput;

    //vlakno hry
    public Settings settings;
    public Turn turn;
    public Desk desk;
    public char playerColor;
    public boolean allDone = false; //ukonceni behu
    public boolean pleaseWait = false; //pozastaveni behu

    public boolean gameSetAndRuning = false; //hra byla nastavena a uz se provadi tahy, atd.
    InternalFrameDemo gui;

    /**
     * Konstruktor vlakna hry
     * @param name
     */
    Dama(String name){
        super(name);

        gui = new InternalFrameDemo();
        gui.setVisible(true);
        gui.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Okno zavreno, ukoncuji...");
                Dama.wakeUpGameThread();
                Dama.stopGameThread();
                System.exit(0);
            }
        } );

    }

    public void run(){

        //inicializace

        System.out.println("Hra: Uspavam se");
        Dama.sleepGameThread();

        //smycka nastaveni hry
        while(true){

            try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}

            synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }

            //vypinaci cast
            checkGUIShutdown(gui.isVisible());
            if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

            if(this.settings == null){
                continue;
            }

            this.desk = new Desk();
            gui.updateDesk(desk);

            System.out.println("Hra: Tisknu nastaveni");
            System.out.println(this.settings.getXML());


            //smycka vlastni hry
            while(true){
                gameSetAndRuning = true;
                System.out.println("Hra: nastavena, beziii hra.... bezii uz beziiii");
                System.out.println(this.settings.getSettingsString());

                /* White */
                this.playerColor = 'W';
                try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println("Hra: its white players turn!");
                    Position pEmpty = new Position(-1, -1);
                    this.turn = Turn.TurnWhite(pEmpty, pEmpty);

                    Dama.sleepGameThread();

                    try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }
                    System.out.println("Hra: white is playing...");

                    //zpracovani tahu bileho
                    if(Check.isMustTake(desk, 'W')){
                        Moves.Take(desk, turn);
                        desk.clearMustTakeFlag();
                    }else{
                        Moves.Move(desk, turn);
                    }

                    Check.isPieceToDame(desk);
                    gui.updateDesk(this.desk);

                /* Black */
                this.playerColor = 'B';
                try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println("Hra: its BLACK players turn!");
                    this.turn = Turn.TurnBlack(pEmpty, pEmpty);

                    Dama.sleepGameThread();

                    try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }
                    System.out.println("Hra: black is playing...");

                    //zpracovani tahu cerneho
                    if(Check.isMustTake(desk, 'B')){
                        Moves.Take(desk, turn);
                        desk.clearMustTakeFlag();
                    }else{
                        Moves.Move(desk, turn);
                    }

                    Check.isPieceToDame(desk);
                    gui.updateDesk(this.desk);

                //vypinaci cast
                checkGUIShutdown(gui.isVisible());
                if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            }
        }
    }


    /**
     * Kontrola jestli se GUI zavrelo
     * @param isGUIvisible gui.isVisible;
     */
    private void checkGUIShutdown(boolean isGUIvisible){

        if(!isGUIvisible){
            System.out.println("Hra: GUI zavreno - vypinam program.");
            System.exit(0);
        }
    }


    //staticke metody
    public static void main(String[] args) throws InterruptedException {

        // projekt:
        // TODO javadoc

        Dama.startingGameThread();
/*
        InternalFrameDemo gui;
        gui = new InternalFrameDemo();
        gui.setVisible(true);
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
*//*
        Desk d = new Desk();
        //System.out.println("Velka sachovnice:");
        //d.printBoard();
        //System.out.println("Mala sachovnice:");
        //d.printSimpleBoard();

        //int i=0;
        int j=0;
        while(true)
        {
            gui.updateDesk(d);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            boolean pom;
            if ((j%2)==0) pom=AI.AIMove(d, 'W', 6);
            else pom=AI.AIMove(d, 'B', 6);
            if (pom==false)
            {
                System.out.println("Doslo k patu. To nedas typYco!!!!!");
                break;
            }

            Check.isPieceToDame(d);
            d.clearMustTakeFlag();
            j++;
            if (Check.isVictory(d))
            {
                System.out.println("VYHRA TYPYCO!!!!");
                break;
            }
        }*/

        /*
        System.out.println("Validace:\ta1-a2 - " + Check.isNotationValid("a1-a2"));
        System.out.println("Validace:\ta5xa2 - " + Check.isNotationValid("a5xa2"));
        System.out.println("Validace:\td1-a1  - " + Check.isNotationValid("d1-a0 "));
        System.out.println("Validace:\th1-a2 - " + Check.isNotationValid("h1-a2"));
        System.out.println("Validace:\tg1-a9 - " + Check.isNotationValid("g1-a9"));
        System.out.println("Validace:\ts1-a2 - " + Check.isNotationValid("s1-a2"));
        System.out.println("Validace:\tk1-a2 - " + Check.isNotationValid("k1-a2"));
        */

    }


    /**
     * Zastavi predchozi vlakno, spusti nove, uspi ho a ceka se na nastaveni
     */
    public static void startingGameThread(){

        Dama.stopGameThread();

        System.out.println("startingGameThread...");
        dama = new Dama("DamaVlakno");
        dama.start();

        //Dama.sleepGameThread();
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

        if(Dama.isGameSetAndRunning()){
            Dama.clickCount++;
            {
                System.out.println("mouseInput:" + pos.getStr());
            }

            if(Dama.clickCount == 1){
                Dama.turnInput = Dama.getTurn();

                turnInput.origin = pos;
            }

            if(Dama.clickCount == 2){
                turnInput.destination = pos;

                Dama.input(turnInput);

                Dama.clickCount = 0;
            }
        }
    }

    /**
     * Zpracuje vstup od textove ramecku od notace a posila ho dal
     */
    public static void notaceInput(Turn turn){

        ///zpracovani


        Dama.input(turn);
    }

    /**
     * Jednotny vstup tahu pro vlakno hry
     */
    public static void input(Turn turn){

        if(Moves.isTurnValid(turn, Dama.getDesk())){
            Dama.setTurn(turn);
            Dama.wakeUpGameThread();
        }else{
            System.out.println("Dama.input: nevalidni tah, neposilam ho dal.");
        }
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

    public static boolean setTurn(Turn turn){

        System.out.println("setTurn: ukladam tah...");

        if(dama != null){
            synchronized (dama){
                dama.turn = turn;
                dama.notify();
            }
            return true;
        }else{
            System.out.println("setTurn: hra neexistuje!");
            return false;
        }

    }

    public static Desk getDesk(){
        synchronized (dama){
            return dama.desk;
        }
    }

    public static Turn getTurn(){
        synchronized (dama){
            return dama.turn;
        }
    }


    public static char getPlayerColor(){
        synchronized (dama){
            return dama.playerColor;
        }
    }


}
