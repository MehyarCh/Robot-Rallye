package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;

import java.util.ArrayList;

public class Antenna extends BoardElement {

    private ArrayList<String> orientations;
    private Position position;

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
    @Override
    public String toString(){
        return "Antenna";
    }
    @Override
    public Position getPosition() {
        return position;
    }
}
