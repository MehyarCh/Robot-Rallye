package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.Direction;

public class RebootDirection {

    //Body: "direction": "right"
    private Direction direction;

    public Direction getDirection() {
        return direction;
    }

    public RebootDirection(Direction direction) {
        this.direction = direction;
    }
}
