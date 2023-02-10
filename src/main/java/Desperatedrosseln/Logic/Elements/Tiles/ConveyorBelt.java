package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class ConveyorBelt extends BoardElement {
    private int speed;
    private ArrayList<String> orientations;
    //out then in(s)
    private Position position;
    private boolean isCurved;




    public ConveyorBelt(String type, String isOnBoard, int speed, ArrayList<String> orientations) {
        super(type, isOnBoard);
        this.speed = speed;
        this.orientations = orientations;
        isCurved = isCurved();
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

    public boolean isCurved(){
        if( speed == 2 && orientations.size()>2){
            return true;
        }else if (speed == 1 ){
            DIRECTION in = DIRECTION.stringToDirection(orientations.get(0));
            DIRECTION out = DIRECTION.stringToDirection(orientations.get(1));
            if(!DIRECTION.getOppositeOf(in).equals(out)){
                return true;
            }
        }
        return false;
    }
    public Position calculateNextPos(Robot robot){
        int x = robot.getPosition().getX();
        int y = robot.getPosition().getY();
        if( speed == 1 ){
            if(!isCurved) {
                DIRECTION dir = DIRECTION.stringToDirection(orientations.get(0));
                switch (dir) {
                    case TOP -> {
                        y = y - 1;
                    }
                    case BOTTOM -> {
                        y = y + 1;
                    }
                    case LEFT -> {
                        x = x - 1;
                    }
                    case RIGHT -> {
                        x = x + 1;
                    }
                    default -> {

                    }
                }
                //returns new position
            } else {
                //return same position
                //green belt is curved
                //if the direction the robot is coming from is "in" (zufluss)
                //rotate robot in (in - out) if 90 counterclockwise, if -90 clockwise
            }
        } else {
            //speed 2
            if (!isCurved ){
                //move 1
                //check new position
            }
        }
        return new Position(x,y);
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
