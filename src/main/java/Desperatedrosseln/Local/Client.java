package Desperatedrosseln.Local;


import Desperatedrosseln.Json.utils.JsonDeserializer;
import Desperatedrosseln.Local.Controllers.LobbyController;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.Robot;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;


import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Runnable {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private int clientID;

    private List<String> cardsInHand;
    private HashMap<Integer, Integer> playersWithRobots = new HashMap<>();
    private HashMap<String, Integer> localPlayerList = new HashMap<>();
    private MainController mainController;
    private String protocol = "Version 0.1";
    ArrayList<Integer> robotIDs = new ArrayList<>();
    private String clientName;

    public boolean isMainSceneStarted = false;

    private boolean isMyTurn = false;

    public boolean lobbyControllerInitialized = false;
    private LobbyController lobbyController;
    private List<String> upgrades = new ArrayList<>();
    public int energyReserve;
    public boolean isGotSentMaps() {
        return gotSentMaps;
    }

    public void setGotSentMaps(boolean gotSentMaps) {
        this.gotSentMaps = gotSentMaps;
    }

    private boolean gotSentMaps = false;

    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    public List<Integer> takenRobots = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(MainController.class);

    public void setLobbyControllerInitialized(boolean lobbyControllerInitialized) {
        this.lobbyControllerInitialized = lobbyControllerInitialized;
    }

    public Client() {
        try {
            clientSocket = new Socket("localhost", 3000);
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new Thread(this).start();
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


    public void sendHelloServer() {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
        String helloServer = helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", false, protocol));
        sendMessage("HelloServer", helloServer);
    }

    public void sendPlayerValues(int robotID) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);
        sendMessage("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(clientName, robotID)));
    }


    /**
     * @param message = the incoming protocol message
     * this method checks the type of each received message and continues according to the protocol
     */
    private void checkProtocolMessage(String message) throws IOException {
        //TODO: Logs
        if (message.startsWith("{\"messageType\":\"GameStarted\"")) {
            Stage stage = lobbyController.getStage();
            mainController.startMainScene(stage, lobbyController.getSelectedRobot());
            JsonDeserializer jsonDeserializer = new JsonDeserializer();
            ProtocolMessage<GameStarted> gameStartedProtocolMessage = jsonDeserializer.deserialize(message);
            GameStarted gameStarted = gameStartedProtocolMessage.getMessageBody();
            Desperatedrosseln.Logic.Elements.Map map = new Desperatedrosseln.Logic.Elements.Map(mainController.getMapController().convertMap(gameStarted.getGameMap()));
            mainController.getMapController().setMapAsList(gameStarted.getGameMap());
            mainController.getMapController().setMap(map);
            mainController.getMapController().showMap();
            mainController.getMapController().setMap(gameStartedProtocolMessage.getMessageBody().getGameMap());
            //startStartPointSelectionTimer();
            return;
        }
        Moshi moshi = new Moshi.Builder().build();
        Message msg;
        {
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            msg = messageJsonAdapter.fromJson(message);
        }

        if (!msg.getMessageType().equals("Alive")) {
            logger.trace(msg.getMessageType() + ": " + msg.getMessageBody());
        }

        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                //sendHelloServer();


                break;
            case "Alive":
                sendMessage(msg.getMessageType(), msg.getMessageBody());
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.clientID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                //sendPlayerValues(clientID);
                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());
                if (!robotIDs.contains(playerAdded.getFigure())) {
                    //add robotID to the list of taken robots
                    robotIDs.add(playerAdded.getFigure());

                    mapRobotToClient(playerAdded.getClientID(), playerAdded.getFigure());
                    //disable all other robot choice buttons in the GUI if it is already taken
                    //check if the message is about another client and disable the specific robot-icon that he chose in this GUI
                    if (lobbyControllerInitialized && playerAdded.getClientID() != this.clientID){
                        lobbyController.disableRobotIcon(playerAdded.getFigure());
                    }
                }
                break;

            case "PlayerStatus":
                break;
            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap selectMap = selectMapJsonAdapter.fromJson(msg.getMessageBody());

                lobbyController.addMapsToChoice(selectMap.getMaps());
                lobbyController.canChooseMap();

                break;
            case "ReceivedChat":
                JsonAdapter<ReceivedChat> receivedChatJsonAdapter = moshi.adapter(ReceivedChat.class);
                ReceivedChat receivedChat = receivedChatJsonAdapter.fromJson(msg.getMessageBody());
                if (isMainSceneStarted) {
                    if (receivedChat.isPrivate()) {
                        mainController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": (Whispered)" + receivedChat.getMessage());
                    } else {
                        mainController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": " + receivedChat.getMessage());
                    }
                } else {
                    if (receivedChat.isPrivate()) {
                        lobbyController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": (Whispered)" + receivedChat.getMessage());
                    } else {
                        lobbyController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": " + receivedChat.getMessage());
                    }
                }
                break;
            case "GameStarted":
                //see above
                break;

            case "CardPlayed":
                break;
            case "StartingPointTaken":
                JsonAdapter<StartingPointTaken> startingPointTakenJsonAdapter = moshi.adapter(StartingPointTaken.class);
                StartingPointTaken startingPointTaken = startingPointTakenJsonAdapter.fromJson(msg.getMessageBody());
                mainController.getMapController().addUnavailablePosition(startingPointTaken.getX(), startingPointTaken.getY());
                mainController.getMapController().addRobotToUI(playersWithRobots.get(startingPointTaken.getClientID()), startingPointTaken.getX(), startingPointTaken.getY());
                break;
            case "YourCards":
                JsonAdapter<YourCards> yourCardsJsonAdapter = moshi.adapter(YourCards.class);
                YourCards yourCards = yourCardsJsonAdapter.fromJson(msg.getMessageBody());

                cardsInHand = yourCards.getCardsInHand();
                mainController.fillHand();
                mainController.updateCardImages();
                mainController.initRegisterValues();
                mainController.cardClick();
                startCardSelectionTimer();
                break;
            case "CurrentPlayer":
                JsonAdapter<CurrentPlayer> currentPlayerJsonAdapter = moshi.adapter(CurrentPlayer.class);
                CurrentPlayer currentPlayer = currentPlayerJsonAdapter.fromJson(msg.getMessageBody());
                if (currentPlayer.getClientID() == this.clientID) {
                    isMyTurn = true;
                    //ToDo: maybe add here PlayCard protocoll -> get the card in the current register and send it to the server via PlayCard
                } else if (currentPlayer.getClientID() == this.clientID) {
                    isMyTurn = false;
                }
                break;
            case "Movement":
                JsonAdapter<Movement> movementJsonAdapter = moshi.adapter(Movement.class);
                Movement movement = movementJsonAdapter.fromJson(msg.getMessageBody());
                mainController.getMapController().move(playersWithRobots.get(movement.getClientID()),
                        movement.getX(), movement.getY());
                break;
            case "PlayerTurning":
                JsonAdapter<PlayerTurning> playerTurningJsonAdapter = moshi.adapter(PlayerTurning.class);
                PlayerTurning playerTurning = playerTurningJsonAdapter.fromJson(msg.getMessageBody());
                mainController.getMapController().rotateRobot(playerTurning.getClientID(), playerTurning.getRotation());
                break;
            case "ExchangeShop":
                JsonAdapter<ExchangeShop> exchangeShopJsonAdapter = moshi.adapter(ExchangeShop.class);
                ExchangeShop exchangeShop = exchangeShopJsonAdapter.fromJson(msg.getMessageBody());
                List<String> shopCards = exchangeShop.getCards();
                mainController.exchangeShop(shopCards);
                Collections.shuffle(shopCards);
                JsonAdapter<BuyUpgrade> buyUpgradeJsonAdapter = moshi.adapter(BuyUpgrade.class);
                //sendMessage("BuyUpgrade",buyUpgradeJsonAdapter.toJson(new BuyUpgrade(true,shopCards.get(0))));
                break;
            case "RefillShop":
                JsonAdapter<RefillShop> refillShopJsonAdapter = moshi.adapter(RefillShop.class);
                RefillShop refillShop = refillShopJsonAdapter.fromJson(msg.getMessageBody());
                List<String> refillShopCards = refillShop.getCards();
                mainController.exchangeShop(refillShopCards);
                for (String c:
                     refillShopCards) {
                    System.out.print("_"+c);
                }
                Collections.shuffle(refillShopCards);
                JsonAdapter<BuyUpgrade> buyUpgradeJsonAdapter1 = moshi.adapter(BuyUpgrade.class);
                //sendMessage("BuyUpgrade",buyUpgradeJsonAdapter1.toJson(new BuyUpgrade(true,refillShopCards.get(0))));
                break;
            case "UpgradeBought":
                JsonAdapter<UpgradeBought> upgradeBoughtJsonAdapter = moshi.adapter(UpgradeBought.class);
                UpgradeBought upgradeBought = upgradeBoughtJsonAdapter.fromJson(msg.getMessageBody());

                if(upgradeBought.getClientID() == clientID){
                    String upgrade = upgradeBought.getCard();
                    upgrades.add(upgrade);
                }

                break;
            case "Energy":
                JsonAdapter<Energy> energyJsonAdapter = moshi.adapter(Energy.class);
                Energy energy = energyJsonAdapter.fromJson(msg.getMessageBody());
                energyReserve = energy.getCount();
                break;
            case "Error":
                if (mainController != null) {
                    mainController.addChatMessage("Error message from Server for ");
                }
                break;
        }
    }


    private void startStartPointSelectionTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mainController.getMapController().hasStartpoint) {
                    mainController.getMapController().runAutoStartPointSelection();
                    sendChatMessage("start point selected", -1);
                }
            }
        }, 30 * 1000);
    }

    private void startCardSelectionTimer() {
        Timer timer = new Timer();
        logger.info("Timer started for Card selection");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mainController.isProgrammingDone) {
                    mainController.sendRandomCards();
                    sendChatMessage("random cards sent", -1);
                }
            }
        }, 30 * 1000);
    }

    private String getPlayerName(int from) {
        for (String key :
                localPlayerList.keySet()) {
            if (localPlayerList.get(key) == from) {
                return key;
            }
        }
        return null;
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

    @Override
    public void run() {
        listener();
    }

    private void listener() {
        String message;
        try {
            while (!clientSocket.isClosed()) {
                message = in.readUTF();
                checkProtocolMessage(message);
            }
        } catch (IOException e) {
            //logOut();
        }
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public DataInputStream getInputStr() {
        return in;
    }

    public DataOutputStream getOutputStr() {
        return out;
    }

    public String getName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        logger.info("Setting local client name to " + clientName);
        this.clientName = clientName;
    }

    public void sendChatMessage(String message, int to) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        if (message.startsWith("/")) {          //TODO

            String[] messageParts = message.split(" ", 3);


            if (message.startsWith("/dm")) {
                if (messageParts.length < 3) {
                    //lobbyController.addChatMessage("Please complete the  command.");
                    mainController.addChatMessage("Please complete the  command.");
                    return;
                }
                if (messageParts[1].equals(this.clientName)) {
                    //lobbyController.addChatMessage("Please complete the  command.");
                    mainController.addChatMessage("You cannot send yourself private Messages, it is weird. Just think and talk to yourself.");
                    return;
                }
                if (localPlayerList.containsKey(messageParts[1])) {
                    sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(messageParts[2], localPlayerList.get(messageParts[1]))));
                } else {
                    //lobbyController.addChatMessage("Please complete the  command.");
                    mainController.addChatMessage("/dm did not work. Reason: invalid player name.");
                }
            } else if (message.startsWith("/addAI")) {
                sendMessage("addAI", "");
            } else if (message.startsWith("/dc")) {
                //disconnect the client from the server ->  closeAll in Clienthandler ToDo: fix this
                this.logOut();
                sendMessage("Logout", "");
            }


        } else {
            sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(message, -1)));
        }
    }

    public List<String> getCardsInHand() {
        return cardsInHand;
    }

    public ArrayList<Integer> getRobotIDs() {
        return robotIDs;
    }

    public void setLobbyController(LobbyController lobbyController) {
        this.lobbyController = lobbyController;
    }

    public void mapRobotToClient(int clientID, int robotID) {
        playersWithRobots.put(clientID, robotID);
    }
}

