package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;

import java.util.ArrayList;
import java.util.List;

public class ConveyorBelt extends BoardElement {
    private ArrayList<DIRECTION> directions;
    private int speed;
    private Position position;
    public ConveyorBelt(Position pos, int speed, ArrayList<DIRECTION> directions){
        this.position = pos;
        this.directions = directions;
        this.speed = speed;
    }
    @Override
    public void execute(List<Robot> active_robots){
        if(speed==1){
            for(Robot curr : active_robots){
                if(curr.getPosition()== this.position) {
                    //checks if robot is on this conveyor
                    for(DIRECTION direction : directions) {
                        // directions.size()> 1 then rotate
                        curr.moveByConveyor(1, direction);
                        //TODO: check if conveyor is curved + rules of being moved from conveyor or from different field
                        //IDEE: directions should tell if feld is curved or not
                    }
                }
            }
        }else if (speed==2){
            for(Robot curr : active_robots){
                if(curr.getPosition()== this.position) {
                    for (DIRECTION direction : directions) {
                        // directions.size()> 1 then rotate
                        curr.moveByConveyor(1, direction);
                        //TODO: check if conveyor is curved + rules of being moved from conveyor or from different field
                    }
                }
            }

        }
    }
    public int getSpeed(){
        return speed;
    }
    @Override
    public String toString(){
        return "ConveyorBelt";
    }
}
