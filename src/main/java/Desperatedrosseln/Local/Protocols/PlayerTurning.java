package Desperatedrosseln.Local.Protocols;

public class PlayerTurning {

    /*
    Body:
        "clientID": 42,
        "rotation": "counterclockwise"
     */

    private int clientID;
    private String rotation;

    public int getClientID() {
        return clientID;
    }

    public String getRotation() {
        return rotation;
    }

    public PlayerTurning(int clientID, String rotation) {
        this.clientID = clientID;
        this.rotation = rotation;
    }
}
