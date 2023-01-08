package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;

import java.util.ArrayList;

public class Antenna extends BoardElement {

    private ArrayList<String> orientations;

    public Antenna(String type, String isOnBoard, ArrayList<String> orientations) {
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
