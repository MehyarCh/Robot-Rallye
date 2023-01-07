package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.tiles.Tile;


import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JsonMapReader {

    private JsonFileReader jsonFileReader;
    private JsonDeserializer deserializer;
    private String testMap;
    private final String dizzyHighway;

    public JsonMapReader(){
        jsonFileReader = new JsonFileReader();
        try {
            deserializer = new JsonDeserializer();
            dizzyHighway = jsonFileReader.readFile("C:/Uni/desperatedrosseln-hp/src/main/resources/maps/dizzyHighway.json");
        } catch (IOException e) {
            System.out.println("123");
            throw new RuntimeException(e);
        }
    }

    /*public List<List<List<Tile>>> readMapFromJson(String mapName) throws IOException {
        JsonDeserializer deserializer = new JsonDeserializer();
        GameStarted gameStarted;
        List<List<List<Tile>>> gameMap;
        if(Objects.equals(mapName, "testMap")) {
            gameStarted = deserializer.deserializeMessageBody(testMap);
            gameMap = gameStarted.getGameMap();
            return gameMap;
        } else if(Objects.equals(mapName, "dizzyHighway")) {
            gameStarted = deserializer.deserializeMessageBody(dizzyHighway);
            gameMap = gameStarted.getGameMap();
            System.out.println(gameMap);
            Tile tile = gameMap.get(0).get(0).get(0);
            System.out.println(tile instanceof Empty);
            return gameMap;
        } else {
            return null;
        }
    }*/

    public List<List<List<Tile>>> readMapFromJson(String mapName) {
        ProtocolMessage<GameStarted> message;
        switch (mapName) {
            case "testMap":
                message = deserializer.deserialize(testMap);
                return message.getMessageBody().getGameMap();
            case "dizzyHighway":
                message = deserializer.deserialize(dizzyHighway);
                //System.out.println(message.getMessageBody().getGameMap());
                return message.getMessageBody().getGameMap();
            default:
                return null;
        }
    }
}
