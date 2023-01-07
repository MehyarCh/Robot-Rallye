package Desperatedrosseln.Logic;

import Desperatedrosseln.Connection.ClientHandler;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Local.Protocols.SelectMap;
import Desperatedrosseln.Logic.AI.AIClient;
import Desperatedrosseln.Logic.Elements.Robot;
import Desperatedrosseln.Logic.Elements.tiles.Position;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static String currentMap;
    private static ArrayList<Player> players = new ArrayList<>();
    private final int port;
    private String protocol = "Version 0.1";

    private static int mapSelecttioinPlayerID = -1;
    private int distance;

    public Game(int port, String protocol) {
        this.protocol = protocol;
        this.port = port;
    }

    public static void readyPlayer(ClientHandler client) {             //TODO: case player disconnects
        if(mapSelecttioinPlayerID == -1){
            mapSelecttioinPlayerID = client.getPlayer().getID();
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<Message> messageJsonAdapter = moshi.adapter(Message.class);
            JsonAdapter<SelectMap> selectMapJsonAdapter = moshi.adapter(SelectMap.class);
            ArrayList<String> maps = new ArrayList<>();
            maps.add("Dizzy highway");
            client.sendMessage(messageJsonAdapter.toJson(new Message("SelectMap",selectMapJsonAdapter.toJson(new SelectMap(maps)))));
        }
    }

    public void runStep(){

    }

    private int calculateDistance(Position pos1, Position pos2){
        return distance;
    }

    private void activateElements(){

    }



    public void start(){

    }

    public void setPlayerValues(){                              //TODO: redo
        for (ClientHandler client:
                ClientHandler.clients) {
            players.add(new Player(new Robot(ClientHandler.clients.indexOf(client))));
        }
    }

    public static String getCurrentMap() {
        return currentMap;
    }

    public static void setCurrentMap(String currentMap) {
        Game.currentMap = currentMap;
    }

    public static int getMapSelecttioinPlayerID() {
        return mapSelecttioinPlayerID;
    }

    public static void setMapSelecttioinPlayerID(int mapSelecttioinPlayerID) {
        Game.mapSelecttioinPlayerID = mapSelecttioinPlayerID;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public void addAI(){
        new AIClient(port,protocol);
    }
}



