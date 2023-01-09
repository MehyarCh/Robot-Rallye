package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.Direction;

import java.util.*;

public class Robot extends BoardElement {

    public static ArrayList<Position> robotPositions;
    private Direction direction;
    private final int ID;
    private Position position;


    public Robot(int iD) { //ToDo: verify
        if(robotPositions == null){
            robotPositions = new ArrayList<>();
        }
        robotPositions.add(position);
        this.direction = direction;

        this.ID = iD;

    }
    @Override
    public void execute(){

    }

    public void move(int steps){
        if (steps > 3 || steps < 1){
            //System.out.println("Es geht nicht");
            return;
        }
        for(int i = 0; i < steps; i++){
            switch (direction){
                case UP -> {
                    position.setY(position.getY() - steps);
                }
                case DOWN -> {
                    position.setY(position.getY() + steps);
                }
                case LEFT -> {
                    position.setX(position.getX() - steps);
                }
                case RIGHT -> {
                    position.setX(position.getX() + steps);
                }
            }
        }
    }

    public int getID() {
        return ID;
    }

    private void push(){

    }
    private void rotate(){
        //rotation always clockwise
    }
}
