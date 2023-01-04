package Desperatedrosseln.Local;

import Desperatedrosseln.Connection.ClientHandler;
import Desperatedrosseln.Local.Controllers.LoginController;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.GameMap;
import Desperatedrosseln.Logic.Game;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

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
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
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

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendHelloServer() {
        JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
        String helloServer = helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", false, protocol));
        Message message = new Message("HelloServer", helloServer);
        sendMessage(messageJsonAdapter.toJson(message));
    }

    private void checkProtocolMessage(String message) throws IOException {
        //TODO: Logs
        Message msg = messageJsonAdapter.fromJson(message);

        if(!msg.getMessageType().equals("Alive")){
            System.out.println(msg.getMessageType()+": "+msg.getMessageBody());
        }

        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                //sendHelloServer();

                //System.out.println("helloClient");

                break;
            case "Alive":
                sendMessage(messageJsonAdapter.toJson(msg));
                //System.out.println("Alive check from server");
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.clientID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                System.out.println("welcome Client "+clientID);
                JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);          //TODO:replace ClientID with player figure!!!!!
                sendMessage(messageJsonAdapter.toJson(new Message("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(clientName, clientID)))));
                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());

                if (playerAdded.getClientID() == clientID) {                                      //TODO: Ready button?
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage(messageJsonAdapter.toJson(new Message("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(true)))));
                }
                break;
            case "PlayerStatus":
                break;
            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap sm = selectMapJsonAdapter.fromJson(msg.getMessageBody());
                //TODO: GUI map selection
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
                JsonAdapter<GameStarted> gameStartedJsonAdapter = moshi.adapter(GameStarted.class);
                GameStarted gameStarted = gameStartedJsonAdapter.fromJson(msg.getMessageBody());

               // GameMap gameMap = gameStarted.getMap();

                break;
            case "Error":
                if(mainController != null){
                    mainController.addChatMessage("Error Occurred");
                }
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
        System.out.println("Setting local client name to "+clientName);
        this.clientName = clientName;
    }

    public void sendChatMessage(String message, int to) {
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        if (message.startsWith("/dm")) {          //TODO

            String[] messageParts = message.split(" ", 3);

            if(messageParts.length <3){
                mainController.addChatMessage("Please complete the /dm command.");
                return;
            }


            if (messageParts[1].equals(this.clientName)) {
                mainController.addChatMessage("You cannot send yourself private Messages, it is wierd. just think and talk to yourself.");
                return;
            }

            if (localPlayerList.containsKey(messageParts[1])) {
                sendMessage(messageJsonAdapter.toJson(new Message("SendChat", sendChatJsonAdapter.toJson(new SendChat(messageParts[2], localPlayerList.get(messageParts[1]))))));
            } else {
                mainController.addChatMessage("/dm did not work. Reason: invalid player name.");
            }

        } else if (message.startsWith("/addAI")) {
            sendMessage(messageJsonAdapter.toJson(new Message("addAI","")));
        } else {
            sendMessage(messageJsonAdapter.toJson(new Message("SendChat", sendChatJsonAdapter.toJson(new SendChat(message, -1)))));
        }
    }


}

