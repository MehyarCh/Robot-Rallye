package Desperatedrosseln.Logic.Elements;

import javafx.geometry.Pos;

import java.util.ArrayList;

public class Gear extends BoardElement{

    ArrayList<String> orientations;
    private Position position;

    public Gear(Position position, String orientation) { //can either be clockwise or counterclockwise -> turning direction
        this.position = position;
        this.orientations = new ArrayList<>();
        orientations.add(orientation);
    }

    public void execute(Robot curr){
            if(curr.getPosition() == this.position) {
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
}

