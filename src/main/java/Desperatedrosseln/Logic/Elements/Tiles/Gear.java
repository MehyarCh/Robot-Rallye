package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;

public class Gear extends BoardElement {

    ArrayList<String> orientations;


    public Gear(String type, String isOnBoard, ArrayList<String> orientations) {
        super(type,isOnBoard); //can either be clockwise or counterclockwise -> turning direction
        this.orientations = orientations;
    }

    public void execute(Robot curr){
            if(curr.getPosition().getX() == super.getPosition().getX() && curr.getPosition().getY() == super.getPosition().getY()) {
                if (orientations.equals("clockwise")) {
                    curr.changeDirection(-90); //turn to the right
                } else if (orientations.equals("counterclockwise")) {
                    curr.changeDirection(90); //turn to the left
                }
            }
        }

        public String getOrientation(){
            return orientations.get(0);
        }

        public ArrayList<String> getOrientations() { return orientations; }
}

