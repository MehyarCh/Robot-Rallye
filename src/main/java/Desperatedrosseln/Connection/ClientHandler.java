package Desperatedrosseln.Connection;

import Desperatedrosseln.Json.utils.JsonMapReader;
import Desperatedrosseln.Json.utils.JsonSerializer;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Local.Protocols.Error;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Game;
import Desperatedrosseln.Logic.Player;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler implements Runnable {
    private String protocol;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String clientName;
    private Player player;
    Timer timer = new Timer();
    public static ArrayList<ClientHandler> clients = new ArrayList<>();
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
    private Game game;

    private int clientID;
    public boolean isAI = false;

    public Player getPlayer() {
        return player;
    }
    private List<String> maps = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger();


    public ClientHandler(Socket socket, Game game, String protocol) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.game = game;
            this.protocol = protocol;
            initializeMaps();
            clients.add(this);

            sendCurrentPlayers();

            JsonAdapter<HelloClient> helloClientJsonAdapter = moshi.adapter(HelloClient.class);
            sendMessage("HelloClient", helloClientJsonAdapter.toJson(new HelloClient(protocol)));//send HelloClient

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCurrentPlayers() {
        JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
        for (Player player :
                game.getPlayers()) {
            if (!player.equals(this.player)) {
                sendMessage("PlayerAdded", playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())));
            }
        }
    }


    public void sendErrorMessage() {
        JsonAdapter<Desperatedrosseln.Local.Protocols.Error> errorJsonAdapter = moshi.adapter(Desperatedrosseln.Local.Protocols.Error.class);
        sendMessage("Error", errorJsonAdapter.toJson(new Error()));
    }

    public void checkCommands(String msg) throws IOException, ClassNotFoundException {


        if (msg == null) {
            return;
        }

        Message message = messageJsonAdapter.fromJson(msg);

        if (!message.getMessageType().equals("Alive")) {
            if (isAI) {
                logger.info("AI-message" + message.getMessageType() + ": " + message.getMessageBody());
            } else {
                logger.info(message.getMessageType() + ": " + message.getMessageBody());
            }

        }

        switch (message.getMessageType()) {

            case "HelloServer":
                JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
                clientID = clients.indexOf(this) + 1;
                HelloServer helloServer = helloServerJsonAdapter.fromJson(message.getMessageBody());

                if (helloServer.getProtocol().equals(this.protocol)) {
                    isAI = helloServer.isAI;
                    JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                    sendMessage("Welcome", welcomeJsonAdapter.toJson(new Welcome(clientID)));
                    logger.info(clientID + " connected");
                } else {
                    logger.debug("invalid protocol");
                    sendErrorMessage();
                }
                break;

            case "Alive":
                break;

            case "PlayerValues":
                JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);

                PlayerValues playerValues = playerValuesJsonAdapter.fromJson(message.getMessageBody());

                boolean robotTaken = false;
                if (!game.getPlayers().isEmpty()) {
                    for (Player player :
                            game.getPlayers()) {
                        if (player.getRobot().getID() == playerValues.getFigure()) {
                            sendErrorMessage();
                            robotTaken = true;
                        }
                    }
                    if (!robotTaken) {
                        setPlayerValues(playerValues);
                    }

                } else {
                    setPlayerValues(playerValues);
                }


                break;
            case "SetStatus":

                JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                boolean status = setStatusJsonAdapter.fromJson(message.getMessageBody()).isReady();
                player.setReady(status);

                if (!isAI && Game.mapSelectionPlayer == -1 && status) {
                    Game.mapSelectionPlayer = player.getID();
                } else if (Game.mapSelectionPlayer == player.getID() && !status) {
                    Game.mapSelectionPlayer = -1;
                }

                JsonAdapter<PlayerStatus> playerStatusJsonAdapter = moshi.adapter(PlayerStatus.class);
                PlayerStatus playerStatus = new PlayerStatus(player.getID(), status);
                broadcastMessage("PlayerStatus", playerStatusJsonAdapter.toJson(playerStatus));

                //if all players are ready, the first to be ready chooses the map
                if (game.playersAreReady() && Game.getPlayers().size() > 1) {
                    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
                    JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);

                    for (ClientHandler clientHandler : clients) {
                        if (clientHandler.getClientID() == Game.mapSelectionPlayer) {
                            clientHandler.sendMessage("SelectMap", selectMapJsonAdapter.toJson(new SelectMap(maps)));
                        }
                    }

                }

                break;
            case "MapSelected":
                JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                Game.setCurrentMap(mapSelectedJsonAdapter.fromJson(message.getMessageBody()).getMap());

                //ToDO send GameStarted with Map
                List<List<List<BoardElement>>> gameMap = new JsonMapReader().readMapFromJson(game.getCurrentMap());

                if (clients.size() > 1) {

                    ProtocolMessage<GameStarted> gameStartedProtocolMessage = new ProtocolMessage<>("GameStarted", new GameStarted(new JsonMapReader().readMapFromJson(game.getCurrentMap())));
                    String jsonGameStarted = new JsonSerializer().serialize(gameStartedProtocolMessage);

                    for (ClientHandler client :
                            clients) {
                        try {
                            client.getOut().writeUTF(jsonGameStarted);
                            client.getOut().flush();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                }
                break;
            case "SendChat":

                JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
                SendChat sendChat = sendChatJsonAdapter.fromJson(message.getMessageBody());
                JsonAdapter<ReceivedChat> receivedChatJsonAdapter = moshi.adapter(ReceivedChat.class);
                if (sendChat.getTo() == -1) {
                    broadcastMessage("ReceivedChat", receivedChatJsonAdapter.toJson(new ReceivedChat(sendChat.getMessage(), clientID, false)));
                } else {
                    for (ClientHandler client :
                            clients) {
                        if (client.getClientID() == sendChat.getTo()) {
                            client.sendMessage("ReceivedChat", receivedChatJsonAdapter.toJson(new ReceivedChat(sendChat.getMessage(), clientID, true)));
                        }
                    }
                }
                break;
            case "PlayCard":
                JsonAdapter<CardPlayed> cardPlayedJsonAdapter = moshi.adapter(CardPlayed.class);
                CardPlayed cardPlayed = new CardPlayed(player.getID(), cardPlayedJsonAdapter.fromJson(message.getMessageBody()).getCard()); //add clientID and the card that was played
                broadcastMessage("CardPlayed", cardPlayedJsonAdapter.toJson(cardPlayed)); //send CardPlayed message to every client
            default:
                broadcastMessage(" ", "SERVER BRO");

            case "addAI":
                game.addAI();
                break;
            case "SetStartingPoint":
                JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
                SetStartingPoint setStartingPoint = setStartingPointJsonAdapter.fromJson(message.getMessageBody());
                game.initGameMap();
                game.placeRobot(player, setStartingPoint.getX(), setStartingPoint.getY());
                ++game.startingPositionsChosen;

                if ( game.startingPositionsChosen == clients.size() && !game.isRunning()) {
                    game.runStep();
                }

                //ToDo one Robot one tile

                JsonAdapter<StartingPointTaken> startingPointTakenJsonAdapter = moshi.adapter(StartingPointTaken.class);
                broadcastMessage("StartingPointTaken", startingPointTakenJsonAdapter.toJson(new StartingPointTaken(setStartingPoint.getX(), setStartingPoint.getY(), clientID)));
                break;
            case "SelectedCard":
                JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);
                SelectedCard selectedCard = selectedCardJsonAdapter.fromJson(message.getMessageBody());
                //add this to the players register in Game as well
                logger.debug(selectedCard.getCard(), selectedCard.getRegister());
                player.addToRegister(selectedCard.getCard(), selectedCard.getRegister());

                JsonAdapter<CardSelected> cardSelectedJsonAdapter = moshi.adapter(CardSelected.class);
                if (selectedCard.getCard().equals("null")) {
                    broadcastMessage("CardSelected", cardSelectedJsonAdapter.toJson(new CardSelected(clientID, selectedCard.getRegister(), false)));
                    //replace the card through "null" in the register and replace the null spot in the hand with the card that got put back
                    player.addToRegister(selectedCard.getCard(), selectedCard.getRegister());
                } else {
                    broadcastMessage("CardSelected", cardSelectedJsonAdapter.toJson(new CardSelected(clientID, selectedCard.getRegister(), true)));
                    logger.trace(game.selectionFinished());
                    if(game.selectionFinished()) {
                        JsonAdapter<ActivePhase> activePhaseJsonAdapter = moshi.adapter(ActivePhase.class);
                        ActivePhase activePhase3 = new ActivePhase(3);
                        broadcastMessage("ActivePhase", activePhaseJsonAdapter.toJson(activePhase3));
                        game.runStep();
                        String hand = "{";
                        for(ClientHandler client: clients){
                            hand += client.getClientID();
                            for(int i = 0; i<5; i++) {
                                hand = hand + " " + client.getPlayer().getRegisters()[i] + ",";
                            }
                            hand = hand + "}";
                            logger.warn(hand);
                            hand = "{";
                        }
                    }
                }
                break;
            case "Logout":
                if (player.getID() == Game.mapSelectionPlayer) {
                    Game.mapSelectionPlayer = -1;
                }
                closeAll(this.socket, this.in, this.out);
                //problem with alive message as soon as socket gets closed; -> ToDo: disconnect client properly (multiple errors)
                break;
        }
    }



    private void setPlayerValues(PlayerValues playerValues) {
        player = new Player();
        player.setID(clientID);
        player.setName(playerValues.getName());
        player.setRobot(new Robot(playerValues.getFigure()));
        this.clientName = playerValues.getName();
        game.getPlayers().add(player);
        sendCurrentPlayers();

        JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
        broadcastMessage("PlayerAdded", playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())));
    }

    public int getClientID() {
        return clientID;
    }


    public void sendMessage(String type,String json) {
        try {
            out.writeUTF(messageJsonAdapter.toJson(new Message(type,json)));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // messageType: 0 = broadcast to all others, 1 = to everyone
    public void broadcastMessage(String type,String json) {
        for (ClientHandler client : clients) {
            client.sendMessage(type,json);
        }
    }


    public void closeAll(Socket socket, DataInputStream in, DataOutputStream out) {
        removeClientHandler();
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClientHandler() {
        clients.remove(this);
        //String logout = "User " + clientName + " has left the Chat";
        //broadcastMessage(logout);
    }

    @Override
    public void run() {

        String message;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                JsonAdapter<Alive> aliveJsonAdapter = moshi.adapter(Alive.class);
                sendMessage("Alive", aliveJsonAdapter.toJson(new Alive())); //after 5s alive message gets sent to the client.
            }
        }, 5 * 1000, 5 * 1000); //5 seconds


        while (!socket.isClosed()) {
            try {
                message = in.readUTF();
                checkCommands(message);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString() {
        return clientName + "| ID " + clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public DataOutputStream getOut() {
        return out;
    }
    public void initializeMaps(){
        maps.add("DizzyHighway");
        maps.add("ExtraCrispy");
        maps.add("LostBearings");
        maps.add("DeathTrap");
    }
}
