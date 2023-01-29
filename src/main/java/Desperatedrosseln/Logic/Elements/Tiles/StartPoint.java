package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;

public class StartPoint extends BoardElement {
    private Position position;
    public StartPoint(String type, String isInBoard) {
        super(type, isInBoard);
    }
    @Override
    public String toString(){
        return "StartPoint";
    }
    @Override
    public Position getPosition() {
        return position;
    }
}
