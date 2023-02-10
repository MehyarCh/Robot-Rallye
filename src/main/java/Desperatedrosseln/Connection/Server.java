package Desperatedrosseln.Connection;

import Desperatedrosseln.Local.Protocols.Alive;
import Desperatedrosseln.Local.Protocols.ConnectionUpdate;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Local.Protocols.PlayerAdded;
import Desperatedrosseln.Logic.Game;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**

 @author Manuel
 The class Server is responsible for setting up a server socket on a specified port and listening for incoming client connections.
 Each time a new client connects, a new instance of the ClientHandler class is created and started in a new thread.
 The Server class also implements a broadcastMessage method that can be used to send messages to all connected clients.
 */
public class Server {
    private ServerSocket serverSocket;
    private List<String> gameLog;
    private int port = 3000;
    private String protocol = "Version 0.1";
    private Game game;

    private static final Logger logger = LogManager.getLogger();

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);

    public Server() throws IOException {
        serverSocket = new ServerSocket(port);
    }


    public void startServer() {

        game = new Game(port,protocol, ClientHandler.clients);

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket,game, protocol);
                Thread thread = new Thread(clientHandler);
                thread.start();

                /*for (ClientHandler clientHandler1 : ClientHandler.clients) {
                    if (clientHandler.getSocket().isClosed()) {
                        //ClientHandler.clients.remove(clientHandler);
                        //logger.info("[SERVER]: A client has disconnected");
                        //JsonAdapter<ConnectionUpdate> connectionUpdateJsonAdapter = moshi.adapter(ConnectionUpdate.class);
                        //broadcastMessage("ConnectionUpdate", connectionUpdateJsonAdapter.toJson(new ConnectionUpdate(clientHandler.getClientID(), false, "remove")));
                        //logger.warn("ConnectionUpdate sent");
                    }
                }*/

            } catch (IOException e) {
                closeServer();
            }
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        logger.info("[SERVER]: Waiting for client connection");
        Server server = new Server();
        server.startServer();



    }
}