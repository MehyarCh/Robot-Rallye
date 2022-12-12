package Desperatedrosseln.Local.Protocols;

public class CheckPointReached {

    /*
    Body:
        "clientID": 42,
        "number": 3
     */

    private int clientID;
    private int number;

    public int getClientID() {
        return clientID;
    }

    public int getNumber() {
        return number;
    }

    public CheckPointReached(int clientID, int number){
        this.clientID = clientID;
        this.number = number;
    }
}
