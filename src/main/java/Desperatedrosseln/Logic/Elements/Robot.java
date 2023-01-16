package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;

import java.util.*;

public class Robot extends BoardElement {

    public static ArrayList<Position> robotPositions;
    private DIRECTION direction;
    private final int ID;
    private Position position = super.getPosition();


    public Robot(int iD) {
        super("Robot","placeholder");
        if(robotPositions == null){
            robotPositions = new ArrayList<>();
        }
        robotPositions.add(position);
        this.direction = direction;

        this.ID = iD;

    }


    /**
     * moves the position of the robot depending on the Programming Card played in the register
     * @param steps the number of steps to move the robot
     */
    public void move(int steps){
        //TODO: check position is not out of board
        if (steps > 3 || steps < 1){
            //System.out.println("Es geht nicht");
            return;
        }
        for(int i = 0; i < steps; i++){
            switch (direction){
                case TOP -> {
                    position.setY(position.getY() - steps);
                }
                case BOTTOM -> {
                    position.setY(position.getY() + steps);
                }
                case LEFT -> {
                    position.setX(position.getX() - steps);
                }
                case RIGHT -> {
                    position.setX(position.getX() + steps);
                }
                default -> {

                }
            }
        }
    }
    public void moveByConveyor(int speed, DIRECTION direction){
        for(int i = 0; i < speed; i++){
            switch (direction){
                case TOP -> {
                    position.setY(position.getY() - 1);
                }
                case BOTTOM -> {
                    position.setY(position.getY() + 1);
                }
                case LEFT -> {
                    position.setX(position.getX() - 1);
                }
                case RIGHT -> {
                    position.setX(position.getX() + 1);
                }
                default -> {

                }
            }
        }
    }

    public int getID() {
        return ID;
    }

    public void push(String direction){

        switch (direction) {
            case "up":
                position.setY(position.getY() + 1);
                break;
            case "down":
                position.setY(position.getY() - 1);
                break;
            case "right":
                position.setX(position.getX() + 1);
                break;
            case "left":
                position.setX(position.getX() - 1);
                break;
        }

    }

    /**
     * Get the angle value of the direction, make sure it doesn't get out of the interval [0-360]
     * then get the new direction based on the new calculated angle
     * @param angle the angle that the direction changes in, exp: +90 for left, -90 for right
     */
    public void changeDirection(int angle) {
        int new_angle = this.direction.getAngle() + angle;
        if(new_angle>=360){
            new_angle-=360;
        }else if(new_angle<0){
            new_angle+=360;
        }
        this.direction = DIRECTION.valueOfDirection(new_angle);
    }

    private void rotate(){
        //rotation always clockwise
    }


    public void moveBack() {
        switch (direction) {
            case TOP -> {
                //move 1 down
                position.setY(position.getY() + 1);
            }
            case BOTTOM -> {
                //move 1 up
                position.setY(position.getY() - 1);
            }
            case LEFT -> {
                //move 1 right
                position.setX(position.getX() + 1);
            }
            case RIGHT -> {
                //move 1 left
                position.setX(position.getX() - 1);
            }
            default -> {

            }
        }
    }

    public void reboot(DIRECTION direction, Position newPos){
        //TODO: reboot robot
        setDirection(direction);
        //TODO: take robot actually out of the map
        position.copy(newPos);

    }


    public DIRECTION getDirection() {
        return direction;
    }

    private void setDirection(DIRECTION direction) {
        this.direction= direction;
    }

    @Override
    public String toString(){
        return "Robot";
    }

    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Robot)) {
            return false;
        }

        // typecast o to Robot so that we can compare data members
        Robot r = (Robot) o;

        // Compare the data members and return accordingly
        return this.getID() == r.getID();
    }
}

