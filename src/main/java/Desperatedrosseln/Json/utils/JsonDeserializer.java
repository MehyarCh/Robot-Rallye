package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Local.Protocols.ActivePhase;
import Desperatedrosseln.Local.Protocols.Alive;
import Desperatedrosseln.Local.Protocols.GameStarted;
import Desperatedrosseln.Local.Protocols.ProtocolMessage;
import Desperatedrosseln.Logic.Elements.tiles.Tile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

public class JsonDeserializer {

    GsonBuilder builder;
    Gson gson;

    public JsonDeserializer() throws IOException {
        builder = new GsonBuilder();
        builder.registerTypeAdapter(Tile.class, new TileAdapter());
        gson = builder.create();
    }

    public ProtocolMessage deserialize(String json) {
        ProtocolMessage jsonMessage = gson.fromJson(json, ProtocolMessage.class);

        return switch (jsonMessage.getMessageType()) {
            case "GameStarted" ->
                gson.fromJson(json, new TypeToken<ProtocolMessage<GameStarted>>() {}.getType());
            case "Alive" ->
                gson.fromJson(json, new TypeToken<ProtocolMessage<Alive>>() {}.getType());
            case "ActivePhase" ->
                    gson.fromJson(json, new TypeToken<ProtocolMessage<ActivePhase>>() {}.getType());
            default -> null;
        };
    }
}




