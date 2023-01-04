package Desperatedrosseln.Logic.AI;

import Desperatedrosseln.Local.Protocols.*;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class AIClient extends Thread{

    private final DataInputStream in;
    private final DataOutputStream out;
    Socket clientSocket;
    HashMap<String, Integer> localPlayerList = new HashMap<>();

    AI ai;
    private int AI_ID;
    private String AIName = "GigaChad";
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
    private String protocol;

    public AIClient( int port, String protocol) {

        this.protocol =protocol;

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

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkProtocolMessage(String message) throws IOException {
        //TODO: Logs
        Message msg = messageJsonAdapter.fromJson(message);
        switch (msg.getMessageType()) {
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
                sendMessage(messageJsonAdapter.toJson(new Message("HelloServer",helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", true, protocol)))));

                break;
            case "Alive":
                sendMessage(messageJsonAdapter.toJson(msg));
                //System.out.println("Alive check from server");
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.AI_ID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                ai = new AI(AI_ID);



                JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);          //TODO:replace ClientID with player figure!!!!!
                sendMessage(messageJsonAdapter.toJson(new Message("PlayerValues", playerValuesJsonAdapter.toJson(new PlayerValues(AIName, AI_ID)))));
                break;

            case "PlayerAdded":
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());

                localPlayerList.put(playerAdded.getName(), playerAdded.getClientID());

                if (playerAdded.getClientID() == AI_ID) {                                      //TODO: Ready button?
                    sendChatMessage("Hello Everyone", -1);
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage(messageJsonAdapter.toJson(new Message("SetStatus", setStatusJsonAdapter.toJson(new SetStatus(true)))));
                }
                break;

            case "SelectMap":
                JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
                SelectMap selectMap = selectMapJsonAdapter.fromJson(msg.getMessageBody());
                String map = selectMap.getMaps().get(0);

                JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                sendMessage(messageJsonAdapter.toJson(new Message("MapSelected", mapSelectedJsonAdapter.toJson(new MapSelected(map)))));
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
                //ToDo: get starting points from the map
                int x = 0, y = 0;
                JsonAdapter<SetStartingPoint> setStartingPointJsonAdapter = moshi.adapter(SetStartingPoint.class);
                sendMessage(messageJsonAdapter.toJson(new Message("SetStartingPoint", setStartingPointJsonAdapter.toJson(new SetStartingPoint(x,y)))));

                break;

            case "StartingPointTaken":
                //ToDo:update the starting points
                break;

            case "YourCards":
                JsonAdapter<YourCards> yourCardsJsonAdapter = moshi.adapter(YourCards.class);
                ai.setCardsInHand(yourCardsJsonAdapter.fromJson(msg.getMessageBody()).getCardsInHand());
                JsonAdapter<SelectedCard> selectedCardJsonAdapter = moshi.adapter(SelectedCard.class);
                for(int i=0;i<5;++i){
                    sendMessage(messageJsonAdapter.toJson(new Message("SelectedCard",selectedCardJsonAdapter.toJson(new SelectedCard(ai.getCardsInHand().get(i),i)))));
                }

                break;
                case "Error":
                break;
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
    public void sendChatMessage(String message, int to) {
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        if (message.startsWith("/dm")) {          //TODO

            String[] messageParts = message.split(" ", 3);

            if (localPlayerList.containsKey(messageParts[1])) {
                sendMessage(messageJsonAdapter.toJson(new Message("SendChat", sendChatJsonAdapter.toJson(new SendChat(messageParts[2], localPlayerList.get(messageParts[1]))))));
            }

        } else {
            sendMessage(messageJsonAdapter.toJson(new Message("SendChat", sendChatJsonAdapter.toJson(new SendChat(message, -1)))));
        }
    }
}
