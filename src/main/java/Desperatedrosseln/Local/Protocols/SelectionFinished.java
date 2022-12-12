package Desperatedrosseln.Local.Protocols;

public class SelectionFinished {

    //Body: "clientID": 42

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public SelectionFinished(int clientID) {
        this.clientID = clientID;
    }
}
