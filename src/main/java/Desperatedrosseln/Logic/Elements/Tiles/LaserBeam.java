package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;

import java.util.ArrayList;

public class LaserBeam extends BoardElement {
    ArrayList<String> orientations;
    private int count;
    boolean isFullWidth;
    public LaserBeam(String type, String isOnBoard, ArrayList<String> orientations, int count, boolean isFullWidth) {
        super(type, isOnBoard);
        this.orientations = orientations;
        this.count = count;
        this.isFullWidth = isFullWidth;
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

    public boolean isFullWidth() {
        return isFullWidth;
    }

    public void setFullWidth(boolean fullWidth) {
        isFullWidth = fullWidth;
    }
}
