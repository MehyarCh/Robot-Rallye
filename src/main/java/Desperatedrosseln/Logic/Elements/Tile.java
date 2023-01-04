package Desperatedrosseln.Logic.Elements;

public class Tile {
    private String type;
    private String isOnBoard;

    public Tile(String type, String isOnBoard) {
        this.type = type;
        this.isOnBoard = isOnBoard;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsOnBoard() {
        return isOnBoard;
    }

    public void setIsOnBoard(String isOnBoard) {
        this.isOnBoard = isOnBoard;
    }
}

