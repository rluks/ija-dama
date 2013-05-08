package dama;

import gui.PlayerGui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import dama.io.Output;
import dama.io.Turn;
import dama.operations.Check;
import dama.operations.Moves;
import dama.state.Desk;
import dama.state.Settings;

/**
 * Prehravac ulozenych her
 * @author Roman Luks
 * @author Pavel Hala
 *
 */
public class Player extends Thread{

    //staticke
    protected static Turn turnInput;
    protected static int playerCount = 0;

    /**
     * Z vlakna prehravace ziska desku
     * @param thread reference na dane vlakno prehravace
     * @return desk instance tridy Desk obsahujici stav na sachovnici
     */
    public static Desk getDesk(Player thread){
        synchronized (thread){
            return thread.desk;
        }
    }
    /**
     * Ziska hodnotu index (do Vektoru tahu) z vlakna prehravace
     * @param thread reference na dane vlakno prehravace
     * @return integer hodnotu
     */
    public static int getIndex(Player thread){
        synchronized (thread){
            return thread.index;
        }
    }

    /**
     * Z vlakna prehravace ziska velikost vektoru nactenych tahu
     * @param thread reference na dane vlakno prehravace
     */
    public static int getLoadedTurnsSize(Player thread){
        synchronized (thread){
            return thread.loadedTurns.size();
        }
    }
    /**
     * Z vlakna prehravace ziska vektor tahu
     * @param thread reference na dane vlakno prehravace
     * @return vektor tahu
     */
    public static Vector<Turn> getOutputTurns(Player thread){
        synchronized (thread){
            return thread.ouputTurns;
        }
    }

    /**
     * Z vlakna prehravace ziska barvu hrace na tahu
     * @param thread reference na dane vlakno prehravace
     * @return char barva hrace 'W' nebo 'B', pripadne '\0'
     */
    public static char getPlayerColor(Player thread){
        synchronized (thread){
            return thread.playerColor;
        }
    }

    /**
     * Z vlakna prehravace ziska settings
     * @param thread reference na dane vlakno prehravace
     * @return settings instance Settings
     */
    public static Settings getSettings(Player thread){
        synchronized (thread){
            return thread.settings;
        }
    }


    /**
     * Ziska hodnotu stepIndex(index pri krokovani) z vlakna prehravace
     * @param thread reference na dane vlakno prehravace
     * @return integer hodnotu
     */
    public static int getStepIndex(Player thread){
        synchronized (thread){
            return thread.stepIndex;
        }
    }


    /**
     * Z vlakna prehravace ziska turn
     * @param thread reference na dane vlakno prehravace
     * @return turn instance tridy Turn obsahujici jeden pultah
     */
    public static Turn getTurn(Player thread){
        synchronized (thread){
            return thread.turn;
        }
    }


    /**
     * Ziskani hodnoty vstupniho Turn ze staticke promenne turnInput
     * @return statickou promennou turnInput
     */
    public static Turn getTurnInput() {
        return turnInput;//staticka!
    }


    /**
    * Jednotny vstup tahu pro vlakno prehravace
    * @param thread reference na dane vlakno prehravace
    * @param turn jeden pultah
    */
    public static void inputTurn(Player thread, Turn turn)
    {

        if(Moves.isTurnValid(turn, Player.getDesk(thread)))
        {
            System.out.println("Player.inputTurn: vkladam tah");
            Player.setTurn(thread, turn);
        }
        else
        {
            System.out.println("Player.inputTurn: nevalidni tah, prehravac MUSI nastavit napovedu");
            Player.setTurn(thread, turn);
        }
    }


    /**
     * Probudi vlakna prehravace ze spanku (Sleep)
     * @param thread reference na dane vlakno prehravace
     * @return false pokud vlakno neexistuje
     */
    public static boolean interruptPlayerThread(Player thread){

        if(thread != null){
            if(thread.isAlive()){
                thread.interrupt();
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    /**
     * Kontrola jestli uz se hraje (zda bylo vse nastaveno a probiha smycka prehravace)
     * @param thread reference na dane vlakno prehravace
     */
    public static boolean isGameSetAndRunning(Player thread){

        if(Player.isGameThreadRunning(thread)){
            synchronized (thread){
                if(thread.gameSetAndRuning){
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
     * Kontrola jestli bezi vlakno prehravace
     * @param thread reference na dane vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean isGameThreadRunning(Player thread){
        if(thread != null){
            if(thread.isAlive()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Nastavi do vlakna prehravace posun v tazich
     * @param thread reference na dane vlakno prehravace
     * @param indexdelta posun stepIndexu
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setIndexChange(Player thread, int indexdelta){

        int stepIndex = Player.getStepIndex(thread);

        if(thread != null){
            synchronized (thread){
                thread.stepIndex = stepIndex+indexdelta;
                System.out.println("Player: setIndexChange: zmena " + stepIndex + " o " + indexdelta + " na " + Integer.toString(stepIndex+indexdelta));
            }
            return true;
        }else{
            System.out.println("Player: setIndexChange: hra neexistuje!");
            return false;
        }
    }

    /**
     * Do vlakna prehravace nastavi nactene tahy
     * @param thread reference na dane vlakno prehravace
     * @param loadedTurns vektor nactenych tahu
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setLoadedTurns(Player thread, Vector<Turn> loadedTurns){

        System.out.println("Player: setTurn: ukladam nactene tahy do vlakna prehravace...");

        if(thread != null){
            synchronized (thread){
                thread.loadedTurns = loadedTurns;
                thread.notify();
            }
            return true;
        }else{
            System.out.println("Player: setLoadedTurns: hra neexistuje!");
            return false;
        }
    }

    /**
     * Uklada nastaveni do vlakna prehravace
     * @param thread reference na dane vlakno prehravace
     * @param settings instance tridy Settings s nastavenim prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setSettings(Player thread, Settings settings){
        System.out.println("Player: setSettings: ukladam nastaveni...");

        if(thread != null){
            synchronized (thread){
                thread.settings = settings;
                thread.notify();
            }
            return true;
        }else{
            System.out.println("Player: setSettings: hra neexistuje!");
            return false;
        }
    }

    /**
     * Uklada tah do vlakna prehravace
     * @param thread reference na dane vlakno prehravace
     * @param turn instance tridy
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setTurn(Player thread, Turn turn){

        System.out.println("Player: setTurn: ukladam tah...");

        if(thread != null){
            synchronized (thread){
                thread.turn = turn;
                thread.notify();
            }
            return true;
        }else{
            System.out.println("Player: setTurn: hra neexistuje!");
            return false;
        }

    }

    /**
     * Nastavi do staticke promenne turnInput hodnotu turn
     * @param thread reference na dane vlakno prehravace
     * @param turn
     */
    public static void setTurnInput(Player thread, Turn turn) {
        Player.turnInput = turn;
    }


    /**
     * Nastavi do vlakna prehravace dobu cekani pri prehravani
     * @param thread reference na dane vlakno prehravace
     * @param waittime
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setWaitTime(Player thread, int waittime){

        System.out.println("Player: setWaitTime: ukladam dobu cekani " + (double)waittime/1000.0 + "s ...");

        if(thread != null){
            synchronized (thread){
                thread.waitTime = waittime;
                thread.notify();
            }
            return true;
        }else{
            System.out.println("Player: setWaitTime: hra neexistuje!");
            return false;
        }
    }


    /**
     * Uspi vlakno prehravace
     * @param thread reference na dane vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean sleepPlayerThread(Player thread){
        System.out.println("Player: sleepGameThread: pokousim se uspat hru...");

        if(thread != null){
            synchronized (thread){
                thread.pleaseWait = true;
            }
            return true;
        }else{
            System.out.println("Player: sleepGameThread: hra neexistuje!");
            return false;
        }
    }


    /**
     * Spusti nove vlakno prehravace
     */
    public static void startingPlayerThread(){

        System.out.println("Player: startingPlayerThread...");

        Player thread = new Player("" + Player.playerCount);
        thread.start();
        System.out.println(thread.getName());

        Player.playerCount++;
    }


    /**
     * Do vlakna prehravace zapise ze bylo ulozeno settings a pripadne loadedTurns
     * a muze se tim padem zacit hrat
     * @param thread reference na dane vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean startPlayer(Player thread){
        System.out.println("Player: startGame: povoluji beh smycky prehravace...");

        if(thread != null){
            if(thread.isAlive()){
                thread.startGame = true;
            }
            return true;
        }else{
            System.out.println("Player: startGame: hra neexistuje!");
            return false;
        }
    }

    /**
     * Zastavi vlakno prehravace
     * @param player reference na dane vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean stopThread(Player player){
        System.out.println("Player: stopGameThread: zastavuji vlakno prehravace...");

        if(player != null){
            if(player.isAlive()){
                player.allDone = true;
            }
            return true;
        }else{
            System.out.println("Player: stopGameThread: hra neexistuje!");
            return false;
        }
    }

    /**
     * Probudi vlakno prehravace
     * @param thread reference na dane vlakno prehravace
    * @return vraci false pokud prehravac neexistuje
     */
    public static boolean wakeUpPlayerThread(Player player){
        System.out.println("Player: wakeUpPlayerThread: probouzim prehravac...");

        if(player != null){
            synchronized (player){
                player.pleaseWait = false;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: wakeUpPlayerThread: prehravac neexistuje!");
            return false;
        }
    }


    //vlakno prehravace
    public Settings settings;
    protected String currentPlayerAIHumanSettings;
    protected Vector<Turn> ouputTurns;        //sem hra uklada provedene tahy - pouzije se v pripade ulozeni prehravace
    protected Vector<Turn> loadedTurns;        //zde jsou ulozeny tahy v pripade nacteni prehravace
    protected Turn turn;
    protected Desk desk;
    protected char playerColor;

    protected int tahCislo = 0;
    protected int waitTime = 1000;
    protected int index = -1;//index pro pohyb ve vektoru tahu

    protected int stepIndex; //posune se v tazich o pocet kliknuti na tlacitka forward, back
    protected boolean wtake;
    protected boolean btake;
    protected boolean wbtake;
    protected Turn wturn;

    protected Turn bturn;
    protected boolean startGame = false;    //po vlozeni settings, a pripadne loadedTurns tohle povolit
    protected boolean allDone = false;         //ukonceni behu
    protected boolean pleaseWait = false;     //pozastaveni behu
    protected boolean gameSetAndRuning = false; //hra byla nastavena a uz se provadi tahy, atd.
    protected PlayerGui gui;
    protected int blockMeAtIndex = -1;

    private boolean fastforward = false;


    /**
     * Konstruktor vlakna prehravace
     * @param name
     */
    Player(String name){
        super(name);

        this.getId();
        gui = new PlayerGui(this);
        gui.setVisible(true);
        gui.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("PlayerGui: Okno zavreno, ukoncuji...");
                return;
                //System.exit(0);
            }
        } );
    }

    /**
     * Kontrola jestli se GUI zavrelo
     * @param gui2 instance Gui
     */
    private void checkGUIShutdown(PlayerGui gui2){

        if(!gui2.isVisible()){
            System.out.println("Player: GUI zavreno - nastavuji allDone na true.");
            this.allDone = true;
        }
    }


    /**
     * Tisk informaci o tom kdo je na tahu do konzole a do gui
     */
    private void printAndSetInfo() {
        System.out.println("Player: "+ Output.getPlayerColorString(this.playerColor)+ " will play!");
        String infoText = "Player: it's "+ Output.getPlayerColorString(this.playerColor)+ "players turn!";
        gui.setInfoText(infoText);
    }

    /**
     * Vytiskne do gui notaci a zvysi pocitadlo tahu (kdyz tahnul zase bily)
     */
    private void printNotation() {

        if(this.playerColor == 'B'){
            gui.NotationNewLineLastBlack(bturn, btake);
        }else if(this.playerColor == 'W'){
            tahCislo++;
            gui.NotationNewLineLastWhite(wturn, wtake, this.tahCislo);
        }
    }

    public void run(){

        //inicializace

        System.out.println("Player: Uspavam se, cekam na vlozeni nastaveni, eventuelne loadedTurns, probud me a startgame.");
        this.pleaseWait = true;

        //smycka nastaveni prehravace
        while(true){

            synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping before startGame..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }

            //vypinaci cast
            checkGUIShutdown(gui);
            if(allDone){System.out.println("Player: Dama terminated, allDone!!");return;}

            if(this.startGame == false){
                continue;
            }

            this.desk = new Desk();
            gui.updateDesk(desk);
            gui.setPlayerButtons(true);

            System.out.println("Player: Tisknu nastaveni");
            System.out.println(this.settings.getXML());

            this.ouputTurns = new Vector<Turn>();

            if(this.loadedTurns != null && this.loadedTurns.size() > 0){
                System.out.println("Player: V loadedTurns je ulozeno " + this.loadedTurns.size() + " tahu");

                this.stepIndex = this.loadedTurns.size()-1;
                System.out.println("Player: index posledniho tahu " + this.stepIndex);
            }

            //smycka vlastni prehravace
            while(true){
                gameSetAndRuning = true;
                System.out.println("Player: tah cislo: " + tahCislo + ", bezi s timto nastavenim: " + this.settings.getSettingsString());

                //gui.setMenu(true);

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping at beginning of playing..."); wait();}
                catch (Exception e) { System.out.println("Player: probuzen"); } }  }

                //vypinaci cast
                checkGUIShutdown(gui);
                if(allDone){System.out.println(this.getId() + " - Player: Dama terminated, allDone!!");return;}

                //zmeny pro kazde kolo
                switchPlayerColors();
                printAndSetInfo();
                gui.setWantturn(Integer.toString(this.tahCislo) + ".");

                this.index++;
                this.stepIndex++;

                System.out.println("Player: pred - index: " + this.index + " | stepIndex: " + this.stepIndex);
                this.stepIndex = (this.stepIndex >= this.index) ? this.index : this.stepIndex;
                if(this.stepIndex < this.index){
                    System.out.println("Player: step index klesl pod index.");//bylo zmacknuto tlacitko pro krok zpet

                    //restart
                    this.desk = new Desk();
                    this.turn = null;
                    this.tahCislo = 0;
                    this.index = -1;
                    this.playerColor = 'X';
                    this.waitTime = 0;
                    this.blockMeAtIndex = this.stepIndex-1;
                    this.fastforward = true;
                    gui.NotationClear();
                    continue;
                }
                System.out.println("Player: PO - index: " + this.index + " | stepIndex: " + this.stepIndex);


                //vykresleni desky
                Check.isMustTake(desk, this.playerColor);
                if(!this.fastforward){gui.updateDesk(this.desk);}

                if(this.blockMeAtIndex == this.index){
                    this.fastforward = false;
                    this.waitTime = 1000;
                    this.pleaseWait = true;
                }

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping after updateDesk..."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }

                //ziskani dalsiho tahu
                if(this.loadedTurns != null && this.loadedTurns.size() > 0){// && this.index <= this.stepIndex/*this.loadedTurns.size()-1*/){//nactene tahy

                    //rychlost prehravani
                    try {Thread.sleep(this.waitTime);} catch (InterruptedException e) {
                        System.out.println("Player: prerusen spanek pri cekani na waittime.");
                    }

                    if(this.index <= this.loadedTurns.size()-1){
                        System.out.println("index:" + this.index);
                        Turn loadedTurn = new Turn(this.loadedTurns.elementAt(this.index));
                        this.turn = loadedTurn;
                    }else{
                        System.out.println("Player: dalsi tah uz nemam!");
                        //gui.setMenu(false);
                        gui.setForward(false);
                        this.pleaseWait = true;
                        synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping nemam dalsi tahy.."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }
                        continue;

                    }
                }

                //kontrola
                if(this.turn == null){
                    System.out.println("PlayerGui: Ziskany tah je null");
                    this.gui.createFail("Ziskany tah je null");
                    return;
                }

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping before checking turn..."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }

                //zpracovani tahu
                boolean executedTurn;
                System.out.println("Player: " + Output.getPlayerColorString(this.playerColor) + " is playing...");
                if(Check.isMustTake(this.desk, this.playerColor)){//Take
                    executedTurn = Moves.Take(this.desk, this.turn);
                    wbtake = true;
                }else{//Move
                    executedTurn = Moves.Move(this.desk, this.turn);
                    wbtake = false;
                }

                //kontrola
                if(executedTurn){
                    //System.out.println("PlayerGui: Tah " + this.turn.getTurnString() + " byl proveden");
                }else{
                    System.out.println("PlayerGui: Tah " + this.turn.getTurnString() + " NEBYL proveden");
                    //this.gui.createFail("Tah " + this.turn.getTurnString() + " NEBYL proveden");
                    return;
                }
                desk.clearMustTakeFlag();

                //zmena na damy
                Check.isPieceToDame(desk);

                //vykresleni desky
                if(!this.fastforward){gui.updateDesk(this.desk);}

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping before playerColorCheck..."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }

                //kontrola barvy hracu
                if(this.playerColor != this.turn.playerColor){
                    System.out.println("Player: barva hrace v tahu "+ this.turn.playerColor + " a v this.playerColor " + this.playerColor + " si neodpovida!");
                    gui.createFail("barva hrace v tahu "+ this.turn.playerColor + " a v this.playerColor " + this.playerColor + " si neodpovida!");
                    return;
                }

                //nastavovani promennych pro (a) tisk
                setWBTurn();
                setWBtake();
                this.ouputTurns.addElement(new Turn(this.turn));
                printNotation();

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping after printNotation..."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }

                //ukonceni prehravace pri vitezstvi TODO aby se neskoncilo ale slo zase krokovat

                if(Check.isVictory(desk)){
                    System.out.println("Player: Konec prehravani");
                    gui.setInfoText("!!! Victory !!!");
                    gui.setMenu(false);
                    this.pleaseWait = true;
                    //synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping victory..."); wait();} catch (Exception e) { System.out.println("Player: probuzen"); } }  }
                    //return;
                }
            }//smycka vlastni prehravace
        }
    }


    /**
     * Metoda ulozi flag o tom jestli se bralo do wtake nebo btake
     * dle barvy hrace na tahu
     */
    private void setWBtake() {

        if(this.playerColor == 'W'){
            wtake = wbtake;
        }else if(this.playerColor == 'B'){
            btake = wbtake;
        }else{
            System.out.println("Player: neco je spatne s barvami hracu!");
        }
    }

    /**
     * Metoda ulozi posledni provedeny tah do wturn nebo bturn
     * dle barvy hrace na tahu
     */
    private void setWBTurn() {

        if(this.playerColor == 'W'){
            this.wturn = this.turn;
        }else if(this.playerColor == 'B'){
            this.bturn = this.turn;
        }else{
            System.out.println("Player: neco je spatne s barvami hracu!");
        }
    }


    /**
     * Prohazuje barvy hracu
     */
    private void switchPlayerColors() {

        if (this.playerColor != 'W' && this.playerColor != 'B'){//prvni kolo
            this.playerColor = 'W';
        }else if (this.playerColor == 'W'){
            this.playerColor = 'B';
        }else if (this.playerColor == 'B'){
            this.playerColor = 'W';
        }else{
            System.out.println("Player: neco je spatne s barvami hracu!");
        }
    }
}
