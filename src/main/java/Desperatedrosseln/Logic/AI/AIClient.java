package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Json.utils.JsonDeserializer;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Game;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.jar.Attributes;


/**
 * @author Rishabh
 *
 * psuedo clients
 *
 */

public class AIClient extends Thread {

    private final DataInputStream in;
    private final DataOutputStream out;
    Socket clientSocket;
    HashMap<String, Integer> localPlayerList = new HashMap<>();
    ArrayList<Integer> robotIDs = new ArrayList<>();
    AI ai;
    public static ArrayList<String> aiNames;
    private int AI_ID;
    private String AIName = "-not specified-";
    private boolean hasStartPoint = false;
    private ArrayList<AIClient.Position> unavailableStartingPoints = new ArrayList<>();
    private String protocol;
    List<List<List<BoardElement>>> gameMap;
    private int robotID;
    private HashMap<Integer, Robot> players = new HashMap<>();
    List<String> upgrades = new ArrayList<>();
    private boolean memorySwapping;

    private static final Logger logger = LogManager.getLogger();
    private int energyReserve = 5;
    private ArrayList<String> regCards;
    /**
     * @author Rishabh
     *
     */
    public class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public boolean isEqual(Position position) {
            return x == position.getX() && y == position.getY();
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    public AIClient(int port, String protocol) {

        this.protocol = protocol;

        try {
            clientSocket = new Socket("localhost", port);
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(this).start();
    }

    public void logOut() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String type, String body) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);

        try {
            out.writeUTF(messageJsonAdapter.toJson(new Message(type, body)));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkProtocolMessage(String message) throws IOException {
        //TODO: Logs
        if (message.equals("{\"messageBody\":\"{}\",\"messageType\":\"Alive\"}")) {
            return;
        }
        Moshi moshi = new Moshi.Builder().build();
        if (message.startsWith("{\"messageType\":\"GameStarted\"")) {
            JsonDeserializer jsonDeserializer = new JsonDeserializer();
            ProtocolMessage<GameStarted> gameStartedProtocolMessage = jsonDeserializer.deserialize(message);
            GameStarted gameStarted = gameStartedProtocolMessage.getMessageBody();
            gameMap = gameStarted.getGameMap();
            ai.setGameMap(gameMap);
            return;
        }


        Message msg;
        {
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            msg = messageJsonAdapter.fromJson(message);
        }
        if (msg.getMessageType().equals("Alive")) {
            return;
        }
        System.out.println(AIName + "(AI)" + msg.getMessageType() + ": " + msg.getMessageBody());
        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
                sendMessage("HelloServer", helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", true, protocol)));
                break;
            case "Alive":
                sendMessage(msg.getMessageType(), msg.getMessageBody());
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.AI_ID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                ai = new AI(AI_ID);

                if (aiNames == null) {
                    aiNames = new ArrayList<>();
                    AIName = "GigaChad";
                } else {
                    switch (aiNames.size()) {
                        case 1:
                            AIName = "The Rizzler";
                            break;
                        default:
                            AIName = "noob " + (aiNames.size() - 1);
                    }
                }
                aiNames.add(AIName);
                JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);          //TODO:replace ClientID with player figure!!!!!


                for (int i = 1; i <= 6; i++) {
                    if (!robotIDs.contains(i)) {
                        robotID = i;
                        sendMessage("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(AIName, robotID)));
                        break;
                    }
                }


                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());
                robotIDs.add(playerAdded.getFigure());
                players.put(playerAdded.getClientID(), new Robot(playerAdded.getFigure()));
                if (playerAdded.getClientID() == AI_ID) {                                      //TODO: Ready button?
                    sendChatMessage("Hello Everyone", -1);
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(true)));
                }
                break;

            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap selectMap = selectMapJsonAdapter.fromJson(msg.getMessageBody());

                int index = (int) (Math.random() * selectMap.getMaps().size());

                String map = selectMap.getMaps().get(index % selectMap.getMaps().size());

                JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                sendMessage("MapSelected", mapSelectedJsonAdapter.toJson(new MapSelected(map)));
                //TODO: GUI map selection
                break;

            case "CurrentPlayer":
                JsonAdapter<CurrentPlayer> currentPlayerJsonAdapter = moshi.adapter(CurrentPlayer.class);
                CurrentPlayer currentPlayer = currentPlayerJsonAdapter.fromJson(msg.getMessageBody());
                System.out.println("Current player's ID: " + currentPlayer.getClientID());

                ai.setCurrentPlayer(currentPlayer.getClientID());

                if (AI_ID == ai.getCurrentPlayer()) {
                    JsonAdapter<PlayCard> playCardJsonAdapter = moshi.adapter(PlayCard.class);
                    PlayCard playCard = new PlayCard(regCards.get(0));
                    regCards.remove(0);
                    sendMessage("PlayCard", playCardJsonAdapter.toJson(playCard));
                }
                break;
            case "ActivePhase":
                JsonAdapter<ActivePhase> activePhaseJsonAdapter = moshi.adapter(ActivePhase.class);
                ai.setActivePhase(activePhaseJsonAdapter.fromJson(msg.getMessageBody()).getPhase());
                break;
            case "ExchangeShop":
                JsonAdapter<ExchangeShop> exchangeShopJsonAdapter = moshi.adapter(ExchangeShop.class);
                ExchangeShop exchangeShop = exchangeShopJsonAdapter.fromJson(msg.getMessageBody());
                List<String> shopCards = exchangeShop.getCards();
                //ToDo: Card Selection
                Collections.shuffle(shopCards);
                JsonAdapter<BuyUpgrade> buyUpgradeJsonAdapter = moshi.adapter(BuyUpgrade.class);
                for (String upgradeCard :
                        shopCards) {
                    int cost = getUpgradeCardCost(upgradeCard);
                    if (cost <= energyReserve) {
                        energyReserve -= cost;
                        sendMessage("BuyUpgrade", buyUpgradeJsonAdapter.toJson(new BuyUpgrade(true, upgradeCard)));
                        break;
                    }

                }
                break;
            case "RefillShop":
                JsonAdapter<RefillShop> refillShopJsonAdapter = moshi.adapter(RefillShop.class);
                RefillShop refillShop = refillShopJsonAdapter.fromJson(msg.getMessageBody());
                List<String> refillShopCards = refillShop.getCards();
                System.out.print(AIName + ":");
                for (String card :
                        refillShopCards) {
                    System.out.print("--" + card);
                }

                //ToDo: Card Selection
                Collections.shuffle(refillShopCards);
                JsonAdapter<BuyUpgrade> buyUpgradeJsonAdapter1 = moshi.adapter(BuyUpgrade.class);
                for (String upgradeCard :
                        refillShopCards) {
                    int cost = getUpgradeCardCost(upgradeCard);
                    if (cost <= energyReserve) {
                        energyReserve -= cost;
                        sendMessage("BuyUpgrade", buyUpgradeJsonAdapter1.toJson(new BuyUpgrade(true, upgradeCard)));
                        return;
                    }

                }
                sendMessage("BuyUpgrade", buyUpgradeJsonAdapter1.toJson(new BuyUpgrade(false, "")));

                break;
            case "UpgradeBought":
                JsonAdapter<UpgradeBought> upgradeBoughtJsonAdapter = moshi.adapter(UpgradeBought.class);
                UpgradeBought upgradeBought = upgradeBoughtJsonAdapter.fromJson(msg.getMessageBody());

                if (upgradeBought.getClientID() == AI_ID) {
                    String upgrade = upgradeBought.getCard();
                    upgrades.add(upgrade);
                }
                break;

            case "StartingPointTaken":
                //ToDo:update the starting points

                JsonAdapter<StartingPointTaken> startingPointTakenJsonAdapter = moshi.adapter(StartingPointTaken.class);
                StartingPointTaken startingPointTaken = startingPointTakenJsonAdapter.fromJson(msg.getMessageBody());

                unavailableStartingPoints.add(new AIClient.Position(startingPointTaken.getX(), startingPointTaken.getY()));
                JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
                if (startingPointTaken.getClientID() == AI_ID - 1) {
                    if (!hasStartPoint) {
                        Position startPos = newStartPos();
                        hasStartPoint = true;
                        sendMessage("SetStartingPoint", setStartingPointJsonAdapter.toJson(new SetStartingPoint(startPos.getX(), startPos.getY())));
                        ai.robot = players.get(AI_ID);

                    }
                }
                ai.placeRobot(new Position(startingPointTaken.getX(), startingPointTaken.getY()), players.get(startingPointTaken.getClientID()));
                break;

            case "YourCards":
                JsonAdapter<YourCards> yourCardsJsonAdapter = moshi.adapter(YourCards.class);
                ai.setCardsInHand(yourCardsJsonAdapter.fromJson(msg.getMessageBody()).getCardsInHand());
                JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);

                regCards = ai.selectRegisterCards();

                //sendChatMessage(ai.getTinyPath(),-1);

                if (memorySwapping) {

                    for (int i = 0; i < 3; i++) {

                    }

                    memorySwapping = false;
                }

                for (int i = 0; i < 5; ++i) {
                    if (i < regCards.size()) {
                        sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(regCards.get(i), i)));
                    } else {
                        logger.info("AIClient setting random cards");
                        sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(ai.getRandomCard(), i)));
                    }

                }
                break;
            case "Energy":
                JsonAdapter<Energy> energyJsonAdapter = moshi.adapter(Energy.class);
                Energy energy = energyJsonAdapter.fromJson(msg.getMessageBody());
                if (energy.getClientID() == AI_ID) {
                    energyReserve = energy.getCount();
                }
                break;
            case "Movement":
                JsonAdapter<Movement> movementJsonAdapter = moshi.adapter(Movement.class);
                Movement movement = movementJsonAdapter.fromJson(msg.getMessageBody());
                ai.updateRobotPosition(players.get(movement.getClientID()), movement.getX(), movement.getY());
                System.out.println(AIName + "----" + ai.getTinyPath());
                break;
            case "CheckPointReached":
                JsonAdapter<CheckPointReached> checkPointReachedJsonAdapter = moshi.adapter(CheckPointReached.class);
                CheckPointReached checkPointReached = checkPointReachedJsonAdapter.fromJson(msg.getMessageBody());
                if (checkPointReached.getClientID() == AI_ID) {
                    ai.currentGoal = checkPointReached.getNumber() + 1;
                }
                break;
            case "Error":
                break;
        }


    }

    private int getUpgradeCardCost(String upgradeCard) {
        switch (upgradeCard) {
            case "SpamBlocker":
            case "AdminPrivilege":
                return 3;
            case "MemorySwap":
                return 1;
            case "RearLaser":
                return 2;
        }
        return 9999;
    }

    private Position newStartPos() {

        for (int i = 0; i < gameMap.size(); i++) {
            for (int j = 0; j < gameMap.get(i).size(); j++) {
                for (BoardElement element :
                        gameMap.get(i).get(j)) {
                    if (element.getType().equals("StartPoint")) {

                        boolean isPosTaken = false;

                        Position currPos = new Position(i, j);
                        if (!unavailableStartingPoints.isEmpty()) {
                            for (Position pos : unavailableStartingPoints) {
                                if (pos.isEqual(currPos)) {
                                    isPosTaken = true;
                                }
                            }
                        }

                        if (!isPosTaken) {
                            return currPos;
                        }

                    }
                }
            }
        }


        return null;
    }

    @Override
    public void run() {
        listener();
    }

    private void listener() {
        String message;
        try {
            while (!clientSocket.isClosed()) {
                message = in.readUTF();
                if (message != null) {

                    checkProtocolMessage(message);

                } else {
                    break;
                }
            }
        } catch (IOException e) {
            logOut();
        }
    }


    public void sendChatMessage(String message, int to) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        if (message.startsWith("/dm")) {          //TODO

            String[] messageParts = message.split(" ", 3);

            if (localPlayerList.containsKey(messageParts[1])) {
                sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(messageParts[2], localPlayerList.get(messageParts[1]))));
            }

        } else {
            sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(message, -1)));
        }
    }
}
