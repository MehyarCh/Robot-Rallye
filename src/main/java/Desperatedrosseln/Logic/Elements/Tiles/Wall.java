package Desperatedrosseln.Logic.Elements.Tiles;

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
    @Override
    public Position getPosition(){
        return position;
    }
    @Override
    public void setPosition(int x, int y){
        if(position!=null){
            position.setX(x);
            position.setY(y);
        }else{
            position = new Position(x,y);
        }
    }
}


