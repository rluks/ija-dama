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
import dama.state.Position;
import dama.state.Settings;

/**
 * Prehravac ulozenych her
 * @author Roman Lukš
 * @author Pavel Hála
 *
 */
public class Player extends Thread{

    //staticke
    public static Player player;
    protected static Turn turnInput;

    //vlakno prehravace
    protected Settings settings;
    protected String currentPlayerAIHumanSettings;

    protected Vector<Turn> ouputTurns;        //sem hra uklada provedene tahy - pouzije se v pripade ulozeni hry
    protected Vector<Turn> loadedTurns;        //zde jsou ulozeny tahy v pripade nacteni hry

    protected Turn turn;
    protected Desk desk;
    protected char playerColor;
    protected int tahCislo = 0;
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
    protected boolean invalidTurn = false;

    /**
     * Konstruktor vlakna hry
     * @param name
     */
    Player(String name){
        super(name);

        gui = new PlayerGui();
        gui.setVisible(true);
        gui.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("Okno zavreno, ukoncuji...");
                System.exit(0);
            }
        } );

    }

    public void run(){

        //inicializace

        System.out.println("Player: Uspavam se, cekam na vlozeni nastaveni, eventuelne loadedTurns, probud me a startgame.");
        //Dama.sleepGameThread();
        this.pleaseWait = true;

        //smycka nastaveni hry
        while(true)
        {

            synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping before startGame..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }

            //vypinaci cast
            checkGUIShutdown(gui);
            if(allDone){System.out.println("Player: Dama terminated, allDone!!");return;}


            if(this.startGame == false){
                continue;
            }

            this.desk = new Desk();
            gui.updateDesk(desk);
            //this.desk.printSimpleBoard();


            System.out.println("Player: Tisknu nastaveni");
            System.out.println(this.settings.getXML());

            Position pEmpty = new Position(-1, -1);
            this.ouputTurns = new Vector<Turn>();

            //smycka vlastni hry
            while(true){
                gameSetAndRuning = true;
                System.out.println("Player: tah cislo: " + tahCislo + " ,bezi s timto nastavenim: " + this.settings.getSettingsString());

                //vypinaci cast
                checkGUIShutdown(gui);
                if(allDone){System.out.println(this.getId() + " - Player: Dama terminated, allDone!!");return;}

                //zmeny pro kazde kolo
                switchPlayerColors();
                this.currentPlayerAIHumanSettings = getAIorHumanSettingsForCurrentPlayerColor(this.playerColor);
                printAndSetInfo();

                //vykresleni desky
                Check.isMustTake(desk, this.playerColor);
                gui.updateDesk(this.desk);

                //ziskani dalsiho tahu
                if(this.loadedTurns != null && this.loadedTurns.size() > 0){//nactene tahy

                    System.out.println("Player: V loadedTurns zbyva " + this.loadedTurns.size() + " tahu");

                    gui.setMouse(false);
                    gui.setSubmitButton(false);

                    Turn loadedTurn = new Turn(this.loadedTurns.firstElement());
                    this.turn = loadedTurn;

                    this.loadedTurns.remove(0);

                }/*else if(this.currentPlayerAIHumanSettings == "AI"){//AI

                        System.out.println("Player: it's "+ Output.getPlayerColorString(this.playerColor)+ " AI turn!");
                        gui.setMouse(false);
                        gui.setSubmitButton(false);

                        //rychlost AI
                        try {Thread.sleep(this.settings.aiWait);} catch (InterruptedException e) {
                            System.out.println("Player: prerusen spanek pri tahu AI.");
                            //e.printStackTrace();
                        }

                        //vypinaci cast
                        checkGUIShutdown(gui);
                        if(allDone){System.out.println(this.getId() + " - Player: Dama terminated, allDone!!");return;}

                        this.turn = AI.AIMove(new Desk(this.desk), this.playerColor, this.aiStochasticity);

                }*/else{//Human

                    this.turn = (this.playerColor == 'W') ? Turn.TurnWhite(pEmpty, pEmpty) : Turn.TurnBlack(pEmpty, pEmpty);

                    boolean validTurn = false;

                    gui.setMouse(true);
                    gui.setSubmitButton(true);

                    while(!validTurn){
                        gui.clearAura();
                        this.pleaseWait = true;
                        synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping, waiting for human..."); wait();} catch (Exception e) {
                            System.out.println("Player: prerusen spanek pri tahu cloveka.");
                            /*e.printStackTrace();*/ } } }

                        //vypinaci cast
                        checkGUIShutdown(gui);
                        if(allDone){System.out.println("Player: Dama terminated, allDone!!");return;}

                        validTurn = Moves.isTurnValid(this.turn, this.desk);
                        if(validTurn){
                            System.out.println("Player: tah je validni");
                            validTurn = true;
                        }else{
                            System.out.println("Player: tah NENI validni");
                            validTurn = false;
                            gui.setInfoText("Nevalidni tah!");
                            gui.clearAura();
                            gui.ZobrazNapovedu();
                        }
                    }
                }

                //vypinaci cast
                //checkGUIShutdown(gui);
                //if(allDone){System.out.println("Player: Dama terminated, allDone!!");return;}

                //kontrola
                if(this.turn == null){
                    System.out.println("Ziskany tah je null");
                    this.gui.createFail("Ziskany tah je null");
                    return;
                }

                //zpracovani tahu
                boolean executedTurn;
                System.out.println("Player: " + Output.getPlayerColorString(this.playerColor) + " is playing...");
                if(Check.isMustTake(this.desk, this.playerColor)){//Take
                    executedTurn = Moves.Take(this.desk, this.turn);
                    System.out.println("Player: tah " + Output.getPlayerColorString(this.playerColor) + ", take() vraci:" + executedTurn);
                    wbtake = true;

                }else{//Move
                    executedTurn = Moves.Move(this.desk, this.turn);
                    System.out.println("Player: tah " + Output.getPlayerColorString(this.playerColor) + ", MOVE() vraci:" + executedTurn);
                    wbtake = false;
                }

                //kontrola
                if(executedTurn){
                    System.out.println("Tah " + this.turn.getTurnString() + " byl proveden");
                }else{
                    System.out.println("Tah " + this.turn.getTurnString() + " NEBYL proveden");
                    this.gui.createFail("Tah " + this.turn.getTurnString() + " NEBYL proveden");
                    return;
                }
                desk.clearMustTakeFlag();

                //zmena na damy
                Check.isPieceToDame(desk);

                //vykresleni desky
                gui.updateDesk(this.desk);

                //kontrola
                if(this.playerColor != this.turn.playerColor){
                    System.out.println("Player: barva hrace v tahu a v this.playerColor si neodpovida!");
                    gui.createFail("barva hrace v tahu a v this.playerColor si neodpovida!");
                    return;
                }

                //cisteni
                gui.clearAura();
                this.invalidTurn = false;

                //nastavovani promennych pro (a) tisk
                setWBTurn();
                setWBtake();
                this.ouputTurns.addElement(new Turn(this.turn));
                printNotation();

                //ukonceni hry pri vitezstvi
                if(Check.isVictory(desk)){
                    System.out.println("Player: Konec hry, dalsi hrac uz nehraje.");
                    try {
                        System.out.println("Player: Konec hry, tisknu xml s tahy.");
                        Output.printStringToFile(Output.getXmlFromSettingsAndTurnVector(this.settings, this.ouputTurns), "examples\\katafalka.xml");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        gui.createFail("Failed to save file!");
                    }
                    gui.setInfoText("!!! Victory !!!");
                    gui.createVictory();
                    gui.setMenu(false);
                    gui.setSubmitButton(false);
                    gui.setMouse(false);
                    this.pleaseWait = true;
                    synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping..."); wait();} catch (Exception e) { e.printStackTrace(); } }  }
                    return;
                }

                //vypinaci cast
                //checkGUIShutdown(gui);
                //if(allDone){System.out.println("Player: Dama terminated, allDone!!");return;}
            }
        }
    }


    /**
     * Vytiskne do gui notaci a zvysi pocitadlo tahu
     */
    private void printNotation() {

        if(this.playerColor == 'B'){
            //System.out.println("Player: oba hraci hrali");
            gui.NotationNewLineLastBlack(bturn, btake);
        }else if(this.playerColor == 'W'){
            tahCislo++;
            //System.out.println("Player: hral bily");
            gui.NotationNewLineLastWhite(wturn, wtake, this.tahCislo);
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
     * Tisk informaci o tom kdo je na tahu do konzole a do gui
     */
    private void printAndSetInfo() {
        System.out.println("Player: "+ Output.getPlayerColorString(this.playerColor)+ " will play!");
        String infoText = (currentPlayerAIHumanSettings == "AI")? "Player: it's "+ Output.getPlayerColorString(this.playerColor)+ " AI turn!" : "Player: it's "+ Output.getPlayerColorString(this.playerColor)+ "players turn!";
        gui.setInfoText(infoText);
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

    /**
     * Kontrola jestli se GUI zavrelo
     * @param gui2 instance Gui
     */
    private void checkGUIShutdown(PlayerGui gui2){

        if(!gui2.isVisible()){
            System.out.println("Player: GUI zavreno - nastavuji allDone na true.");
            this.allDone = true;
            //gui.pullThePlug();
            //System.exit(0);
        }
    }

    private String getAIorHumanSettingsForCurrentPlayerColor(char playerColor){
        if(playerColor == 'W'){
            return this.settings.white;
        }else if(playerColor == 'B'){
            return this.settings.black;
        }else{
            return "";
        }
    }

    //staticke metody
    public static void main(String[] args) throws InterruptedException {

        // projekt:
        // TODO javadoc
        // TODO k try-catch blokum dat do catch osetreni,zotaveni z chyb

        //Player.startingGameThread();

    }


    /**
     * Spusti vlakno prehravace
     */
    public static void startingPlayerThread(){

        System.out.println("startingPlayerThread...");
        player = new Player("PlayerVlakno");
        player.start();
    }



    /**
     * Uspi vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean sleepGameThread(){
        System.out.println("sleepGameThread: pokousim se uspat hru...");

        if(player != null){
            synchronized (player){
                player.pleaseWait = true;
            }
            return true;
        }else{
            System.out.println("sleepGameThread: hra neexistuje!");
            return false;
        }
    }



    /**
     * Probudi vlakno hry
    * @return vraci false pokud prehravac neexistuje
     */
    public static boolean wakeUpGameThread(){
        System.out.println("wakeUpGameThread: probouzim hru...");

        if(player != null){
            synchronized (player){
                player.pleaseWait = false;
                player.notify();
            }
            return true;
        }else{
            System.out.println("wakeUpGameThread: hra neexistuje!");
            return false;
        }
    }



    /**
     * Zastavi vlakno hry
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean stopGameThread(){
        System.out.println("stopGameThread: zastavuji vlakno hry...");

        if(player != null){
            if(player.isAlive()){
                player.allDone = true;
            }
            return true;
        }else{
            System.out.println("stopGameThread: hra neexistuje!");
            return false;
        }
    }

    /**
     * Do vlakna hry zapise ze bylo ulozeno settings a pripadne loadedTurns
     * a muze se tim padem zacit hrat
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean startGame(){
        System.out.println("startGame: povoluji beh smycky vlastni hry...");

        if(player != null){
            if(player.isAlive()){
                player.startGame = true;
            }
            return true;
        }else{
            System.out.println("startGame: hra neexistuje!");
            return false;
        }
    }



    /**
     * Kontrola jestli bezi vlakno hry
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean isGameThreadRunning(){
        if(player != null){
            if(player.isAlive()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Kontrola jestli uz se hraje (zda bylo vse nastaveno a probiha smycka hry)
     */
    public static boolean isGameSetAndRunning(){

        if(Player.isGameThreadRunning()){
            synchronized (player){
                if(player.gameSetAndRuning){
                    return true;
                }else{
                    return false;
                }
            }
        }else{
            return false;
        }
    }

    public static boolean interruptGameThread(){

        if(player != null){
            if(player.isAlive()){
                player.interrupt();
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static boolean setInvalidTurn(boolean invalidturn) {
        System.out.println("setInvalidTurn: davam hre vedet ze byl nevalidni tah...");

        if(player != null){
            synchronized (player){
                player.invalidTurn  = invalidturn;
                player.notify();
            }
            return true;
        }else{
            System.out.println("setInvalidTurn: hra neexistuje!");
            return false;
        }
    }

    /**
     * Uklada nastaveni do vlakna hry
     * @param settings instance tridy Settings s nastavenim hry
     * @return vraci false pokud hra neexistuje
     */
    public static boolean setSettings(Settings settings){
        System.out.println("setSettings: ukladam nastaveni...");

        if(player != null){
            synchronized (player){
                player.settings = settings;
                player.notify();
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

        if(player != null){
            synchronized (player){
                player.turn = turn;
                player.notify();
            }
            return true;
        }else{
            System.out.println("setTurn: hra neexistuje!");
            return false;
        }

    }



    public static boolean setLoadedTurns(Vector<Turn> loadedTurns){

        System.out.println("setTurn: ukladam nactene tahy do vlakna hry...");

        if(player != null){
            synchronized (player){
                player.loadedTurns = loadedTurns;
                player.notify();
            }
            return true;
        }else{
            System.out.println("setLoadedTurns: hra neexistuje!");
            return false;
        }
    }



    /**
     * Z vlakna hry ziska desku
     * @return desk instance tridy Desk obsahujici stav hry na sachovnici
     */
    public static Desk getDesk(){
        synchronized (player){
            return player.desk;
        }
    }

    /**
     * Z vlakna hry ziska turn
     * @return turn instance tridy Turn obsahujici jeden pultah
     */
    public static Turn getTurn(){
        synchronized (player){
            return player.turn;
        }
    }


    /**
     * Z vlakna hry ziska barvu hrace na tahu
     * @return char barva hrace 'W' nebo 'B', pripadne '\0'
     */
    public static char getPlayerColor(){
        synchronized (player){
            return player.playerColor;
        }
    }

    /**
     * Z vlakna hry ziska settings
     * @return
     */
    public static Settings getSettings(){
        synchronized (player){
            return player.settings;
        }
    }

    /**
     * Z vlakna hry ziska vektor tahu
     * @return
     */
    public static Vector<Turn> getOutputTurns(){
        synchronized (player){
            return player.ouputTurns;
        }
    }

    /**
     * Nastavi do vlakna hry vstupni Turn
     * @param turn
     */
    public static void setTurnInput(Turn turn) {
        Player.turnInput = turn;
    }

    /**
     * Ziskani hodnoty vstupniho Turn z vlakna hry
     * @return
     */
    public static Turn getTurnInput() {
        return Player.turnInput;
    }
}
