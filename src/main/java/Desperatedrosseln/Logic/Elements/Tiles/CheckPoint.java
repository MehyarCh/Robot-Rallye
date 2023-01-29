package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Player;

public class CheckPoint extends BoardElement {

    private int x;
    private int y;
    private Position position;

    private int count;

    public CheckPoint(String type, String isInBoard, int count) {
        super(type, isInBoard);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void execute(Player player) {
        if (player.getNextCheckPoint() == this.count) {
            player.setNextCheckPoint();
        }
    }
    @Override
    public String toString(){
        return "CheckPoint";
    }
    @Override
    public Position getPosition() {
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

