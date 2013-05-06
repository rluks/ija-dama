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

    /**
     * Z vlakna prehravace ziska desku
     * @return desk instance tridy Desk obsahujici stav na sachovnici
     */
    public static Desk getDesk(){
        synchronized (player){
            return player.desk;
        }
    }
    /**
     * Z vlakna prehravace ziska vektor tahu
     * @return
     */
    public static Vector<Turn> getOutputTurns(){
        synchronized (player){
            return player.ouputTurns;
        }
    }

    /**
     * Z vlakna prehravace ziska barvu hrace na tahu
     * @return char barva hrace 'W' nebo 'B', pripadne '\0'
     */
    public static char getPlayerColor(){
        synchronized (player){
            return player.playerColor;
        }
    }
    /**
     * Z vlakna prehravace ziska settings
     * @return
     */
    public static Settings getSettings(){
        synchronized (player){
            return player.settings;
        }
    }

    /**
     * Z vlakna prehravace ziska turn
     * @return turn instance tridy Turn obsahujici jeden pultah
     */
    public static Turn getTurn(){
        synchronized (player){
            return player.turn;
        }
    }

    /**
     * Z vlakna prehravace ziska jestli hraje clovek
     * @return true hraje clovek, false hraji se ulozene tahy
     */
    public static boolean isHumanPlaying(){
        synchronized (player){
            return player.human;
        }
    }

    /**
     * Z vlakna prehravace ziska velikost vektoru nactenych tahu
     */
    public static int getLoadedTurnsSize(){
        synchronized (player){
            return player.loadedTurns.size();
        }
    }


    /**
     * Ziskani hodnoty vstupniho Turn ze staticke promenne turnInput
     * @return
     */
    public static Turn getTurnInput() {
        return Player.turnInput;
    }



    public static boolean interruptPlayerThread(){

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


    /**
     * Kontrola jestli uz se hraje (zda bylo vse nastaveno a probiha smycka prehravace)
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


    /**
     * Kontrola jestli bezi vlakno prehravace
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


    public static boolean setLoadedTurns(Vector<Turn> loadedTurns){

        System.out.println("Player: setTurn: ukladam nactene tahy do vlakna prehravace...");

        if(player != null){
            synchronized (player){
                player.loadedTurns = loadedTurns;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: setLoadedTurns: hra neexistuje!");
            return false;
        }
    }
    /**
     * Uklada nastaveni do vlakna prehravace
     * @param settings instance tridy Settings s nastavenim prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setSettings(Settings settings){
        System.out.println("Player: setSettings: ukladam nastaveni...");

        if(player != null){
            synchronized (player){
                player.settings = settings;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: setSettings: hra neexistuje!");
            return false;
        }
    }
    /**
     * Uklada tah do vlakna prehravace
     * @param turn instance tridy
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setTurn(Turn turn){

        System.out.println("Player: setTurn: ukladam tah...");

        if(player != null){
            synchronized (player){
                player.turn = turn;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: setTurn: hra neexistuje!");
            return false;
        }

    }

    /**
     * Nastavi do staticke promenne turnInput hodnotu turn
     * @param turn
     */
    public static void setTurnInput(Turn turn) {
        Player.turnInput = turn;
    }

    /**
     * Nastavi do vlakna prehravace dobu cekani pri prehravani
     * @param waittime
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setWaitTime(int waittime){

        System.out.println("Player: setWaitTime: ukladam dobu cekani " + (double)waittime/1000.0 + "s ...");

        if(player != null){
            synchronized (player){
                player.waitTime = waittime;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: setWaitTime: hra neexistuje!");
            return false;
        }
    }

    /**
     * Nastavi do vlakna prehravace o kolik se ma posunout v tazich
     * @param indexdelta
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean setIndexChange(int indexdelta){

        if(player != null){
            synchronized (player){

                int stepIndex = Player.getStepIndex();

                if ((stepIndex >= 0 && indexdelta > 0 && stepIndex+indexdelta < Player.getLoadedTurnsSize()) || (indexdelta < 0 && stepIndex-indexdelta > 0)){
                    player.stepIndex += indexdelta;
                    System.out.println("Player: setIndexChange: zmena " + stepIndex + " o " + indexdelta + " na " + Player.getStepIndex());
                    player.notify();
                }
            }
            return true;
        }else{
            System.out.println("Player: setIndexChange: hra neexistuje!");
            return false;
        }
    }

    /**
     * Ziska hodnotu stepIndex z vlakna prehravace
     * @return integer hodnotu
     */
    public static int getStepIndex(){
        synchronized (player){
            return player.stepIndex;
        }
    }


    /**
     * Uspi vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean sleepPlayerThread(){
        System.out.println("Player: sleepGameThread: pokousim se uspat hru...");

        if(player != null){
            synchronized (player){
                player.pleaseWait = true;
            }
            return true;
        }else{
            System.out.println("Player: sleepGameThread: hra neexistuje!");
            return false;
        }
    }


    /**
     * Do vlakna prehravace zapise ze bylo ulozeno settings a pripadne loadedTurns
     * a muze se tim padem zacit hrat
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean startPlayer(){
        System.out.println("Player: startGame: povoluji beh smycky prehravace...");

        if(player != null){
            if(player.isAlive()){
                player.startGame = true;
            }
            return true;
        }else{
            System.out.println("Player: startGame: hra neexistuje!");
            return false;
        }
    }


    /**
     * Spusti vlakno prehravace
     */
    public static void startingPlayerThread(){

        System.out.println("Player: startingPlayerThread...");
        player = new Player("PlayerVlakno");
        player.start();
    }
    /**
     * Zastavi vlakno prehravace
     * @return vraci false pokud prehravac neexistuje
     */
    public static boolean stopThread(){
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
    * @return vraci false pokud prehravac neexistuje
     */
    public static boolean wakeUpPlayerThread(){
        System.out.println("Player: wakeUpGameThread: probouzim hru...");

        if(player != null){
            synchronized (player){
                player.pleaseWait = false;
                player.notify();
            }
            return true;
        }else{
            System.out.println("Player: wakeUpGameThread: hra neexistuje!");
            return false;
        }
    }

    //vlakno prehravace
    protected Settings settings;
    protected String currentPlayerAIHumanSettings;
    protected Vector<Turn> ouputTurns;        //sem hra uklada provedene tahy - pouzije se v pripade ulozeni prehravace
    protected Vector<Turn> loadedTurns;        //zde jsou ulozeny tahy v pripade nacteni prehravace
    protected Turn turn;
    protected Desk desk;
    protected char playerColor;
    protected int tahCislo = 0;

    protected boolean human;//indikace ze se ceka na vstup od cloveka, pouziti pro zamceni play tlacitka

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
    protected boolean invalidTurn = false;

    /**
     * Konstruktor vlakna prehravace
     * @param name
     */
    Player(String name){
        super(name);

        gui = new PlayerGui();
        gui.setVisible(true);
        gui.addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.out.println("PlayerGui: Okno zavreno, ukoncuji...");
                System.exit(0);
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


    private String getAIorHumanSettingsForCurrentPlayerColor(char playerColor){
        if(playerColor == 'W'){
            return this.settings.white;
        }else if(playerColor == 'B'){
            return this.settings.black;
        }else{
            return "";
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
     * Vytiskne do gui notaci a zvysi pocitadlo tahu
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
            gui.setMouse(false);

            System.out.println("Player: Tisknu nastaveni");
            System.out.println(this.settings.getXML());

            Position pEmpty = new Position(-1, -1);
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

                synchronized(this){ while(pleaseWait){ try { System.out.println("Player: sleeping at beginning of playing..."); wait();}
                catch (Exception e) { System.out.println("Player: probuzen"); /*e.printStackTrace();*/ } }  }

                //vypinaci cast
                checkGUIShutdown(gui);
                if(allDone){System.out.println(this.getId() + " - Player: Dama terminated, allDone!!");return;}

                //zmeny pro kazde kolo
                switchPlayerColors();
                this.currentPlayerAIHumanSettings = getAIorHumanSettingsForCurrentPlayerColor(this.playerColor);
                printAndSetInfo();
                this.index++;

                //vykresleni desky
                Check.isMustTake(desk, this.playerColor);
                gui.updateDesk(this.desk);

                //ziskani dalsiho tahu
                if(this.loadedTurns != null && this.loadedTurns.size() > 0 && this.index <= this.stepIndex/*this.loadedTurns.size()*/){//nactene tahy

                    //System.out.println("Player: V loadedTurns je ulozeno " + this.loadedTurns.size() + " tahu");

                    this.human = false;
                    gui.setSubmitButton(false);

                    //rychlost prehravani
                    try {Thread.sleep(this.waitTime);} catch (InterruptedException e) {
                        System.out.println("Player: prerusen spanek pri cekani na waittime.");
                    }

                    //Turn loadedTurn = new Turn(this.loadedTurns.firstElement());
                    Turn loadedTurn = new Turn(this.loadedTurns.elementAt(this.index));
                    this.turn = loadedTurn;

                    //this.loadedTurns.remove(0);

                }else{//Human

                    this.human = true;
                    this.turn = (this.playerColor == 'W') ? Turn.TurnWhite(pEmpty, pEmpty) : Turn.TurnBlack(pEmpty, pEmpty);

                    boolean validTurn = false;

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

                //kontrola
                if(this.turn == null){
                    System.out.println("PlayerGui: Ziskany tah je null");
                    this.gui.createFail("Ziskany tah je null");
                    return;
                }

                //zpracovani tahu
                boolean executedTurn;
                System.out.println("Player: " + Output.getPlayerColorString(this.playerColor) + " is playing...");
                if(Check.isMustTake(this.desk, this.playerColor)){//Take
                    executedTurn = Moves.Take(this.desk, this.turn);
                    //System.out.println("Player: tah " + Output.getPlayerColorString(this.playerColor) + ", take() vraci:" + executedTurn);
                    wbtake = true;

                }else{//Move
                    executedTurn = Moves.Move(this.desk, this.turn);
                    //System.out.println("Player: tah " + Output.getPlayerColorString(this.playerColor) + ", MOVE() vraci:" + executedTurn);
                    wbtake = false;
                }

                //kontrola
                if(executedTurn){
                    //System.out.println("PlayerGui: Tah " + this.turn.getTurnString() + " byl proveden");
                }else{
                    System.out.println("PlayerGui: Tah " + this.turn.getTurnString() + " NEBYL proveden");
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

                //ukonceni prehravace pri vitezstvi
                if(Check.isVictory(desk)){
                    System.out.println("Player: Konec prehravace, dalsi hrac uz nehraje.");
                    try {
                        System.out.println("Player: Konec prehravace, tisknu xml s tahy.");
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
            }
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


    /**
    * Jednotny vstup tahu pro vlakno prehravace
    */
    public static void inputTurn(Turn turn)
    {

        if(Moves.isTurnValid(turn, Player.getDesk()))
        {
            System.out.println("Player.inputTurn: vkladam tah");
            Player.setTurn(turn);
            //Player.wakeUpPlayerThread();
        }
        else
        {
            System.out.println("Player.inputTurn: nevalidni tah, prehravac MUSI nastavit napovedu");
            Player.setTurn(turn);
            //Player.wakeUpPlayerThread();
        }
    }
}
