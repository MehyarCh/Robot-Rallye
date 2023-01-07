package Desperatedrosseln.Logic.Elements.tiles;

import java.util.ArrayList;

public class Laser extends Tile {
    ArrayList<String> orientations;
    private int count;
    public Laser(String type, String isOnBoard, ArrayList<String> orientations, int count) {
        super(type, isOnBoard);
        this.orientations = orientations;
        this.count = count;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public void setOrientations(ArrayList<String> orientations) {
        this.orientations = orientations;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
