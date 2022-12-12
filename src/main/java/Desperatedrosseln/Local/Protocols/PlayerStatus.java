package Desperatedrosseln.Local.Protocols;

public class PlayerStatus {

    /*
    Body:
        "clientID": 42,
        "ready": true
     */
    private int clientID;
    private boolean ready;

    public int getClientID() {
        return clientID;
    }

    public boolean isReady() {
        return ready;
    }

    public PlayerStatus(int clientID, boolean ready) {
        this.clientID = clientID;
        this.ready = ready;
    }
}
