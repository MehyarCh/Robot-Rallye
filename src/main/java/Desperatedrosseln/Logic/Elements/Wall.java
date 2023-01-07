package Desperatedrosseln.Logic.Elements;

import Desperatedrosseln.Logic.DIRECTION;

public class Wall extends BoardElement{
    private DIRECTION direction;
    private Position position;

    public DIRECTION getDirection() {
        return direction;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public String toString(){
        return "Wall";
    }
}
