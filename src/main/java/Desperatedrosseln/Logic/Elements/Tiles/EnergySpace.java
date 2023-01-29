package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;

public class EnergySpace extends BoardElement {

    private int count;
    private Position position;

    public EnergySpace(String type, String isOnBoard, int count) {
        super(type, isOnBoard);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    @Override
    public String toString(){
        return "EnergySpace";
    }
    public boolean hasEnergySpace() {
        if(count>0){
            return true;
        }
        return false;
    }
    public void takeCube(){
        if(count>0){
            count--;
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
