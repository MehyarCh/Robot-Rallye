package Desperatedrosseln.Logic.Elements.tiles;

import java.util.ArrayList;

public class ConveyorBelt extends Tile {
    private int speed;
    private ArrayList<String> orientations;

    public ConveyorBelt(String type, String isOnBoard, int speed, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.speed = speed;
        this.orientations = orientations;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public ArrayList<String> getOrientations() {
        return orientations;
    }

    public void setOrientations(ArrayList<String> orientations) {
        this.orientations = orientations;
    }
}
