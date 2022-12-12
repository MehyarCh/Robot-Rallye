package Desperatedrosseln.Local.Protocols;

public class SetStartingPoint {

    /*Body:
        "x": 4,
        "y": 2
    */

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SetStartingPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
