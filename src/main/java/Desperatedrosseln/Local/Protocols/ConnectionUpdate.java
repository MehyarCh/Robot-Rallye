package Desperatedrosseln.Local.Protocols;

public class ConnectionUpdate {

    private int clientID;

    private boolean isConnected;

    public int getClientID() {
        return clientID;
    }

    public boolean getIsConnected() {
        return isConnected; //false
    }

    public String action;

    public ConnectionUpdate(int clientID, boolean isConnected, String action){
        this.clientID = clientID;
        this.isConnected = isConnected;
        this.action = action;
    }
}
