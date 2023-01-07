package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;

import java.util.List;

public class Laser extends BoardElement {
    private Position position;
    private DIRECTION direction;

    public Laser(Position position, DIRECTION direction){
        this.position= position;
        this.direction= direction;
    }

    @Override
    public void execute(List<Robot> active_robots){

    }


    /**
     *
     * @param active_robots
     * @return a list of robots
     */
    private List<Robot> robotsHit(List<Robot> active_robots){
        return  active_robots;
    }

    @Override
    public String toString(){
        return "Laser";
    }
    public DIRECTION getDirection(){
        return this.direction;
    }
}
