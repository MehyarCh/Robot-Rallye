package Desperatedrosseln.Logic.Elements.tiles;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;

import java.util.ArrayList;

public class Wall extends BoardElement {

    private ArrayList<String> orientations;

    private transient Position position;

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


    @Override
    public String toString() {
        return "Wall";
    }
}


