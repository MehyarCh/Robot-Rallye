package Desperatedrosseln.Connection;

import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Local.Protocols.Error;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Game;
import Desperatedrosseln.Logic.Player;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonEncodingException;
import com.squareup.moshi.Moshi;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
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
    private int clientID;
    private Game game;
    private boolean isAI = false;

    public Player getPlayer() {
        return player;
    }


    public ClientHandler(Socket socket, Game game, String protocol) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            this.game = game;
            this.protocol = protocol;

            clients.add(this);

            sendCurrentPlayers();

            JsonAdapter<HelloClient> helloClientJsonAdapter = moshi.adapter(HelloClient.class);         //TODO: redo this
            sendMessage(messageJsonAdapter.toJson(new Message("HelloClient", helloClientJsonAdapter.toJson(new HelloClient(protocol))))); //send HelloClient

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendCurrentPlayers() {
        JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
        for (Player player :
                game.getPlayers()) {
            if (!player.equals(this.player)) {
                sendMessage(messageJsonAdapter.toJson(new Message("PlayerAdded",
                        playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())))));
            }
        }
    }


    public void sendErrorMessage() {
        JsonAdapter<Desperatedrosseln.Local.Protocols.Error> errorJsonAdapter = moshi.adapter(Desperatedrosseln.Local.Protocols.Error.class);
        sendMessage(messageJsonAdapter.toJson(new Message("Error", errorJsonAdapter.toJson(new Error()))));
    }

    public void checkCommands(String msg) throws IOException {

        try {
            Message message = messageJsonAdapter.fromJson(msg);
        } catch (JsonEncodingException e) {
            System.out.println("this msg caused error: " + msg);
        }
        Message message = messageJsonAdapter.fromJson(msg);


        if (msg != null) {

            if (!message.getMessageType().equals("Alive")) {
                System.out.println(message.getMessageType() + ": " + message.getMessageBody());
            }

            switch (message.getMessageType()) {

                case "HelloServer":
                    JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
                    clientID = clients.indexOf(this) + 1;
                    HelloServer helloServer = helloServerJsonAdapter.fromJson(message.getMessageBody());

                    if (helloServer.getProtocol().equals(this.protocol)) {
                        if (helloServer.isAI) {
                            isAI = true;
                        }
                        JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                        sendMessage(messageJsonAdapter.toJson(new Message("Welcome", welcomeJsonAdapter.toJson(new Welcome(clientID)))));
                        System.out.println("client Connected");
                    } else {
                        System.out.println("invalid protocol");
                        sendErrorMessage();
                    }
                    break;

                case "Alive":
                    //
                    //System.out.println("client is alive");
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
                    JsonAdapter<PlayerStatus> playerStatusJsonAdapter = moshi.adapter(PlayerStatus.class);
                    PlayerStatus playerStatus = new PlayerStatus(player.getID(), true);
                    broadcastMessage(messageJsonAdapter.toJson(new Message("PlayerStatus", playerStatusJsonAdapter.toJson(playerStatus))));
                    if (playerStatus.isReady()) {
                        Game.readyPlayer(this);

                    }
                    break;
                case "MapSelected":
                    JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                    Game.setCurrentMap(mapSelectedJsonAdapter.fromJson(message.getMessageBody()).getMap());
                    break;

                case "SendChat":

                    JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
                    SendChat sendChat = sendChatJsonAdapter.fromJson(message.getMessageBody());
                    JsonAdapter<ReceivedChat> receivedChatJsonAdapter = moshi.adapter(ReceivedChat.class);
                    if (sendChat.getTo() == -1) {
                        broadcastMessage(messageJsonAdapter.toJson(new Message("ReceivedChat", receivedChatJsonAdapter.toJson(new ReceivedChat(sendChat.getMessage(), clientID, false)))));
                    } else {
                        for (ClientHandler client :
                                clients) {
                            if (client.getClientID() == sendChat.getTo()) {
                                client.sendMessage(messageJsonAdapter.toJson(new Message("ReceivedChat", receivedChatJsonAdapter.toJson(new ReceivedChat(sendChat.getMessage(), clientID, true)))));
                                //TODO: Display in current clients gui.
                            }
                        }
                    }
                    break;
                default:
                    broadcastMessage(messageJsonAdapter.toJson(new Message(" ", "SERVER BRO")));

                case "addAI":
                    game.addAI();
                    break;

            }


        }
    }

    private void setPlayerValues(PlayerValues playerValues) {
        System.out.println("Setting player: " + playerValues.getName());
        player = new Player();
        player.setID(clientID);
        player.setName(playerValues.getName());
        player.setRobot(new Robot(playerValues.getFigure()));
        this.clientName = playerValues.getName();
        game.getPlayers().add(player);
        sendCurrentPlayers();
        JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
        broadcastMessage(messageJsonAdapter.toJson(new Message("PlayerAdded", playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())))));
    }

    public int getClientID() {
        return clientID;
    }


    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // messageType: 0 = broadcast to all others, 1 = to everyone
    public void broadcastMessage(String message) {
        System.out.println(clients);
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
        System.out.println(message);
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
                sendMessage(messageJsonAdapter.toJson(new Message("Alive", aliveJsonAdapter.toJson(new Alive()))));     //after 5s alive message gets sent to the client.
            }
        }, 5 * 1000, 5 * 1000); //5 seconds


        while (!socket.isClosed()) {
            try {
                message = in.readUTF();
                checkCommands(message);
            } catch (IOException e) {
                e.printStackTrace();
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
}
