package Desperatedrosseln.Local.Protocols;

public class Movement {

    /*
    Body:
        "clientID": 42,
        "x": 4,
        "y": 2
     */
    private int clientID;
    private int x;
    private int y;

    public int getClientID() {
        return clientID;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Movement(int clientID, int x, int y){
        this.clientID = clientID;
        this.x = x;
        this.y = y;
    }
}
