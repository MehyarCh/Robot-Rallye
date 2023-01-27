package Desperatedrosseln.Logic.Elements;


public class BoardElement {
    private String type;
    private String isOnBoard;
    private transient Position position;

    public BoardElement(String type, String isOnBoard) {
        this.type = type;
        this.isOnBoard = isOnBoard;
    }
    public void setPosition(int x, int y) {
        if(position!=null){
            position.setX(x);
            position.setY(y);
        }else{
            position = new Position(x,y);
        }
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