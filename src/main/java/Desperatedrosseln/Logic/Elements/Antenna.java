package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;

public class Antenna extends BoardElement {

    private DIRECTION direction;
    private Position position;

    public Antenna(DIRECTION direction, Position position) {

        this.direction = direction;
    }
}

