package Desperatedrosseln.Local.Protocols;

public class CurrentPlayer {

    //Body: "clientID": 7

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public CurrentPlayer(int clientID){
        this.clientID = clientID;
    }
}
