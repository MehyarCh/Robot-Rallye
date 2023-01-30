package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.DIRECTION;
import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.ArrayList;
import java.util.List;

public class Laser extends BoardElement {
    private ArrayList<String> orientations;
    private Position position;


    private int count;
    public Laser(String type, String isOnBoard, ArrayList<String> orientations, int count) {
        super(type, isOnBoard);
        this.orientations = orientations;
        this.count = count;
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

    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> active_robots){

    }


    /**
     *
     * @param active_robots
     * @return a list of robots
     */
    private List<Desperatedrosseln.Logic.Elements.Robot> robotsHit(List<Robot> active_robots){
        return  active_robots;
    }

    @Override
    public String toString(){
        return "Laser";
    }

    public DIRECTION getDirection(){
        switch (orientations.get(0)){
            case "top":
                return DIRECTION.TOP;
            case "bottom":
                return DIRECTION.BOTTOM;
            case "left":
               return DIRECTION.LEFT;
            case "right":
                return DIRECTION.RIGHT;
            default:
                return null;
        }

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
