package Desperatedrosseln.Logic.Elements.tiles;

import java.util.ArrayList;

public class Wall extends Tile {

    private ArrayList<String> orientations;

    public Wall(String type, String isOnBoard, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.orientations = orientations;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public void setOrientations(ArrayList<String> orientations) {
        this.orientations = orientations;
    }
}
