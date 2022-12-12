package Desperatedrosseln.Local.Protocols;

public class Reboot {

    //Body: "clientID": 42

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public Reboot(int clientID) {
        this.clientID = clientID;
    }
}
