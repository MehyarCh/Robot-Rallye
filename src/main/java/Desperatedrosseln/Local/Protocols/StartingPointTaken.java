package Desperatedrosseln.Local.Protocols;

public class StartingPointTaken {

    /*
    Body:
        "x": 4,
        "y": 2,
        "clientID": 42
     */

    private int x;
    private int y;
    private int clientID;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getClientID() {
        return clientID;
    }

    public StartingPointTaken(int x, int y, int clientID) {
        this.x = x;
        this.y = y;
        this.clientID = clientID;
    }
}
