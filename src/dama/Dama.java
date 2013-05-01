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
    protected static int clickCount = 0; //po dvou klicich posilame tah hre
    protected static Turn turnInput;

    //vlakno hry
    public Settings settings;
    public Turn turn;
    public Desk desk;
    public char playerColor;
    protected int tahCislo = 0;
    protected boolean wtake;
    protected boolean btake;
    private int aiStochasticity = 5; //nahodnost AI tahu
    public boolean allDone = false; //ukonceni behu
    public boolean pleaseWait = false; //pozastaveni behu
    //public int pocaitadlo_tahu=1;

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
        while(true)
        {

            try {Thread.sleep(200);} catch (InterruptedException e) {e.printStackTrace();}

            synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }

            //vypinaci cast
            checkGUIShutdown(gui);
            if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

            if(this.settings == null){
                continue;
            }

            this.desk = new Desk();
            gui.updateDesk(desk);
            this.desk.printSimpleBoard();

            System.out.println("Hra: Tisknu nastaveni");
            //System.out.println(this.settings.getXML());


            //smycka vlastni hry
            while(true){
                gameSetAndRuning = true;
                System.out.println("Hra: nastavena, beziii hra.... bezii uz beziiii");
                Position pEmpty = new Position(-1, -1);

                System.out.println(this.settings.getSettingsString());
                tahCislo++;

                /* White */
                this.playerColor = 'W';
                try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println("Hra: its white players turn!");

                    String infoText = (this.settings.white == "AI")? "It's WHITE AIs turn!" : "It's WHITE players turn!";
                    gui.setInfoText(infoText);

                    if(this.settings.white == "AI"){
                        System.out.println("Hra: AI - tah bileho");
                        turn = AI.AIMove(desk, this.playerColor, this.aiStochasticity);
                        desk.clearMustTakeFlag();
                    }else{

                        this.turn = Turn.TurnWhite(pEmpty, pEmpty);

                        Dama.sleepGameThread();

                        try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                        synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }

                        System.out.println("Hra: white is playing...");

                        //vypinaci cast
                        checkGUIShutdown(gui);
                        if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

                        //zpracovani tahu bileho
                        if(Check.isMustTake(desk, 'W')){
                        	System.out.println("================================BRANI=============================");
                        	System.out.println("Hra: tah bilyho, take() vraci:" + Moves.Take(desk, turn));                            
                            desk.clearMustTakeFlag();
                            wtake = true;
                        }else{
                            System.out.println("Hra: tah bilyho, MOVE() vraci:" + Moves.Move(desk, turn));
                            wtake = false;
                        }
                    }

                    Check.isPieceToDame(desk);
                    gui.updateDesk(this.desk);
                    this.desk.printSimpleBoard();
                    gui.clearAura();

                    Turn wturn = turn;

                /* Black */
                this.playerColor = 'B';
                try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    System.out.println("Hra: its BLACK players turn!");
                    gui.setInfoText("It's BLACK players turn!");
                    this.turn = Turn.TurnBlack(pEmpty, pEmpty);

                    Dama.sleepGameThread();

                    try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                    synchronized(this){ while(pleaseWait){ try { System.out.println("Hra: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }
                    System.out.println("Hra: black is playing...");

                    //vypinaci cast
                    checkGUIShutdown(gui);
                    if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

                    //zpracovani tahu cerneho
                    if(Check.isMustTake(desk, 'B')){
                    	System.out.println("================================BRANI=============================");
                    	System.out.println("Hra: tah CERNYHO, take() vraci:" + Moves.Take(desk, turn));                        
                        desk.clearMustTakeFlag();
                        btake = true;
                    }else{
                        System.out.println("Hra: tah CERNYHO, MOVE() vraci:" +  Moves.Move(desk, turn));
                        btake = false;
                    }

                    Check.isPieceToDame(desk);
                    gui.updateDesk(this.desk);
                    this.desk.printSimpleBoard();
                    gui.clearAura();

                    Turn bturn = turn;

                gui.NotationNewLine(wturn, wtake, bturn, btake, tahCislo);

                //vypinaci cast
                checkGUIShutdown(gui);
                if(allDone){System.out.println("Hra: Dama terminated, allDone!!");return;}

                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}


            }
        }
    }


    /**
     * Kontrola jestli se GUI zavrelo
     * @param isGUIvisible gui.isVisible;
     */
    private void checkGUIShutdown(InternalFrameDemo gui){

        if(!gui.isVisible()){
            System.out.println("Hra: GUI zavreno - vypinam program.");
            this.allDone = true;
            gui.pullThePlug();
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
            System.out.println("mouseInput:" + pos.getStr());

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

    public static void setClickCount(int count){
        Dama.clickCount = count;
        System.out.println("setClickCount:" + count);
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
     * @param settings instance tridy Settings s nastavenim hry
     * @return vraci false pokud hra neexistuje
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

    /**
     * Uklada tah do vlakna hry
     * @param turn instance tridy
     * @return vraci false pokud hra neexistuje
     */
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

    /**
     * Z vlakna hry ziska desku
     * @return desk instance tridy Desk obsahujici stav hry na sachovnici
     */
    public static Desk getDesk(){
        synchronized (dama){
            return dama.desk;
        }
    }

    /**
     * Z vlakna hry ziska turn
     * @return turn instance tridy Turn obsahujici jeden pultah
     */
    public static Turn getTurn(){
        synchronized (dama){
            return dama.turn;
        }
    }


    /**
     * Z vlakna hry ziska barvu hrace na tahu
     * @return char barva hrace 'W' nebo 'B', pripadne '\0'
     */
    public static char getPlayerColor(){
        synchronized (dama){
            return dama.playerColor;
        }
    }


}
