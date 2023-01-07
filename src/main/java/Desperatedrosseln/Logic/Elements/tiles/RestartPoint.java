package Desperatedrosseln.Logic.Elements.tiles;

import java.util.ArrayList;

public class RestartPoint extends Tile {
    private ArrayList<String> orientations;

    public RestartPoint(String type, String isOnBoard, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.orientations = orientations;
    }
}
