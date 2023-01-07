package Desperatedrosseln.Logic.Elements;

import java.util.List;

import static Desperatedrosseln.Logic.DIRECTION.UP;

public class Pit extends BoardElement {

    private Position position;
    public Pit(Position position) {
        this.position = position;
    }

    @Override
    public void execute(){

    }
    @Override
    public void execute(List<Robot> active_robots){
        for(Robot curr : active_robots) {
            if (curr.getPosition() == this.position) {
                curr.reboot(UP);
            }
        }
    }
}