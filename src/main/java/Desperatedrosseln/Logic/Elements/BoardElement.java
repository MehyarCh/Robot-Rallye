package Desperatedrosseln.Logic.Elements;

import java.util.List;

public class BoardElement {

    public List<String> types;

    private String type;
    private String isOnBoard;
    private transient Position position;

    public BoardElement(String type, String isOnBoard) {
        this.type = type;
        this.isOnBoard = isOnBoard;
    }
    public void setPosition(int x, int y) {
        position = new Position(x, y);
    }

    public Position getPosition() {
        return position;
    }
    public String getType() {
        return type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }
}