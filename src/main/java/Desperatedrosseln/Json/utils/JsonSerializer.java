package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Local.Protocols.ProtocolMessage;
import Desperatedrosseln.Logic.Elements.BoardElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonSerializer {
    GsonBuilder builder;
    Gson gson;

    public JsonSerializer() {
        builder = new GsonBuilder();
        builder.registerTypeAdapter(BoardElement.class, new TileAdapter());
        gson = builder.create();
    }

    public String serialize(ProtocolMessage message) {
        return gson.toJson(message);
    }
}
