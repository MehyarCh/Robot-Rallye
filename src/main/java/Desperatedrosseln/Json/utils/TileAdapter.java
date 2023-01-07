package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Logic.Elements.Checkpoint;
import Desperatedrosseln.Logic.Elements.tiles.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class TileAdapter extends TypeAdapter<Tile> {
    @Override
    public void write(JsonWriter out, Tile value) throws IOException {
        // Not used in this example
    }

    @Override
    public Tile read(JsonReader reader) throws IOException {

        String type = null;
        String isOnBoard = null;
        ArrayList<String> orientations = new ArrayList<>();
        int speed = 0;
        int count = 0;

        // Read the JSON object and determine the type of tile to create
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case "type" -> {
                    type = reader.nextString();
                }
                case "isOnBoard" -> {
                    isOnBoard = reader.nextString();
                }
                case "orientations" -> {
                    reader.beginArray();
                    while (reader.hasNext()) orientations.add(reader.nextString());
                    reader.endArray();
                }
                case "speed" -> {
                    speed = reader.nextInt();
                }
                case "count" -> {
                    count = reader.nextInt();
                }
                default -> reader.skipValue();
            }
        }
        reader.endObject();

        assert type != null;
        return switch (type) {
            case "Empty" -> new Empty(type, isOnBoard);
            case "StartPoint" -> new StartPoint(type, isOnBoard);
            case "CheckPoint" -> new CheckPoint(type, isOnBoard);
            case "RestartPoint" -> new RestartPoint(type, isOnBoard, orientations);
            case "Antenna" -> new Antenna(type, isOnBoard, orientations);
            case "Wall" -> new Wall(type, isOnBoard, orientations);
            case "Laser" -> new Laser(type, isOnBoard, orientations, count);
            case "Energy-Space" -> new EnergySpace(type, isOnBoard, count);
            case "ConveyorBelt" -> new ConveyorBelt(type, isOnBoard, speed, orientations);
            default -> null;
        };
    }
}

