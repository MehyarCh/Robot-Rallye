package Desperatedrosseln.Connection;

import Desperatedrosseln.Local.Protocols.Alive;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Logic.Game;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    private ServerSocket serverSocket;
    private List<String> gameLog;
    private int port = 3000;
    private String protocol = "Version 0.1";
    private Game game;

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
        System.out.println("[SERVER]: Waiting for client connection");
        Server server = new Server();
        server.startServer();
    }
}