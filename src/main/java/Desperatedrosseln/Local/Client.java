package Desperatedrosseln.Local;


import Desperatedrosseln.Json.utils.JsonDeserializer;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Protocols.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Runnable {
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private int clientID;

    HashMap<String, Integer> localPlayerList = new HashMap<>();
    private MainController mainController;
    private String protocol = "Version 0.1";

    private String clientName;


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

    public void sendPlayerValues(int RobotID){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);          //TODO:replace ClientID with player figure!!!!!
        sendMessage("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(clientName, clientID)));
    }


    private void checkProtocolMessage(String message) throws IOException {
        //TODO: Logs
        if(message.startsWith("{\"messageType\":\"GameStarted\"")){
            JsonDeserializer jsonDeserializer = new JsonDeserializer();
            ProtocolMessage<GameStarted> gameStartedProtocolMessage = jsonDeserializer.deserialize(message);
            GameStarted gameStarted = gameStartedProtocolMessage.getMessageBody();
            Desperatedrosseln.Logic.Elements.Map map =new Desperatedrosseln.Logic.Elements.Map(mainController.getMapController().convertMap(gameStarted.getGameMap()));
            mainController.getMapController().setMap(map);
            mainController.getMapController().showMap();
            mainController.getMapController().setMap(gameStartedProtocolMessage.getMessageBody().getGameMap());

            return;
        }
        System.out.println(message);
        Moshi moshi = new Moshi.Builder().build();
        Message msg;
        {
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            msg = messageJsonAdapter.fromJson(message);
        }

        if (!msg.getMessageType().equals("Alive")) {
            System.out.println(msg.getMessageType() + ": " + msg.getMessageBody());
        }

        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                //sendHelloServer();

                //System.out.println("helloClient");

                break;
            case "Alive":
                sendMessage(msg.getMessageType(), msg.getMessageBody());
                //System.out.println("Alive check from server");
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.clientID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                System.out.println("welcome Client " + clientID);
                //sendPlayerValues(clientID);
                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());

                if (playerAdded.getClientID() == clientID) {                                      //TODO: Ready button?
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(true)));
                }
                break;
            case "PlayerStatus":
                break;
            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap sm = selectMapJsonAdapter.fromJson(msg.getMessageBody());

                //TODO: GUI map selection

                JsonAdapter<MapSelected> mapSelectedJsonAdapter= moshi.adapter(MapSelected.class);
                sendMessage("MapSelected",mapSelectedJsonAdapter.toJson(new MapSelected(sm.getMaps().get(0))));

                break;
            case "ReceivedChat":
                JsonAdapter<ReceivedChat> receivedChatJsonAdapter = moshi.adapter(ReceivedChat.class);
                ReceivedChat receivedChat = receivedChatJsonAdapter.fromJson(msg.getMessageBody());
                if (receivedChat.isPrivate()) {
                    mainController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": (Whispered)" + receivedChat.getMessage());
                } else {
                    mainController.addChatMessage(getPlayerName(receivedChat.getFrom()) + ": " + receivedChat.getMessage());
                }
            case "GameStarted":

                //skipped
                JsonAdapter<GameStarted> gameStartedJsonAdapter = moshi.adapter(GameStarted.class);
                GameStarted gameStarted = gameStartedJsonAdapter.fromJson(msg.getMessageBody());



                break;
            case "Error":
                if (mainController != null) {
                    mainController.addChatMessage("Error Occurred");
                }
                break;
            case "CardPlayed":
                break;
            case "StartingPointTaken":

                //ToDo

                break;
        }

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
        System.out.println("Setting local client name to " + clientName);
        this.clientName = clientName;
    }

    public void sendChatMessage(String message, int to) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        if (message.startsWith("/")) {          //TODO

            String[] messageParts = message.split(" ", 3);


            if (message.startsWith("/dm")) {
                if (messageParts.length < 3) {
                    mainController.addChatMessage("Please complete the  command.");
                    return;
                }
                if (messageParts[1].equals(this.clientName)) {
                    mainController.addChatMessage("You cannot send yourself private Messages, it is wierd. just think and talk to yourself.");
                    return;
                }
                if (localPlayerList.containsKey(messageParts[1])) {
                    sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(messageParts[2], localPlayerList.get(messageParts[1]))));
                } else {
                    mainController.addChatMessage("/dm did not work. Reason: invalid player name.");
                }
            } else if (message.startsWith("/addAI")) {
                sendMessage("addAI", "");
            }


        } else {
            sendMessage("SendChat", sendChatJsonAdapter.toJson(new SendChat(message, -1)));
        }
    }


}

