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
    private String protocol = "Version 0.1";
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

    public Player getPlayer() {
        return player;
    }

    public static class MessageMap {
        public static String[] messageMap = {
                "[SERVER]: ",

                "Welcome, please enter your username!", // 1
                "Message can not be empty",
                "Here are all commands:",
                "PLACEHOLDER",
                "This username is already taken by another player. Please choose another one.",

                "#101 - nameAttempt", //6
                "#102 - following name accepted !! ",
                "#103 - name already taken",
                "#104 - name cannot contain space characters",
                "#105 - Connection Error",

                "The username cannot contain any space characters.", //11
                "Goodbye ",
        };
    }

    public static class CommandMap {
        public static String[] commandMap = {
                "bye",
                "/help",
                "/dm"
        };

        public static String help() {

            StringBuilder commandList = new StringBuilder();

            System.out.println("\n" + MessageMap.messageMap[3]);

            for (String command : commandMap) {
                commandList.append(command).append(",\n");
            }

            System.out.println(commandList.toString());

            return commandList.toString();
        }
    }

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

            JsonAdapter<HelloClient> helloClientJsonAdapter = moshi.adapter(HelloClient.class);         //TODO: redo this
            sendMessage(messageJsonAdapter.toJson(new Message("HelloClient", helloClientJsonAdapter.toJson(new HelloClient(protocol))))); //send HelloClient


            player = new Player(clientName);

            clients.add(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    public void assignName() {
        try {
            String nameAttempt;
            while (true) {
                nameAttempt = in.readUTF();
                if (nameAttempt.contains(" ")) {
                    sendMessage(createLog(MessageMap.messageMap[9], "server"));
                    sendMessage(createLog(MessageMap.messageMap[11], "server"));
                } else {
                    if (checkName(nameAttempt)) {
                        this.clientName = nameAttempt;
                        sendMessage(createLog(MessageMap.messageMap[7] + this.clientName, "server"));
                        greetUser();
                        break;
                    } else {
                        sendMessage(createLog(MessageMap.messageMap[5], "server"));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */
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

            switch (message.getMessageType()) {

                case "HelloServer":
                    //TODO: disconnection
                    System.out.println("HelloServer");
                    JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                    clientID = clients.indexOf(this) + 1;
                    sendMessage(messageJsonAdapter.toJson(new Message("Welcome", welcomeJsonAdapter.toJson(new Welcome(clientID)))));
                    player.setID(clientID);
                    System.out.println("client Connected");
                    break;

                case "Alive":
                    //
                    //System.out.println("client is alive");
                    break;

                case "PlayerValues":
                    JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);

                    PlayerValues playerValues = playerValuesJsonAdapter.fromJson(message.getMessageBody());

                    boolean robotTaken = false;
                    if (!Game.getPlayers().isEmpty()) {
                        for (Player player :
                                Game.getPlayers()) {
                            if (player.getRobot().getID() == playerValues.getFigure()) {
                                sendErrorMessage();
                                robotTaken = true;
                            }
                        }
                        if (!robotTaken) {
                            player = new Player();
                            player.setName(playerValues.getName());
                            player.setRobot(new Robot(playerValues.getFigure()));
                            this.clientName = playerValues.getName();
                            Game.getPlayers().add(player);
                            JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                            broadcastMessage(messageJsonAdapter.toJson(new Message("PlayerAdded", playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())))), 1);
                        }
                    } else {
                        player = new Player();
                        player.setName(playerValues.getName());
                        player.setRobot(new Robot(playerValues.getFigure()));
                        this.clientName = playerValues.getName();
                        Game.getPlayers().add(player);
                        JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                        broadcastMessage(messageJsonAdapter.toJson(new Message("PlayerAdded", playerAddedJsonAdapter.toJson(new PlayerAdded(player.getID(), player.getName(), player.getRobot().getID())))), 1);
                    }


                    break;
                case "SetStatus":
                    JsonAdapter<PlayerStatus> playerStatusJsonAdapter = moshi.adapter(PlayerStatus.class);
                    PlayerStatus playerStatus = new PlayerStatus(player.getID(), true);
                    broadcastMessage(messageJsonAdapter.toJson(new Message("PlayerStatus", playerStatusJsonAdapter.toJson(playerStatus))), 1);
                    if (playerStatus.isReady()) {
                        Game.readyPlayer(this);

                    }
                    break;
                case "MapSelected":
                    JsonAdapter<MapSelected> mapSelectedJsonAdapter = moshi.adapter(MapSelected.class);
                    Game.setCurrentMap(mapSelectedJsonAdapter.fromJson(message.getMessageBody()).getMap());
                    break;

                case "SendChat":

                    System.out.println("message received");

                    JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
                    SendChat sendChat = sendChatJsonAdapter.fromJson(message.getMessageBody());
                    JsonAdapter<ReceivedChat> receivedChatJsonAdapter = moshi.adapter(ReceivedChat.class);
                    if (sendChat.getTo() == -1) {
                        broadcastMessage(messageJsonAdapter.toJson(new Message("ReceivedChat", receivedChatJsonAdapter.toJson(new ReceivedChat(sendChat.getMessage(), clientID, false)))), 1);
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
                    broadcastMessage(messageJsonAdapter.toJson(new Message(" ", "SERVER BRO")), 1);

                /*
                case SENDCHAT -> {
                    if (message.getContent().equals(CommandMap.commandMap[0])) {                //bye
                        broadcastMessage(new Message(message.getContent(), MessageType.CHATMESSAGE,clientName));
                        sendMessage(new Message(MessageMap.messageMap[12] + clientName, MessageType.SERVERMESSAGE,clientName));
                        closeAll(socket, in, out);
                    }
                }
                case DIRECTCHATMESSAGE -> {
                    sendDirectMessage(message);
                }

                case CHATMESSAGE -> {
                    if (clients.size() > 1) {
                        broadcastMessage(message);
                    }
                }
                case GAMECONTROL -> {
                    if(message.getContent().equals("/join")){
                        if(!game.checkPlayer(message.getSender())){
                            broadcastMessage(new Message(clientName+" joined the game.",MessageType.SERVERMESSAGE,clientName));
                            game.addPlayer(message.getSender());
                        } else {
                            sendMessage(new Message("you are already in the Game",MessageType.SERVERMESSAGE,clientName));
                        }

                    }

                }*/
            }


        }
    }

    public int getClientID() {
        return clientID;
    }

    public boolean checkName(String name) {

        ArrayList<String> names = new ArrayList<>();

        for (ClientHandler handler : clients) {
            names.add(handler.clientName);
        }

        System.out.println(names);

        return names.size() == 0 || !names.contains(name);
    }

    // logType: Add the [Server] or <Client> signature at the start of the line


    public void sendDirectMessage(String message) {
        String[] splitCommand = message.split(" ", 3);
        String command = splitCommand[0];
        String recipient = splitCommand[1];
        String content = splitCommand[2];

        for (ClientHandler client : clients) {
            if (Objects.equals(client.clientName, recipient)) {
                client.sendMessage(content);

                break;
            }
        }
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
    public void broadcastMessage(String message, int messageType) {
        System.out.println(clients);
        if (messageType == 0) {
            for (ClientHandler client : clients) {
                if (!Objects.equals(this.clientName, client.clientName)) {
                    client.sendMessage(message);
                }
            }
        } else if (messageType == 1) {
            for (ClientHandler client : clients) {
                client.sendMessage(message);
            }
        }
        System.out.println(message);
    }

    public void greetUser() {
        String greeting = "User " + clientName + " joined the chat!";
        sendMessage("Welcome " + clientName);
        broadcastMessage(greeting, 0);
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
        String logout = "User " + clientName + " has left the Chat";
        broadcastMessage(logout, 0);
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
