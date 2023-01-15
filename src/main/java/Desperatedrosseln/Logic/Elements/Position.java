package Desperatedrosseln.Logic.Elements;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        //switched x and y ToDO: fix
        this.x = y;
        this.y = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void copy(Position pos){
        x = pos.getX();
        y = pos.getY();
    }

    public void setY(int y) {
        this.y = y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public boolean equals(Position otherPosition){
        return x == otherPosition.getX() && y == otherPosition.getY();
    }
}
