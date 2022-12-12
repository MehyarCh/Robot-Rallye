package Desperatedrosseln.Local.Protocols;

public class Welcome {

    //Body: "clientID": 42

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public Welcome(int clientID) {
        this.clientID = clientID;
    }
}
