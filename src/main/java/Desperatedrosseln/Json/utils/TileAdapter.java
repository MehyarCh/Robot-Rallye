package Desperatedrosseln.Json.utils;

import Desperatedrosseln.Logic.Elements.*;
import Desperatedrosseln.Logic.Elements.Tiles.*;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;

public class TileAdapter extends TypeAdapter<BoardElement> {
    @Override
    public void write(JsonWriter out, BoardElement value) throws IOException {
        out.beginObject();

        // Write the "type" field
        out.name("type");
        out.value(value.getType());
        // Write the "isOnBoard" field
        out.name("isOnBoard");
        out.value(value.getIsOnBoard());

        // Write the "orientations" field

        if(value instanceof RestartPoint || value instanceof Wall || value instanceof Laser || value instanceof LaserBeam
                || value instanceof Antenna || value instanceof PushPanel || value instanceof ConveyorBelt || value instanceof Gear){

            out.name("orientations");
            out.beginArray();
            switch (value.getType()){
                case "Wall": Wall wall = (Wall) value;
                    for (String orientation : wall.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "Laser":Laser laser = (Laser) value;
                    for (String orientation : laser.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "LaserBeam":LaserBeam laserBeam = (LaserBeam) value;
                    for (String orientation : laserBeam.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "Antenna":Antenna antenna = (Antenna) value;
                    for (String orientation : antenna.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "PushPanel":PushPanel pushPanel = (PushPanel) value;
                    for (String orientation : pushPanel.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "ConveyorBelt":ConveyorBelt conveyorBelt = (ConveyorBelt) value;
                    for (String orientation : conveyorBelt.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "RestartPoint": RestartPoint restartPoint = (RestartPoint) value;
                    for (String orientation : restartPoint.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
                case "Gear":Gear gear = (Gear) value;
                    for (String orientation : gear.getOrientations()) {
                        out.value(orientation);
                    }
                    break;
            }
            out.endArray();
        }
        // Write the "registers" field for PushPanel tiles
        if (value instanceof PushPanel) {
            PushPanel pushPanel = (PushPanel) value;
            out.name("registers");
            out.beginArray();
            for (Integer pushPanelRegisters : pushPanel.getRegisters()){
                out.value(pushPanelRegisters);
            }
            out.endArray();
        }


        // Write the "speed" field for ConveyorBelt tiles
        if (value instanceof ConveyorBelt) {
            ConveyorBelt conveyorBelt = (ConveyorBelt) value;
            out.name("speed");
            out.value(conveyorBelt.getSpeed());
        }

        // Write the "count" field for Laser and Energy-Space tiles
        if (value instanceof Laser || value instanceof EnergySpace || value instanceof CheckPoint) {
            int count = 0;
            if (value instanceof Laser) {
                Laser laser = (Laser) value;
                count = laser.getCount();
            } else if (value instanceof EnergySpace) {
                EnergySpace energySpace = (EnergySpace) value;
                count = energySpace.getCount();
            }else if (value instanceof CheckPoint) {
                CheckPoint checkPoint = (CheckPoint) value;
                count = checkPoint.getCount();
            }
            out.name("count");
            out.value(count);
        }

        out.endObject();
    }

    @Override
    public BoardElement read(JsonReader reader) throws IOException {

        String type = null;
        String isOnBoard = null;
        ArrayList<String> orientations = new ArrayList<>();
        ArrayList<Integer> registers = new ArrayList<>();
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
                case "registers" -> {
                    reader.beginArray();
                    while (reader.hasNext()) registers.add(reader.nextInt());
                    reader.endArray();
                }
                default -> reader.skipValue();
            }
        }
        reader.endObject();

        assert type != null;
        return switch (type) {
            case "Empty" -> new Empty(type, isOnBoard);
            case "Pit" -> new Pit(type, isOnBoard);
            case "StartPoint" -> new StartPoint(type, isOnBoard);
            case "CheckPoint" -> new CheckPoint(type, isOnBoard, count);
            case "RestartPoint" -> new RestartPoint(type, isOnBoard, orientations);
            case "Antenna" -> new Antenna(type, isOnBoard, orientations);
            case "Wall" -> new Wall(type, isOnBoard, orientations);
            case "Laser" -> new Laser(type, isOnBoard, orientations, count);
            case "Energy-Space" -> new EnergySpace(type, isOnBoard, count);
            case "ConveyorBelt" -> new ConveyorBelt(type, isOnBoard, speed, orientations);
            case "PushPanel" -> new PushPanel(type, isOnBoard, orientations, registers);
            case "Gear" -> new Gear(type, isOnBoard, orientations);
            default -> null;
        };
    }
}

