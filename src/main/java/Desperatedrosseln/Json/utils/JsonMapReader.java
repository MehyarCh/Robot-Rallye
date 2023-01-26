package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.BoardElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.List;

public class JsonMapReader {

    private JsonFileReader jsonFileReader;
    private JsonDeserializer deserializer;
    private final String dizzyHighway;
    private final String extraCrispy;
    private final String deathTrap;
    private final String lostBearings;

    private static final Logger logger = LogManager.getLogger();

    public JsonMapReader(){
        jsonFileReader = new JsonFileReader();
        try {
            deserializer = new JsonDeserializer();
            dizzyHighway = jsonFileReader.readFile("src/main/resources/maps/dizzyHighway.json");
            extraCrispy = jsonFileReader.readFile("src/main/resources/maps/extraCrispy.json");
            deathTrap = jsonFileReader.readFile("src/main/resources/maps/deathTrap.json");
            lostBearings = jsonFileReader.readFile("src/main/resources/maps/lostBearings.json");;
        } catch (IOException e) {
            logger.warn("error occurred at jsonMapReader()");
            throw new RuntimeException(e);
        }
    }

    /*public List<List<List<BoardElement>>> readMapFromJson(String mapName) throws IOException {
        JsonDeserializer deserializer = new JsonDeserializer();
        GameStarted gameStarted;
        List<List<List<BoardElement>>> gameMap;
        if(Objects.equals(mapName, "testMap")) {
            gameStarted = deserializer.deserializeMessageBody(testMap);
            gameMap = gameStarted.getGameMap();
            return gameMap;
        } else if(Objects.equals(mapName, "dizzyHighway")) {
            gameStarted = deserializer.deserializeMessageBody(dizzyHighway);
            gameMap = gameStarted.getGameMap();
            BoardElement tile = gameMap.get(0).get(0).get(0);
            return gameMap;
        } else {
            return null;
        }
    }*/

    public List<List<List<BoardElement>>> readMapFromJson(String mapName) {
        ProtocolMessage<GameStarted> message;
        switch (mapName) {
            case "DizzyHighway":
                message = deserializer.deserialize(dizzyHighway);
                return message.getMessageBody().getGameMap();
            case "ExtraCrispy":
                message = deserializer.deserialize(extraCrispy);
                return message.getMessageBody().getGameMap();
            case "DeathTrap":
                message = deserializer.deserialize(deathTrap);
                return message.getMessageBody().getGameMap();
            case "LostBearings":
                message = deserializer.deserialize(lostBearings);
                return message.getMessageBody().getGameMap();
            default:
                return null;
        }
    }
}
