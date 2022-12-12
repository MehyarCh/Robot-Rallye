package Desperatedrosseln.Local.Protocols;

public class PlayerAdded {

    /*
    Body:
        "clientID": 42,
        "name": "Nr. 5",
        "figure": 5
     */

    private int clientID;
    private String name;
    private int figure;

    public int getClientID() {
        return clientID;
    }

    public String getName() {
        return name;
    }

    public int getFigure() {
        return figure;
    }

    public PlayerAdded(int clientID, String name, int figure){
        this.clientID = clientID;
        this.name = name;
        this.figure = figure;
    }
}
