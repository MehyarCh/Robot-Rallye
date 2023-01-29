package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;

public class EnergySpace extends BoardElement {

    private int count;

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
}
