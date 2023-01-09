package Desperatedrosseln.Logic.Elements.Tiles;

import Desperatedrosseln.Logic.Elements.BoardElement;
import Desperatedrosseln.Logic.Player;

public class CheckPoint extends BoardElement {

    private int x;
    private int y;

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

