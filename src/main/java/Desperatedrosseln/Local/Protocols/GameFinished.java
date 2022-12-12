package Desperatedrosseln.Local.Protocols;

public class GameFinished {

    //Body: "clientID": 42

    private int clientID;

    public int getClientID() {
        return clientID;
    }

    public GameFinished(int clientID){
        this.clientID = clientID;
    }
}
