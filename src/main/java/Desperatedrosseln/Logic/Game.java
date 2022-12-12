package Desperatedrosseln.Logic;

import Desperatedrosseln.Connection.ClientHandler;
import Desperatedrosseln.Local.Protocols.Message;
import Desperatedrosseln.Local.Protocols.SelectMap;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static String currentMap;
    private static ArrayList<Player> players = new ArrayList<>();
    private int phase;
    private Player playing;
    private static int mapSelecttioinPlayerID = -1;
    private int distance;

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

    public Player getNextPlayer(){
        return playing; //ToDo: change to next player
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

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static String getDizzyHighway(){
        String dizzyhighway = "{\n" +
                "  \"messageType\": \"GameStarted\",\n" +
                "  \"messageBody\": {\n" +
                "    \"gameMap\": [\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Antenna\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": [\"right\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": [\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"StartPoint\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"speed\": 1,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": \"right\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"orientations\": \"right\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"Start A\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"Start A\",\n" +
                "            \"speed\": 1,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"top\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"bottom\",\n" +
                "              \"top\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"top\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"top\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"bottom\"]\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"Laser\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"top\"],\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"left\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"RestartPoint\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"bottom\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"right\"]\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"Laser\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"right\"],\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"left\"]\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"Laser\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"left\"],\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"right\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"top\"]\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"Laser\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"top\"],\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Wall\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"orientations\": [\"bottom\"]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"right\",\n" +
                "              \"bottom\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"bottom\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\",\n" +
                "              \"left\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": 2,\n" +
                "            \"orientations\": [\n" +
                "              \"top\",\n" +
                "              \"bottom\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ]\n" +
                "      ],\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Energy-Space\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"count\": 1\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": \"2\",\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"ConveyorBelt\",\n" +
                "            \"isOnBoard\": \"5B\",\n" +
                "            \"speed\": \"2\",\n" +
                "            \"orientations\": [\n" +
                "              \"left\",\n" +
                "              \"right\"\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"CheckPoint\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ],\n" +
                "        [\n" +
                "          {\n" +
                "            \"type\": \"Empty\",\n" +
                "            \"isOnBoard\": \"5B\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ]\n" +
                "    ]\n" +
                "  }\n" +
                "}\n";

        return dizzyhighway.replace("\n", "").replace("\r", "");
    }


}



