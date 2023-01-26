package Desperatedrosseln.Logic;

import Desperatedrosseln.Connection.ClientHandler;
import Desperatedrosseln.Json.utils.JsonMapReader;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.AI.AIClient;
import Desperatedrosseln.Logic.Cards.Card;
import Desperatedrosseln.Logic.Cards.Damagecard;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Elements.*;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Tiles.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static Desperatedrosseln.Logic.DIRECTION.TOP;

/**
 * @author Mehyar, Luca
 */
public class Game {
    private Map gameMap;
    private static String currentMap;
    private static ArrayList<Player> players = new ArrayList<>();
    private ArrayList<BoardElement> boardElements = new ArrayList<>();
    private List<Player> rebooted_players = new ArrayList<>();
    Moshi moshi = new Moshi.Builder().build();

    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
    private int phase = 0;
    private Player playing;
    private int current_player_index = 0;
    public int startingPositionsChosen=0;
    private int current_register = 0;
    public static int mapSelectionPlayer = -1;
    //
    private ArrayList<Card> spampile = new ArrayList<>(38);
    private ArrayList<Card> viruspile = new ArrayList<>(18);
    private ArrayList<Card> trojanpile = new ArrayList<>(12);
    private ArrayList<Card> wormpile = new ArrayList<>(6);
    private ArrayList<ClientHandler> clients;

    private static final Logger logger = LogManager.getLogger();
    private int distance;
    private final int port;
    private String protocol = "Version 1.0";
    private boolean isRunning = false;

    public Game(int port, String protocol, ArrayList<ClientHandler> clients) {
        this.protocol = protocol;
        this.port = port;
        this.clients = clients;
        addAI();
    }


    public static void readyPlayer(ClientHandler client) {             //TODO: case player disconnects
        if (!client.isAI && mapSelectionPlayer == -1) {
            mapSelectionPlayer = client.getPlayer().getID();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
            ArrayList<String> maps = new ArrayList<>();
            maps.add("DizzyHighway");
            client.sendMessage("SelectMap", selectMapJsonAdapter.toJson(new SelectMap(maps)));
        }
    }

    public void placeRobot(Player player, int x, int y) {
        gameMap.addElement(player.getRobot(), x, y);
    }

    public void runStep() throws ClassNotFoundException {

        if(isRunning){
            return;
        }
        isRunning = true;
        switch (phase) {
            case 0:
                logger.info("Current phase: Starting Phase");
                setUpBoard();
                break;
            case 1:
                logger.info("Current phase: Upgrade Phase");
                break;
            case 2:
                logger.info("Current phase: Programming phase");
                //TODO: check players in reboot array and reposition their robots then take them out of reboot list
                runProgrammingPhase();
                break;
            case 3:
                logger.info("Current phase: Activation phase");
                runActivationPhase();
                break;

        }



    }

    /**
     * initialize all the necessary values, generate map etc
     */
    public void initialize() {
        for (Player player : players) {
            //TODO: set number of checkpoints dynamically from the gamemap info
        }
    }
    public boolean selectionFinished(){
        /*int i=0;
        while(i<players.size()){
            logger.warn(players.get(i).getRegisterTrack() + " ID: " + players.get(i).getID());
            if(players.get(i).getRegisterTrack() < 5){
                return false;
            }
            i++;
        }*/
        for (Player player: players){
            if(player.getRegisterTrack() < 5){
                return false;
            }
        }
        return true;
    }

    /*
    Aufbauphase
     */
    public void setUpBoard() {
        JsonMapReader jsonMapReader = new JsonMapReader();
        List<List<List<BoardElement>>> gameMapList = jsonMapReader.readMapFromJson(currentMap);
        gameMap = new Map(convertMap(gameMapList));

        for (int x = 0; x < gameMapList.size(); x++) {
            for (int y = 0; y < gameMapList.get(x).size(); y++) {
                gameMapList.get(x).get(y).get(0).setPosition(x,y);
            }
        }

        phase = 2;
        isRunning = false;

        //setting up boardelements
        for(int i=0; i<gameMap.getMapFields().size(); i++){
            for(int j=0; j<gameMap.getMapFields().get(0).size(); j++){
                for(BoardElement element : gameMap.getMapFields().get(0).get(0).getTypes()){
                    boardElements.add(element);
                }
            }
        }
    }

    private List<List<MapField>> convertMap(List<List<List<BoardElement>>> gameMapList) {
        List<List<MapField>> mapFields = new ArrayList<>();

        for (List<List<BoardElement>> rows : gameMapList) {
            List<MapField> column = new ArrayList<>();
            for (List<BoardElement> list : rows) {
                List<BoardElement> typeList = new ArrayList<>(list);
                MapField mapField = new MapField(typeList);
                column.add(mapField);
            }
            mapFields.add(column);
        }
        return mapFields;
    }


    /**
     * this method initiates the programmingphase
     */
    private void runProgrammingPhase() {
        //send the current ActivePhase to all Clients
        JsonAdapter<ActivePhase> activePhaseJsonAdapter = moshi.adapter(ActivePhase.class);
        ActivePhase activePhase2 = new ActivePhase(phase);
        broadcastMessage("ActivePhase", activePhaseJsonAdapter.toJson(activePhase2));

        for (Player player : players) {
            int shuffled = player.programmingPhase();

            if (shuffled == 2) {
                JsonAdapter<ShuffleCoding> shuffleCodingJsonAdapter = moshi.adapter(ShuffleCoding.class);
                broadcastMessage("ShuffleCoding", shuffleCodingJsonAdapter.toJson(new ShuffleCoding(player.getID())));

                for (ClientHandler client :
                        clients) {
                    if (client.getClientID() == player.getID()) {                       //send the automatically filled cards to player
                        JsonAdapter<CardsYouGotNow> cardsYouGotNowJsonAdapter = moshi.adapter(CardsYouGotNow.class);
                        client.sendMessage("CardsYouGotNow", cardsYouGotNowJsonAdapter.toJson(new CardsYouGotNow(player.getCardsYouGotNow())));
                    }
                }

            }

            for (ClientHandler client :
                    clients) {
                JsonAdapter<NotYourCards> notYourCardsJsonAdapter = moshi.adapter(NotYourCards.class);
                if (client.getClientID() == player.getID()) {
                    JsonAdapter<YourCards> yourCardsJsonAdapter = moshi.adapter(YourCards.class);
                    client.sendMessage("YourCards", yourCardsJsonAdapter.toJson(new YourCards(player.getHandasStrings())));
                } else {
                    client.sendMessage("NotYourCards", notYourCardsJsonAdapter.toJson(new NotYourCards(player.getID(), player.getHand().size())));
                }
            }


        }
        phase = 3;
        isRunning = false;

    }

    /**
     * selects card from hand into register slot
     *
     * @param number   of the card in the hand. Starts by 0
     * @param register number of register slot filled. Starts by 0
     * @param player   player who selected the card
     */
    private void selectedCard(int number, int register, Player player) {
        player.selectCard(number, register);
    }


    private void broadcastMessage(String type, String json) {
        for (ClientHandler client :
                clients) {
            client.sendMessage(type, json);
        }

    }

    /**
     * this method initiates the Activationphase
     */
    private void runActivationPhase() throws ClassNotFoundException {
        phase = 3;
        /*
        JsonAdapter<ActivePhase> activePhaseJsonAdapter = moshi.adapter(ActivePhase.class);
        ActivePhase activePhase3 = new ActivePhase(phase);
        broadcastMessage("ActivePhase", activePhaseJsonAdapter.toJson(activePhase3));*/
        decideNextPlayer();
        //for each register
        for (int current_register = 0; current_register < 5; current_register++) {
            logger.debug("Activating register: "+ current_register);
            this.current_register = current_register;
            //each player plays their register
            ArrayList<CurrentCards.ActiveCards> activeCardsArrayList = new ArrayList<>();
            for (int played = 1; played <= players.size(); played++) {
                //find the current player and let them play
                for (Player curr : players) {
                    logger.debug("Register " + current_register + "For Player: "+ curr.getID());
                    //Isn't going into condition
                    if (curr.getID()==playing.getID() && !rebooted_players.contains(curr)) {
                        //Server sends currentPlayer message to every Client
                        JsonAdapter<CurrentPlayer> currentPlayerJsonAdapter = moshi.adapter(CurrentPlayer.class);
                        CurrentPlayer currentPlayer = new CurrentPlayer(playing.getID());
                        broadcastMessage("CurrentPlayer", currentPlayerJsonAdapter.toJson(currentPlayer));

                        //the active player plays their card in the current register
                        playCardByType(curr.getRegisterIndex(current_register), curr, current_register);

                        activeCardsArrayList.add(new CurrentCards.ActiveCards(curr.getID(), curr.getRegisterIndex(current_register)));

                    }
                }
            }
            JsonAdapter<CurrentCards> currentCardsJsonAdapter = moshi.adapter(CurrentCards.class);
            broadcastMessage("CurrentCards", currentCardsJsonAdapter.toJson(new CurrentCards(activeCardsArrayList)));
            activateElements();
        }
        isRunning = false;
    }

    /**
     * runs the card from the register, checks if programming or damage card
     *
     * @param card
     * @param curr
     * @param register_number
     */
    private void playCardByType(Card card, Player curr, int register_number) {
        //TODO: if player is not rebooting
        if (card.isDamageCard()) {
            JsonAdapter<ReplaceCard> replaceCardJsonAdapter = moshi.adapter(ReplaceCard.class);
            switch (card.toString()) {
                case "Spam" -> {
                    Card top_of_deck = curr.drawCardFromDeck();
                    //draw first card from deck and play it in register, then put back spam card in spampile
                    curr.getRegisters()[register_number] = top_of_deck;
                    top_of_deck.playCard(curr.getRobot());
                    spampile.add(card);

                    broadcastMessage("ReplaceCard", replaceCardJsonAdapter.toJson(new ReplaceCard(current_register, top_of_deck.toString(), curr.getID())));

                }
                case "Worm" -> {
                    drawSpamCard(curr, 2);
                    List<BoardElement> respawns = getListOf("RestartPoint");
                    Position newPos = new Position(0,0);
                    for (BoardElement respawn:
                            respawns) {

                            if(!gameMap.getMapFields().get(respawn.getPosition().getX()).get(respawn.getPosition().getY()).getTypes().contains(curr.getRobot())){
                                newPos.copy(respawn.getPosition());
                                curr.getRobot().reboot(TOP, newPos);
                                rebootPlayer(curr);
                                break;
                            }


                    }

                    curr.getRobot().reboot(DIRECTION.TOP, newPos);
                    rebootPlayer(curr);
                }
                case "Trojan" -> {
                    Card top_of_deck = curr.drawCardFromDeck();
                    //draw first card from deck and play it in register, draw 2 spam cards add them to deck,
                    //then put back trojan card in trojanpile
                    curr.getRegisters()[register_number] = top_of_deck;
                    top_of_deck.playCard(curr.getRobot());
                    trojanpile.add(card);
                    drawSpamCard(curr, 2);
                    broadcastMessage("ReplaceCard", replaceCardJsonAdapter.toJson(new ReplaceCard(current_register, top_of_deck.toString(), curr.getID())));
                }
                case "Virus" -> {
                    Card top_of_deck = curr.drawCardFromDeck();
                    //draw first card from deck and play it in register, then put back virus card in viruspile
                    curr.getRegisters()[register_number] = top_of_deck;
                    top_of_deck.playCard(curr.getRobot());
                    viruspile.add(card);
                    for (Player player : sixFieldRadius(curr.getID(), curr.getRobot().getPosition())) {
                        drawSpamCard(player, 1);
                    }
                    broadcastMessage("ReplaceCard", replaceCardJsonAdapter.toJson(new ReplaceCard(current_register, top_of_deck.toString(), curr.getID())));
                }
            }


        } else {

            if (curr.getRegisterIndex(register_number).toString().equals("Again")
                    && register_number > 0) {
                curr.getRegisterIndex(register_number - 1).playCard(curr.getRobot());
                switch (curr.getRegisterIndex(register_number - 1).toString()) {
                    case "Move1":
                    case "Move2":
                    case "Move3":
                    case "MoveBack":
                        robotMovedProtokoll(curr.getRobot());
                }
            } else {
                curr.getRegisterIndex(register_number).playCard(curr.getRobot());
            }

            switch (curr.getRegisterIndex(register_number).toString()) {
                case "Move1":
                case "Move2":
                case "Move3":
                case "MoveBack":
                    robotMovedProtokoll(curr.getRobot());
            }

        }
    }

    /**
     * draws a spam card from the spam pile and adds it to the deck
     *
     * @param player:        the player to draw the spam cards
     * @param numberOfCards: the number of spam cards to draw
     */
    private void drawSpamCard(Player player, int numberOfCards) {

        ArrayList<String> cards = new ArrayList<>();

        if (spampile.size() >= numberOfCards) {
            for (int i = 0; i < numberOfCards; i++) {
                drawDamageCard(player, "Spam");
                cards.add("Spam");
            }
        } else if (spampile.size() > 0 && spampile.size() < numberOfCards) {
            drawDamageCard(player, "Spam");
            cards.add("Spam");
        } else {
            Damagecard requestedcard = chooseAnotherDamageCard(player, numberOfCards);
            for (int i = 0; i < numberOfCards; i++) {
                drawDamageCard(player, requestedcard.toString());
                cards.add(requestedcard.toString());
            }
        }
        drawDamageProtokoll(player.getRobot(), cards);
    }

    /**
     * adds a damage card from its pile to the discarded deck if the pile is not empty
     *
     * @param player
     * @param type   the type of the damage card
     */
    private void drawDamageCard(Player player, String type) {
        switch (type) {
            case "Spam" -> {
                if (!spampile.isEmpty()) {
                    player.getDiscarded().add(spampile.get(spampile.size() - 1));
                    spampile.remove(spampile.size() - 1);
                }
            }
            case "Virus" -> {
                if (!viruspile.isEmpty()) {
                    player.getDiscarded().add(viruspile.get(viruspile.size() - 1));
                    viruspile.remove(viruspile.size() - 1);
                }
            }
            case "Trojan" -> {
                if (!trojanpile.isEmpty()) {
                    player.getDiscarded().add(trojanpile.get(trojanpile.size() - 1));
                    trojanpile.remove(trojanpile.size() - 1);
                }
            }
            case "Worm" -> {
                if (!wormpile.isEmpty()) {
                    player.getDiscarded().add(wormpile.get(wormpile.size() - 1));
                    wormpile.remove(wormpile.size() - 1);
                }
            }

            default -> {
                logger.warn("Invalid type of card");
            }
        }
    }

    /**
     * @param player
     * @returns the alternative damagecard the player wants to draw instead
     */
    private Damagecard chooseAnotherDamageCard(Player player, int count) {
        //TODO: protocol pickDamage and SelectedDamage
        //Request to client
        for (ClientHandler client : clients) {
            if (client.getClientID() == player.getID()) {
                //check all the DamageCard piles if they are empty or not - if not add them to availablePiles
                List<String> availablePiles = new ArrayList<>();
                if (viruspile.size()>0){
                    availablePiles.add("Virus");
                }
                if (trojanpile.size()>0){
                    availablePiles.add("Trojan");
                }
                if (wormpile.size()>0){
                    availablePiles.add("Worm");
                }
                //send the request to the client which damagecards he wants to pick
                JsonAdapter<PickDamage> pickDamageJsonAdapter = moshi.adapter(PickDamage.class);
                client.sendMessage("PickDamage", pickDamageJsonAdapter.toJson(new PickDamage(count, availablePiles)));
            }
        }
            //Get client answer - receive "SelectedDamage"
            Damagecard requestedcard = new Damagecard();
            return requestedcard;
        }


    /**
     * returns a list of players in 6 field radius of the Position pos
     *
     * @param id
     * @param pos
     * @return
     */
    private List<Player> sixFieldRadius(int id, Position pos) {
        List<Player> playersInRadius = new ArrayList<>();
        for (Player player : players) {
            if (calculateDistance(player.getRobot().getPosition(), pos) <= 6 && player.getID() != id) {
                playersInRadius.add(player);
            }
        }
        return playersInRadius;
    }

    /**
     * @param curr: the current player playing
     */
    private void rebootPlayer(Player curr) {
        //add the current player to the list of rebooting players
        rebooted_players.add(curr);
        //put all cards that are left in the register of mentioned player onto his discardpile without activating them
        for (Card card : curr.getRegisters()) {
            curr.getDiscarded().add(card);
        }
        //send reboot protocoll message to all clients
        JsonAdapter<Reboot> rebootJsonAdapter = moshi.adapter(Reboot.class);
        Reboot reboot = new Reboot(curr.getID());
        broadcastMessage("Reboot", rebootJsonAdapter.toJson(reboot));
        //ToDo: get rebootfield coordinates and send Movement protocoll
    }

    private void decideNextPlayer() {
        current_player_index++;
        //if the current player is the last player, start from the beginning
        if (current_player_index >= players.size()) {
            current_player_index = 0;
        }
        playing = players.get(current_player_index++);
    }

    private int calculateDistance(Position pos1, Position pos2) {
        return distance;
    }

    private void activateElements() throws ClassNotFoundException {
        activatePits();
        activateConveyorBelts();
        activatePushPanels();
        activateGears();
        activateBoardLasers();
        activateRobotsLasers();
        activateEnergySpaces();
        activateCheckpoints();
        for(Player player: players){
            robotMovedProtokoll(player.getRobot());
        }

    }

    private void activateBoardLasers() {
        List<BoardElement> lasers = getListOf("Laser");
        //determine the range of each laser
        //check if there is a robot within that range
        //idee: recursive algorithm which keeps checking the next cell until it hits

        for (BoardElement laser : lasers) {
            //Assert laser instance of Laser
            shootBoardLaser((Laser) laser);
        }
        /**
         * Board lasers
         * cannot fire through walls, the priority
         * antenna, or hit more than one robot,
         * and they shoot from the red and white
         * pointer. (Take a SPAM damage card for
         * each laser that hits you
         */
    }

    private void shootBoardLaser(Laser laser) {
        Position pos = laser.getPosition();
        boolean laserhit = false;
        List<BoardElement> elemetsOnPos;

        //TODO: not sure about which (X or Y) is width and which is length
        while (pos.getX() < gameMap.getWidth() && pos.getY() < gameMap.getLength()
                && !laserhit) {
            //as long as within the board, and laser still didnt hit any element
            if (hasLaserBlock(pos)) {
                //if cell has a robot or an antenna on it
                elemetsOnPos = gameMap.getElementsOnPos(pos);
                for (BoardElement element : elemetsOnPos) {
                    //either robots on cell get damage, or laser stops
                    if (element instanceof Robot) {
                        laserHitRobot((Robot) element);
                        laserhit = true;
                    } else if (element instanceof Antenna) {
                        laserhit = true;
                    } else if (element instanceof Wall) {
                        laserhit = true;
                    }
                }
            } else {
                pos = getNextPos(pos, laser.getDirection());
            }
        }
    }

    /**
     * select player by robots id and make him draw a spam card
     *
     * @param robot the robot that was hit by the laser
     */
    private void laserHitRobot(Robot robot) {
        Player player = getPlayerByRobot(robot);
        drawSpamCard(player, 1);
    }

    /**
     * @param robot the robot whose player is being looked for
     * @return the player who has selected the robot in question
     */
    private Player getPlayerByRobot(Robot robot) {
        int i = 0;
        while (i < players.size()) {
            if (players.get(i).getRobot().equals(robot)) {
                return players.get(i);
            } else {
                i++;
            }
        }
        return null;
    }

    /**
     * this method is used to calculate the next position in which a laser moves
     *
     * @param pos       the starting position
     * @param direction the direction of the laser
     * @return the next position depending on the direction
     */
    private Position getNextPos(Position pos, DIRECTION direction) {
        int x = pos.getX();
        int y = pos.getY();
        switch (direction) {
            case TOP -> {
                x = pos.getX() - 1;
                y = pos.getY();
            }
            case BOTTOM -> {
                x = pos.getX() + 1;
                y = pos.getY();
            }
            case LEFT -> {
                x = pos.getX();
                y = pos.getY() - 1;
            }
            case RIGHT -> {
                x = pos.getX();
                y = pos.getY() + 1;
            }
        }
        return new Position(x, y);
    }

    /**
     * checks if there are robots an antenna or a wall on a position
     *
     * @param pos the position of the board to check
     * @return true if there are robots or an antenna on pos
     */
    private boolean hasLaserBlock(Position pos) {
        List<BoardElement> elements = gameMap.getElementsOnPos(pos);
        int i = 0;
        while (i < elements.size()) {
            if (elements.get(i) instanceof Robot || elements.get(i) instanceof Antenna
                    || elements.get(i) instanceof Wall) {
                return true;
            } else {
                i++;
            }
        }
        return false;
    }

    private void activateRobotsLasers() {
        /**
         * Robot lasers fire in the direction a
         * robot is facing. Their range has no
         * limit. Any robot in the line of sight is
         * shot. Robot lasers cannot fire through
         * walls or shoot more than one robot.
         * (Remember to take a SPAM damage
         * card for each laser that hits you
         */

        for (Player player : players) {
            shootRobotLaser(player.getRobot());
        }

    }

    /**
     * shoots laser of a robot
     *
     * @param robot the robot which will shoot
     */
    private void shootRobotLaser(Robot robot) {
        Position pos = robot.getPosition();
        boolean laserhit = false;
        List<BoardElement> elemetsOnPos;

        //TODO: not sure about which (X or Y) is width and which is length
        while (pos.getX() < gameMap.getWidth() && pos.getY() < gameMap.getLength()
                && !laserhit) {
            //as long as within the board, and laser still didnt hit any element
            if (hasLaserBlock(pos)) {
                //if cell has a robot or an antenna on it
                elemetsOnPos = gameMap.getElementsOnPos(pos);
                for (BoardElement element : elemetsOnPos) {
                    //robots on cell get damage, or laser stops
                    if (element instanceof Robot) {
                        laserHitRobot((Robot) element);
                        laserhit = true;
                    } else if (element instanceof Wall) {
                        //TODO: take direction of wall into consideration
                        laserhit = true;
                    }
                }
            } else {
                pos = getNextPos(pos, robot.getDirection());
            }
        }
    }

    private void activateEnergySpaces() {
        /**
         * When you end a register on an
         * energy space, if there is an energy
         * cube there, take it. If you end the
         * fifth register on an energy space,
         * take an energy cube from the
         * energy bank
         */

        /*
        JsonAdapter<Energy> energyJsonAdapter = moshi.adapter(Energy.class);
        Energy energy = new Energy(robot.getID(), count, source);
        broadcastMessage("Energy", energyJsonAdapter.toJson(energy));
        */
    }

    private void activateCheckpoints() {
        /**
         *  In order to reach
         * a checkpoint, you must be on it at
         * the end of a register, and you may
         * enter a checkpoint from any side.
         * After you reach a checkpoint, take
         * a checkpoint token, and place it
         * on your player mat to track your
         * progress in the race
         */
        List<BoardElement> checkpoints = getListOf("Checkpoint");
        for (BoardElement checkpoint : checkpoints) {
            //on each checkpoint, check robots on it
            CheckPoint cp = (CheckPoint) checkpoint;
            List<Robot> elements = gameMap.getRobotsOnPos(cp.getPosition());

            for (Robot robot : elements) {
                //foreach robot on checkpoint if the number of the checkpoint corresponds to the number of the next checkpoint then set to
                Player player = getPlayerByRobot(robot);
                CheckPoint checkpoint1 = (CheckPoint) checkpoint;
                checkpoint1.execute(player);
                //send checkpointreached protocoll message to every client
                JsonAdapter<CheckPointReached> checkPointReachedJsonAdapter = moshi.adapter(CheckPointReached.class);
                CheckPointReached checkPointReached = new CheckPointReached(robot.getID(), checkpoint1.getCount());
                broadcastMessage("CheckPointReached", checkPointReachedJsonAdapter.toJson(checkPointReached));

                if (player.getNextCheckPoint() > checkpoints.size()) {
                    logger.info("Player " + player.getID() + "-" + player.getName() + " has won.");
                    //if it was the last checkpoint, the player has won the game and the protocoll message gets sent
                    JsonAdapter<GameFinished> gameFinishedJsonAdapter = moshi.adapter(GameFinished.class);
                    GameFinished gameFinished = new GameFinished(robot.getID());
                    broadcastMessage("GameFinished", gameFinishedJsonAdapter.toJson(gameFinished));
                    return;
                }
            }
        }
    }

    /**
     * activates blue conveyor belts then green ones
     *
     * @throws ClassNotFoundException
     */
    private void activateConveyorBelts() throws ClassNotFoundException {
        //TODO: curved belts not taken into consideration
        //activate blue conveyor belts
        List<ConveyorBelt> conveyorbelts = getListOfBelts(2);
        for (ConveyorBelt belt : conveyorbelts) {
            if (robotOnElement(belt)) {
                //Assert that belt.getSpeed()== 2
                //get robots on belts position (usually just one robot)
                List<Robot> robotList = gameMap.getRobotsOnPos(belt.getPosition());
                belt.execute(robotList);

            }
        }
        //activate green conveyor belts
        conveyorbelts = getListOfBelts(1);
        for (ConveyorBelt belt : conveyorbelts) {
            if (robotOnElement(belt)) {
                //Assert that belt.getSpeed()== 1
                //get robots on belts position (usually just one robot)
                List<Robot> robotList = gameMap.getRobotsOnPos(belt.getPosition());
                belt.execute(robotList);
            }
        }
    }

    /**
     * checks if there are elements on a board element´s position
     *
     * @param element Board element to
     * @return true if robots are on that position
     */
    private boolean robotOnElement(BoardElement element) {
        List<BoardElement> elementsOnPos = gameMap.getElementsOnPos(element.getPosition());
        //check if list of elements has robot
        int i = 0;
        while (i < elementsOnPos.size()) {
            if (elementsOnPos.get(i).toString().equals("Robot")) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param type the type of board elements to return
     * @return a list of board elements of one specific type
     */
    private List<BoardElement> getListOf(String type) {
        List<BoardElement> listofobj = new ArrayList<>();
        for (BoardElement boardelement : boardElements) {
            if (boardelement.toString().equals(type)) {
                listofobj.add(boardelement);
            }
        }
        return listofobj;
    }

    /**
     * filters conveyorbelts from the board elements list
     *
     * @param speed type of conveyor belts to return (green 1, blue 2)
     * @return a list of conveyor belts of a specific type
     */
    private List<ConveyorBelt> getListOfBelts(int speed) {
        List<ConveyorBelt> listofobj = new ArrayList<>();
        for (BoardElement boardelement : boardElements) {
            if (boardelement instanceof ConveyorBelt &&
                    ((ConveyorBelt) boardelement).getSpeed() == speed) {
                listofobj.add((ConveyorBelt) boardelement);
            }
        }
        return listofobj;
    }

    /**
     * @return a list of robots that are still playing (not rebooting/disconnected)
     */
    private List<Robot> getActiveRobots() {
        ArrayList<Robot> active_robots = new ArrayList<>();
        for (Player player : players) {
            //TODO: add Player comparison based on ids
            if (rebooted_players.indexOf(player) != -1) {
                active_robots.add(player.getRobot());
            }
        }
        return active_robots;
    }

    public int getCurrent_register() {
        return current_register;
    }

    /**
     * gets a list of all the gears on the gameboard, checks if there are robots on them and activates the needed ones
     */
    private void activateGears() {

        List<BoardElement> gears = getListOf("Gear");
        //go through all the gears on the gameboard
        for (BoardElement gear : gears) {
            Gear g1 = (Gear) gear;
            //check if there is a robot on the specific gear
            if (robotOnElement(g1)) {
                //get a list of all the robots on that gear
                List<Robot> robotList = gameMap.getRobotsOnPos(g1.getPosition());
                //activate the gear (turn the robots in the specific direction)
                for (Robot curr : robotList) {
                    g1.execute(curr);
                    robotTurnedProtokoll(curr, g1.getOrientation()); //send protocoll message that robot has turned clockwise
                }
            }
        }
    }

    private void activatePushPanels() {

        List<BoardElement> pushpanels = getListOf("PushPanel");
        for (BoardElement pp : pushpanels) {
            //activate the PushPanels only in their assigned registers
            PushPanel p1 = (PushPanel) pp;
            if (p1.getRegisters().contains(current_register)) {
                //get a list of all robots targeted by this pushpanel
                List<Robot> robotList = gameMap.getRobotsOnPos(p1.getPosition());
                p1.execute(robotList);
            }
        }
    }

    private void activatePits() {
        List<BoardElement> pits = getListOf("Pit");

        List<BoardElement> respawns = getListOf("RestartPoint");
        Position newPos = new Position(0,0);


        for (BoardElement pit:
                pits) {

            for (Player player:
                    players) {
                if(!gameMap.getMapFields().get(pit.getPosition().getX()).get(pit.getPosition().getY()).getTypes().contains(player.getRobot())){

                    for (BoardElement respawn:
                           respawns) {

                        for (Player curr:
                                players) {
                            if(!gameMap.getMapFields().get(respawn.getPosition().getX()).get(respawn.getPosition().getY()).getTypes().contains(curr.getRobot())){
                                newPos.copy(respawn.getPosition());
                                break;
                            }

                        }


                    }


                    player.getRobot().reboot(TOP, newPos);
                    rebootPlayer(player);
                    break;
                }

            }


        }

        /*
        for (BoardElement pit : pits) {
            if (robotOnElement(pit)) {
                Pit pit1 = (Pit) pit;
                List<Robot> robotList = gameMap.getRobotsOnPos(pit1.getPosition());
                for (Robot curr : robotList) {
                    if (curr.getPosition().equals(pit1.getPosition())) {


                    }
                }
            }
        }

         */



    }

    public Player getNextPlayer() {
        return playing; //ToDo: change to next player
    }

    public void start() {

    }

    /**
     * @param robot this method is used every time a robots position is changed in any type of way
     *              sends a protocoll message from the server to every client
     *              ! for movement only - not turns!
     */
    public void robotMovedProtokoll(Robot robot) {
        JsonAdapter<Movement> movementJsonAdapter = moshi.adapter(Movement.class);
        Movement movement = new Movement(robot.getID(), robot.getPosition().getX(), robot.getPosition().getY());
        broadcastMessage("Movement", movementJsonAdapter.toJson(movement));
    }

    /**
     * @param robot
     * @param rotation this method is used to send a protocoll message from the Server to every client every time a robot turns
     *                 rotation can either be clockwise or counterclockwise
     */
    public void robotTurnedProtokoll(Robot robot, String rotation) {
        JsonAdapter<PlayerTurning> playerTurningJsonAdapter = moshi.adapter(PlayerTurning.class);
        PlayerTurning playerTurning = new PlayerTurning(robot.getID(), rotation);
        broadcastMessage("PlayerTurning", playerTurningJsonAdapter.toJson(playerTurning));
    }

    /**
     * @param robot
     * @param cards this method is used every time a player/robot takes damage and has to draw a Damagecard
     *              if he takes multiple Damagecards, only one message is sent, containing a list of all the Damagecards
     */
    public void drawDamageProtokoll(Robot robot, ArrayList<String> cards) {
        JsonAdapter<DrawDamage> drawDamageJsonAdapter = moshi.adapter(DrawDamage.class);
        DrawDamage drawDamage = new DrawDamage(robot.getID(), cards);
        broadcastMessage("DrawDamage", drawDamageJsonAdapter.toJson(drawDamage));
    }

    /*public void setPlayerValues() {
        for (ClientHandler client :
                ClientHandler.clients) {
            players.add(new Player(new Robot(ClientHandler.clients.indexOf(client))));
        }
    }*/

    public String getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(String currentMap) {
        Game.currentMap = currentMap;
    }

    public static int getMapSelectionPlayer() {
        return mapSelectionPlayer;
    }

    public static void setMapSelectionPlayer(int mapSelectionPlayer) {
        Game.mapSelectionPlayer = mapSelectionPlayer;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public int getPhase() {
        return phase;
    }

    public void addAI() {
        new AIClient(port, protocol);
    }


    public void initGameMap() {
        setUpBoard();
    }

    public boolean playersAreReady() {
        int i=0;
        while (i<players.size()){
            if(!players.get(i).isReady()){
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean isRunning() {
        return isRunning;
    }
}



