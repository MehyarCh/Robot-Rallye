package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class ConveyorBelt extends BoardElement {
    private int speed;
    private ArrayList<String> orientations;
    private Position position;




    public ConveyorBelt(String type, String isOnBoard, int speed, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.speed = speed;
        this.orientations = orientations;
    }

    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> active_robots) {
        ArrayList<DIRECTION> directions = new ArrayList<>();
        for (String dir : orientations) {
            switch (dir) {
                case "top":
                    directions.add(DIRECTION.TOP);
                    break;
                case "bottom":
                    directions.add(DIRECTION.BOTTOM);
                    break;
                case "left":
                    directions.add(DIRECTION.LEFT);
                    break;
                case "right":
                    directions.add(DIRECTION.RIGHT);
                    break;
            }
        }
        if (speed == 1) {
            for (Desperatedrosseln.Logic.Elements.Robot curr : active_robots) {
                if (curr.getPosition().getX() == super.getPosition().getX() &&curr.getPosition().getY() == super.getPosition().getY()) {
                    //checks if robot is on this conveyor
                    for (DIRECTION direction : directions) {
                        // directions.size()> 1 then rotate
                        curr.moveByConveyor(1, direction);
                        //TODO: check if conveyor is curved + rules of being moved from conveyor or from different field
                        //IDEE: directions should tell if feld is curved or not
                    }
                }
            }
        } else if (speed == 2) {
            for (Robot curr : active_robots) {
                if (curr.getPosition().getX() == super.getPosition().getX() && curr.getPosition().getY() == super.getPosition().getY()) {
                    for (DIRECTION direction : directions) {
                        // directions.size()> 1 then rotate
                        curr.moveByConveyor(1, direction);
                        //TODO: check if conveyor is curved + rules of being moved from conveyor or from different field
                    }
                }
            }

        }
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

    @Override
    public String toString(){
        return "ConveyorBelt";
    }
    @Override
    public Position getPosition() {
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
