package Desperatedrosseln.Local;

import Desperatedrosseln.Connection.ClientHandler;
import Desperatedrosseln.Local.Controllers.LoginController;
import Desperatedrosseln.Local.Controllers.MainController;
import Desperatedrosseln.Local.Protocols.*;
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

    private MainController mainController;
    private String protocol = "Version 0.1";
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
    private String clientName;


    public Client(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            this.in = new DataInputStream(clientSocket.getInputStream());
            this.out = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void startClient() throws IOException {
        Socket clientSocket = new Socket("localhost", 3000);
        Client client = new Client(clientSocket);
        System.out.println("");
        System.out.println("Listening");
        Thread thread = new Thread(client, "ListenerThread");
        thread.start();


    }

    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


public void sendHelloServer(){
    JsonAdapter<HelloServer> helloServerJsonAdapter = moshi.adapter(HelloServer.class);
    String helloServer = helloServerJsonAdapter.toJson(new HelloServer("DesperateDrosseln", false,protocol));
    Message message = new Message("HelloServer",helloServer);
    sendMessage(messageJsonAdapter.toJson(message));
}

    public void checkIncoming(String message) throws IOException {

        System.out.println("msg received by client: "+message);
        Message msg = messageJsonAdapter.fromJson(message);
        checkProtocolMessage(msg);

    }

    private void checkProtocolMessage(Message msg) throws IOException {
        //TODO: Logs

        switch (msg.getMessageType()){
            case "HelloClient":
                //TODO: disconnect if protocol isnt the same as client.
                //sendHelloServer();

                System.out.println("helloClient");

                break;
            case "Alive":
                sendMessage(messageJsonAdapter.toJson(msg));
                //System.out.println("Alive check from server");
                break;
            case "Welcome":
                JsonAdapter<Welcome> welcomeJsonAdapter = moshi.adapter(Welcome.class);
                this.clientID = welcomeJsonAdapter.fromJson(msg.getMessageBody()).getClientID();
                System.out.println("Welcome ID:"+clientID);

                JsonAdapter<PlayerValues> playerValuesJsonAdapter = moshi.adapter(PlayerValues.class);          //TODO: player figure change!!!!!!
                sendMessage(messageJsonAdapter.toJson(new Message("PlayerValues",playerValuesJsonAdapter.toJson(new PlayerValues(clientName,clientName.length())))));
                break;

            case "PlayerAdded":
                System.out.println("player added:");
                JsonAdapter<PlayerAdded> playerAddedJsonAdapter = moshi.adapter(PlayerAdded.class);
                PlayerAdded playerAdded = playerAddedJsonAdapter.fromJson(msg.getMessageBody());
                if(playerAdded.getClientID() == clientID){                                      //TODO: Ready button?
                    JsonAdapter<SetStatus> setStatusJsonAdapter = moshi.adapter(SetStatus.class);
                    sendMessage(messageJsonAdapter.toJson(new Message("SetStatus",setStatusJsonAdapter.toJson(new SetStatus(true)))));
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
                if(receivedChat.getFrom() != -1){
                    //TODO:
                    System.out.println("chatlogset");
                    mainController.addChatMessage(receivedChat.getMessage());
                }else{

                }
                break;
            case "CardPlayed":
                break;
        }

    }





    public void logout() {
        closeAll(clientSocket, in, out);
    }

    public void closeAll(Socket clientSocket, DataInputStream in, DataOutputStream out) {
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
                    checkIncoming(message);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            logout();
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
        this.clientName = clientName;
    }
    public void sendChatMessage(String message,int to){               //TODO: add to gui
        JsonAdapter<SendChat> sendChatJsonAdapter = moshi.adapter(SendChat.class);
        sendMessage(messageJsonAdapter.toJson(new Message("SendChat",sendChatJsonAdapter.toJson(new SendChat(clientName+": "+message,to)))));
    }


    public static void main(String[] args) throws IOException {
        startClient();
    }



}

