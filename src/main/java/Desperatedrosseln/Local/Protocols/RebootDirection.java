package Desperatedrosseln.Local.Protocols;

import Desperatedrosseln.Logic.DIRECTION;

public class RebootDirection {

    //Body: "direction": "right"
    private DIRECTION direction;

    public DIRECTION getDirection() {
        return direction;
    }

    public RebootDirection(DIRECTION direction) {
        this.direction = direction;
    }
}
