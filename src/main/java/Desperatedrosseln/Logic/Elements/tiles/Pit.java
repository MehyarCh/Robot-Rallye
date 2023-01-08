package Desperatedrosseln.Logic.Elements.tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Elements.Robot;

import java.util.List;

import static Desperatedrosseln.Logic.DIRECTION.TOP;

public class Pit extends BoardElement {


    public Pit(String type, String isOnBoard) {
        super(type,isOnBoard);
    }


    public void execute(List<Desperatedrosseln.Logic.Elements.Robot> active_robots){
        for(Robot curr : active_robots) {
            if (curr.getPosition().getX() == super.getPosition().getX() || curr.getPosition().getY() == super.getPosition().getY()) {
                curr.reboot(TOP);
            }
        }
    }


}