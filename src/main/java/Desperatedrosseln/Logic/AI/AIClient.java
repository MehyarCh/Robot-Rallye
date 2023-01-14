package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Json.utils.JsonDeserializer;
import Desperatedrosseln.Local.Controllers.MapController;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.BoardElement;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AIClient extends Thread {

    private final DataInputStream in;
    private final DataOutputStream out;
    Socket clientSocket;
    HashMap<String, Integer> localPlayerList = new HashMap<>();

    AI ai;
    public static ArrayList<String> aiNames;
    private int AI_ID;
    private String AIName = "-not specified-";
    private boolean hasStartpoint = false;
    private ArrayList<AIClient.Position> unavailableStartingPoints = new ArrayList<>();
    private String protocol;
    List<List<List<BoardElement>>> gameMap;

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
        Moshi moshi = new Moshi.Builder().build();
        if (message.startsWith("{\"messageType\":\"GameStarted\"")) {
            JsonDeserializer jsonDeserializer = new JsonDeserializer();
            ProtocolMessage<GameStarted> gameStartedProtocolMessage = jsonDeserializer.deserialize(message);
            GameStarted gameStarted = gameStartedProtocolMessage.getMessageBody();
            gameMap = gameStarted.getGameMap();

            JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
            System.out.println("AI got gamestarted msg !!!!!!!!!!!");
            Position startPos = newStartPos();

            sendMessage("SetStartingPoint", setStartingPointJsonAdapter.toJson(new SetStartingPoint(startPos.getX(), startPos.getY())));
            return;
        }


        Message msg;
        {
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            msg = messageJsonAdapter.fromJson(message);
        }
        System.out.println("AI:" + message);


        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
                sendMessage("HelloServer", helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", true, protocol)));
                break;
            case "Alive":
                sendMessage(msg.getMessageType(), msg.getMessageBody());
                //System.out.println("Alive check from server");
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
                sendMessage("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(AIName, AI_ID)));
                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());

                if (playerAdded.getClientID() == AI_ID) {                                      //TODO: Ready button?
                    sendChatMessage("Hello Everyone", -1);
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(true)));
                }
                break;

            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap selectMap = selectMapJsonAdapter.fromJson(msg.getMessageBody());
                String map = selectMap.getMaps().get(0);

                JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                sendMessage("MapSelected", mapSelectedJsonAdapter.toJson(new MapSelected(map)));
                //TODO: GUI map selection
                break;

            case "CurrentPlayer":
                JsonAdapter<CurrentPlayer> currentPlayerJsonAdapter = moshi.adapter(CurrentPlayer.class);
                ai.setCurrentPlayer(currentPlayerJsonAdapter.fromJson(msg.getMessageBody()).getClientID());
                break;
            case "ActivePhase":
                JsonAdapter<ActivePhase> activePhaseJsonAdapter = moshi.adapter(ActivePhase.class);
                ai.setActivePhase(activePhaseJsonAdapter.fromJson(msg.getMessageBody()).getPhase());
                break;
            case "GameStarted":


                break;

            case "StartingPointTaken":
                //ToDo:update the starting points

                JsonAdapter<StartingPointTaken> startingPointTakenJsonAdapter = moshi.adapter(StartingPointTaken.class);
                StartingPointTaken startingPointTaken = startingPointTakenJsonAdapter.fromJson(msg.getMessageBody());

                unavailableStartingPoints.add(new AIClient.Position(startingPointTaken.getX(), startingPointTaken.getY()));

                break;

            case "YourCards":
                JsonAdapter<YourCards> yourCardsJsonAdapter = moshi.adapter(YourCards.class);
                ai.setCardsInHand(yourCardsJsonAdapter.fromJson(msg.getMessageBody()).getCardsInHand());
                JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);

                for (int i = 0; i < 5; ++i) {
                    sendMessage("SelectedCard", selectedCardJsonAdapter.toJson(new SelectedCard(ai.getRandomCard(), i)));
                }

                break;
            case "Error":
                break;
        }


    }

    private Position newStartPos() {

        for (int i = 0; i < gameMap.size(); i++) {
            for (int j = 0; j < gameMap.get(i).size(); j++) {
                for (BoardElement element :
                        gameMap.get(i).get(j)) {
                    if (element.getType().equals("StartPoint")) {

                        boolean isPosTaken = false;

                        Position currPos = new Position(i,j);
                        if(!unavailableStartingPoints.isEmpty()){
                            for(Position pos: unavailableStartingPoints){
                                if(pos.isEqual(currPos)){
                                    isPosTaken = true;
                                }
                            }
                        }

                        if(!isPosTaken){
                            sendChatMessage("got my starting pos ("+i+","+j+").",-1);
                            return currPos;
                        }

                    }
                }
            }
        }

        System.out.println("----------couldnt find Pos");
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
