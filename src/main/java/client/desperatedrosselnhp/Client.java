package client.desperatedrosselnhp;
import server.connection.ClientHandler;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private String clientName;
    public String[] defaultCommands = ClientHandler.CommandMap.commandMap;
    public String[] defaultMessages = ClientHandler.MessageMap.messageMap;


    public Client(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void startClient() throws IOException {
        Socket clientSocket = new Socket("localhost", 3000);
        Client client = new Client(clientSocket);

        System.out.println("Welcome, please enter an username.");

        Thread thread = new Thread(client, "ListenerThread");
        thread.start();

        client.typeMessages();
    }

    public void sendMessage(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void typeMessages() {
        Scanner scanner = new Scanner(System.in);
        while (!clientSocket.isClosed()) {
            String message = scanner.nextLine();
            checkCommands(message);
        }
    }

    public void checkIncoming(String message) {
        if (message.startsWith(defaultMessages[0] + defaultMessages[7])) {
            String name = message.split("!! ")[1];
            this.clientName = name;
        } else if (!message.startsWith(defaultMessages[0] + "#")){
            System.out.println(message);
        }
    }

    public void checkCommands(String message) {
        if (message != null) {
            // logout
            if (message.equals(defaultCommands[0])) {
                sendMessage(message);
                logout();
            }
            // help
            else if (message.equals(defaultCommands[1])){
                ClientHandler.CommandMap.help();
            }
            // direct message
            else if (message.startsWith(defaultCommands[2])) {
                sendDirectMessage(message);
            }
            // empty
            else if (message.equals(" ")) {
                System.out.println(defaultMessages[2]);
            }
            // normal message
            else {
                sendMessage(message);
            }
        }
    }

    public void sendDirectMessage(String message) {
        String pattern = "(/dm)(\\s)(\\S+)(\\s)(\\S+)((\\s)*(\\S*))*";
        String recipient = message.split(" ")[1];
        if (message.matches(pattern)) {
            if (!Objects.equals(this.clientName, recipient)) {
                sendMessage(message);
            } else {
                System.out.println("You cannot send direct messages to yourself!");
            }
        } else {
            System.out.println("The command might be misspelled. To send a DirectMessage try the following Command: /dm recipient message");
        }
    }

    public void windowDisposition() {
        logout();
    }

    public void logout() {
        closeAll(clientSocket, in, out);
    }

    public void closeAll(Socket clientSocket, BufferedReader in, BufferedWriter out) {
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
                message = in.readLine();
                if (message != null) {
                    checkIncoming(message);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            closeAll(clientSocket, in, out);
        }
    }
    public String getClientName(){
        return this.clientName;
    }


    public static void main(String[] args) throws IOException {
        startClient();
    }
}
