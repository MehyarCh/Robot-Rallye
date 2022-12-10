package Desperatedrosseln.Connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String clientName;

    public static ArrayList<ClientHandler> clients = new ArrayList<>();

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
            assignName();
            clients.add(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void assignName() {
        try {
            String nameAttempt;
            while (true) {
                nameAttempt = in.readUTF();
                if (nameAttempt.contains(" ")) {
                    sendMessage(createLog(MessageMap.messageMap[9], "server" ));
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

    public void checkCommands(String message) {
        if (message != null) {
            // logout
            if (message.equals(CommandMap.commandMap[0])) {
                broadcastMessage(createLog(message, "client"), 0);
                sendMessage(createLog(MessageMap.messageMap[12] + clientName, "server"));
                closeAll(socket, in, out);
            }
            // help
            else if (message.equals(CommandMap.commandMap[1])){

            }
            // direct message
            else if (message.startsWith(CommandMap.commandMap[2])) {
                sendDirectMessage(message);
            } else {
                String messageWithLog = createLog(message, "client");
                if (clients.size() > 1) {
                    broadcastMessage(messageWithLog, 0);
                } else {
                    System.out.println(messageWithLog);
                }
            }
        }
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
    public String createLog(String message, String logType) {
        switch (logType) {
            case "server" -> message = MessageMap.messageMap[0] + message;
            case "client" -> message = "<" + clientName + ">: " + message;
            case "dm"     -> message = "<" + clientName + ">: " + "Direct Message - " + message;
        }
        return message;
    }

    public void sendDirectMessage(String message) {
        String[] splitCommand = message.split(" ", 3);
        String command = splitCommand[0];
        String recipient = splitCommand[1];
        String content = splitCommand[2];

        for (ClientHandler client : clients) {
            if (Objects.equals(client.clientName, recipient)) {
                client.sendMessage(createLog(content, "dm"));
                System.out.println(createLog(content, "dm"));
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
        sendMessage(createLog("Welcome " + clientName, "server"));
        broadcastMessage(createLog(greeting, "server"), 0);
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
        String logout = createLog("User " + clientName + " has left the Chat", "server");
        broadcastMessage(logout, 0);
    }

    @Override
    public void run() {

        String message;

        while (!socket.isClosed()) {
            try {
                message = in.readUTF();
                checkCommands(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getClientName(){
        return clientName;
    }
}
