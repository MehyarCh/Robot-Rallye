package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Local.Protocols.*;
import Desperatedrosseln.Logic.Elements.BoardElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonMapReader {

    private JsonFileReader jsonFileReader;
    private JsonDeserializer deserializer;
    private final String dizzyHighway;
    private final String extraCrispy;
    private final String deathTrap;
    private final String lostBearings;
    private final String twister;

    private static final Logger logger = LogManager.getLogger();

    public JsonMapReader(){
        jsonFileReader = new JsonFileReader();
        try {

            InputStream dH = JsonMapReader.class.getResourceAsStream("/maps/dizzyHighway.json");
            dizzyHighway = jsonFileReader.readFile(dH);

            InputStream eC = JsonMapReader.class.getResourceAsStream("/maps/extraCrispy.json");
            extraCrispy = jsonFileReader.readFile(eC);

            InputStream dT = JsonMapReader.class.getResourceAsStream("/maps/deathTrap.json");
            deathTrap = jsonFileReader.readFile(dT);

            InputStream lB = JsonMapReader.class.getResourceAsStream("/maps/lostBearings.json");
            lostBearings = jsonFileReader.readFile(lB);

            InputStream tw = JsonMapReader.class.getResourceAsStream("/maps/twister.json");
            twister = jsonFileReader.readFile(tw);

            deserializer = new JsonDeserializer();
            //dizzyHighway = jsonFileReader.readFile("src/main/resources/maps/dizzyHighway.json");
            //extraCrispy = jsonFileReader.readFile("src/main/resources/maps/extraCrispy.json");
            //deathTrap = jsonFileReader.readFile("src/main/resources/maps/deathTrap.json");
            //lostBearings = jsonFileReader.readFile("src/main/resources/maps/lostBearings.json");
            //twister = jsonFileReader.readFile("src/main/resources/maps/twister.json");
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
            case "Twister":
                message = deserializer.deserialize(twister);
                return message.getMessageBody().getGameMap();
            default:
                return null;
        }
    }
}
