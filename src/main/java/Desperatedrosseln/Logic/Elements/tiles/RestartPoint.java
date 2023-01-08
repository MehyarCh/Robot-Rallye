package Desperatedrosseln.Logic.Elements.tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;

import java.util.ArrayList;

public class RestartPoint extends BoardElement {
    private ArrayList<String> orientations;

    public RestartPoint(String type, String isOnBoard, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.orientations = orientations;
    }
}
