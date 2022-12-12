package Desperatedrosseln.Connection;

import Desperatedrosseln.Local.Protocols.Alive;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Logic.Game;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Objects;

public class Server {
    private ServerSocket serverSocket;
    private List<String> gameLog;
    private Game game;

    public Server(ServerSocket serversocket) {
        this.serverSocket = serversocket;
    }

    public void startServer() {

        game = new Game();

        while (!serverSocket.isClosed()) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
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
        ServerSocket serverSocket = new ServerSocket(3000);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}