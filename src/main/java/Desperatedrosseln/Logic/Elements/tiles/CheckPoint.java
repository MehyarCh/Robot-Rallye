package Desperatedrosseln.Logic.Elements.tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Elements.Position;
import Desperatedrosseln.Logic.Player;

public class CheckPoint extends BoardElement {

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
}

