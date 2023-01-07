package Desperatedrosseln.Logic.Elements.tiles;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonClass;

import java.util.List;

public class Tile {

    public List<String> types;

    private String type;
    private String isOnBoard;

    public Tile(String type, String isOnBoard) {
        this.type = type;
        this.isOnBoard = isOnBoard;
    }

    public String getType() {
        return type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }
}