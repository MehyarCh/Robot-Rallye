package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.List;

import static Desperatedrosseln.Logic.DIRECTION.TOP;

public class Pit extends BoardElement {
    private Position position;


    public Pit(String type, String isOnBoard) {
        super(type,isOnBoard);
    }


    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> active_robots){

    }
    @Override
    public String toString(){
        return "Pit";
    }
    @Override
    public Position getPosition() {
        return position;
    }


}