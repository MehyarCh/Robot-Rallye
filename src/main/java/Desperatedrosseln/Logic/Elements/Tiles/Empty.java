package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;

public class Empty extends BoardElement {
    private Position position;
    public Empty(String type, String isOnBoard) {
        super(type, isOnBoard);
    }
    @Override
    public String toString(){
        return "Empty";
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